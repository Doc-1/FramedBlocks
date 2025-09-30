package io.github.xfacthd.framedblocks.client.render.special;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.xfacthd.framedblocks.FramedBlocks;
import io.github.xfacthd.framedblocks.api.block.IBlockType;
import io.github.xfacthd.framedblocks.api.block.IFramedBlock;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.RegisterOutlineRenderersEvent;
import io.github.xfacthd.framedblocks.common.config.ClientConfig;
import io.github.xfacthd.framedblocks.common.config.DevToolsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class BlockOutlineRenderer
{
    private static final int DEFAULT_LINE_COLOR = ARGB.color(0x66, 0xFF000000);
    private static final Map<IBlockType, OutlineRenderer<?>> OUTLINE_RENDERERS = new IdentityHashMap<>();
    private static final Set<IBlockType> ERRORED_TYPES = new HashSet<>();

    public static void onRenderBlockHighlight(ExtractBlockOutlineRenderStateEvent event)
    {
        if (!ClientConfig.VIEW.useFancySelectionBoxes() && !DevToolsConfig.VIEW.isOcclusionShapeDebugRenderingEnabled())
        {
            return;
        }

        BlockHitResult result = event.getHitResult();
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        BlockState state = level.getBlockState(result.getBlockPos());
        if (!(state.getBlock() instanceof IFramedBlock block))
        {
            return;
        }

        if (DevToolsConfig.VIEW.isOcclusionShapeDebugRenderingEnabled())
        {
            VoxelShape shape = state.getOcclusionShape();
            Vec3 offset = Vec3.atLowerCornerOf(result.getBlockPos()).subtract(event.getCamera().getPosition());

            event.addCustomRenderer((renderState, buffer, poseStack, translucentPass, levelRenderState) ->
            {
                if (translucentPass == renderState.isTranslucent())
                {
                    boolean highContrast = renderState.highContrast();
                    if (highContrast)
                    {
                        VertexConsumer builder = buffer.getBuffer(RenderType.secondaryBlockOutline());
                        ShapeRenderer.renderShape(poseStack, builder, shape, offset.x, offset.y, offset.z, 0xFF000000);
                    }
                    VertexConsumer builder = buffer.getBuffer(RenderType.lines());
                    int lineColor = highContrast ? CommonColors.HIGH_CONTRAST_DIAMOND : DEFAULT_LINE_COLOR;
                    ShapeRenderer.renderShape(poseStack, builder, shape, offset.x, offset.y, offset.z, lineColor);
                }
                return true;
            });
            return;
        }

        IBlockType type = block.getBlockType();
        if (type.hasSpecialHitbox())
        {
            OutlineRenderer<Object> renderer = getRenderer(type);
            if (renderer == null)
            {
                if (ERRORED_TYPES.add(type))
                {
                    FramedBlocks.LOGGER.error("IBlockType '{}' requests custom outline rendering but no OutlineRender was registered!", type.getName());
                }
                return;
            }

            Object data = renderer.extractOutlineData(state, level, result.getBlockPos());
            if (data == null) return;

            Vec3 offset = Vec3.atLowerCornerOf(result.getBlockPos()).subtract(event.getCamera().getPosition());
            event.addCustomRenderer((renderState, buffer, poseStack, translucentPass, levelRenderState) ->
            {
                if (translucentPass == renderState.isTranslucent())
                {
                    poseStack.pushPose();
                    poseStack.translate(offset.x, offset.y, offset.z);
                    poseStack.translate(.5, .5, .5);
                    renderer.rotateMatrix(poseStack, state);
                    poseStack.translate(-.5, -.5, -.5);

                    AbstractLineDrawer drawer = createDrawer(poseStack.last(), buffer);
                    renderer.draw(state, data, drawer);
                    drawer.finish();

                    poseStack.popPose();
                }
                return true;
            });
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static OutlineRenderer<Object> getRenderer(IBlockType type)
    {
        return (OutlineRenderer<Object>) OUTLINE_RENDERERS.get(type);
    }

    private static AbstractLineDrawer createDrawer(PoseStack.Pose pose, MultiBufferSource buffer)
    {
        if (Minecraft.getInstance().options.highContrastBlockOutline().get())
        {
            return new HighContrastLineDrawer(pose, buffer);
        }
        return new DefaultLineDrawer(pose, buffer);
    }

    public static void init()
    {
        ModLoader.postEvent(new RegisterOutlineRenderersEvent((type, renderer) ->
        {
            Preconditions.checkArgument(
                    type.hasSpecialHitbox(),
                    "IBlockType %s doesn't return true from IBlockType#hasSpecialHitbox()",
                    type
            );
            OUTLINE_RENDERERS.put(type, renderer);
        }));
    }

    public static boolean hasOutlineRenderer(IBlockType type)
    {
        return OUTLINE_RENDERERS.containsKey(type);
    }

    private static abstract class AbstractLineDrawer implements SimpleOutlineRenderer.LineDrawer
    {
        final PoseStack.Pose pose;

        AbstractLineDrawer(PoseStack.Pose pose)
        {
            this.pose = pose;
        }

        abstract void finish();

        void drawLine(VertexConsumer builder, float x1, float y1, float z1, float x2, float y2, float z2, int color)
        {
            float nX = x2 - x1;
            float nY = y2 - y1;
            float nZ = z2 - z1;
            float nLen = Mth.sqrt(nX * nX + nY * nY + nZ * nZ);

            nX = nX / nLen;
            nY = nY / nLen;
            nZ = nZ / nLen;

            builder.addVertex(pose, x1, y1, z1).setColor(color).setNormal(pose, nX, nY, nZ);
            builder.addVertex(pose, x2, y2, z2).setColor(color).setNormal(pose, nX, nY, nZ);
        }
    }

    private static final class DefaultLineDrawer extends AbstractLineDrawer
    {
        private final VertexConsumer builder;

        DefaultLineDrawer(PoseStack.Pose pose, MultiBufferSource buffer)
        {
            super(pose);
            this.builder = buffer.getBuffer(RenderType.lines());
        }

        @Override
        public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2)
        {
            drawLine(builder, x1, y1, z1, x2, y2, z2, DEFAULT_LINE_COLOR);
        }

        @Override
        void finish() { }
    }

    private static final class HighContrastLineDrawer extends AbstractLineDrawer
    {
        private static final int LINE_SIZE = 6;
        private static final int INITIAL_LINE_COUNT = 32;

        private final MultiBufferSource buffer;
        private float[] lines;
        private int pointer;

        HighContrastLineDrawer(PoseStack.Pose pose, MultiBufferSource buffer)
        {
            super(pose);
            this.buffer = buffer;
            this.lines = new float[INITIAL_LINE_COUNT * LINE_SIZE];
        }

        @Override
        public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2)
        {
            if (pointer + LINE_SIZE > lines.length)
            {
                float[] newLines = new float[lines.length + (INITIAL_LINE_COUNT * LINE_SIZE)];
                System.arraycopy(lines, 0, newLines, 0, lines.length);
                lines = newLines;
            }

            lines[pointer] = x1;
            lines[pointer + 1] = y1;
            lines[pointer + 2] = z1;
            lines[pointer + 3] = x2;
            lines[pointer + 4] = y2;
            lines[pointer + 5] = z2;

            pointer += LINE_SIZE;
        }

        @Override
        void finish()
        {
            drawBufferedLines(RenderType.secondaryBlockOutline(), CommonColors.BLACK);
            drawBufferedLines(RenderType.lines(), CommonColors.HIGH_CONTRAST_DIAMOND);
        }

        private void drawBufferedLines(RenderType renderType, int color)
        {
            VertexConsumer builder = buffer.getBuffer(renderType);
            for (int i = 0; i < pointer; i += LINE_SIZE)
            {
                drawLine(builder, lines[i], lines[i + 1], lines[i + 2], lines[i + 3], lines[i + 4], lines[i + 5], color);
            }
        }
    }



    private BlockOutlineRenderer() { }
}
