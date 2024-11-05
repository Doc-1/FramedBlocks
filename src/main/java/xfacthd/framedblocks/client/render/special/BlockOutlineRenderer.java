package xfacthd.framedblocks.client.render.special;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.render.RegisterOutlineRenderersEvent;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.common.config.ClientConfig;
import xfacthd.framedblocks.common.config.DevToolsConfig;

import java.util.*;

public final class BlockOutlineRenderer
{
    private static final Map<IBlockType, OutlineRenderer> OUTLINE_RENDERERS = new IdentityHashMap<>();
    private static final Set<IBlockType> ERRORED_TYPES = new HashSet<>();
    private static final RandomSource RANDOM = RandomSource.create();

    public static void onRenderBlockHighlight(final RenderHighlightEvent.Block event)
    {
        if (!ClientConfig.VIEW.useFancySelectionBoxes() && !DevToolsConfig.VIEW.isOcclusionShapeDebugRenderingEnabled())
        {
            return;
        }

        BlockHitResult result = event.getTarget();
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        BlockState state = level.getBlockState(result.getBlockPos());
        if (!(state.getBlock() instanceof IFramedBlock block))
        {
            return;
        }

        if (DevToolsConfig.VIEW.isOcclusionShapeDebugRenderingEnabled())
        {
            VoxelShape shape = state.getOcclusionShape();
            boolean highContrast = Minecraft.getInstance().options.highContrastBlockOutline().get();
            Vec3 offset = Vec3.atLowerCornerOf(result.getBlockPos()).subtract(event.getCamera().getPosition());

            if (highContrast)
            {
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());
                ShapeRenderer.renderShape(event.getPoseStack(), builder, shape, offset.x, offset.y, offset.z, 0xFF000000);
            }
            VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());
            int lineColor = highContrast ? 0xFF57FFE1 : ARGB.color(0x66, 0xFF000000);
            ShapeRenderer.renderShape(event.getPoseStack(), builder, shape, offset.x, offset.y, offset.z, lineColor);

            event.setCanceled(true);
            return;
        }

        IBlockType type = block.getBlockType();
        if (type.hasSpecialHitbox())
        {
            OutlineRenderer renderer = OUTLINE_RENDERERS.get(type);
            if (renderer == null)
            {
                if (ERRORED_TYPES.add(type))
                {
                    FramedBlocks.LOGGER.error("IBlockType '{}' requests custom outline rendering but no OutlineRender was registered!", type.getName());
                }
                return;
            }

            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
            ModelData modelData = level.getModelData(result.getBlockPos());
            RANDOM.setSeed(42);
            boolean hasTranslucent = model.getRenderTypes(state, RANDOM, modelData).contains(RenderType.translucent());
            if (hasTranslucent == event.isForTranslucentBlocks())
            {
                PoseStack mstack = event.getPoseStack();
                Vec3 offset = Vec3.atLowerCornerOf(result.getBlockPos()).subtract(event.getCamera().getPosition());
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());

                mstack.pushPose();
                mstack.translate(offset.x, offset.y, offset.z);
                mstack.translate(.5, .5, .5);
                renderer.rotateMatrix(mstack, state);
                mstack.translate(-.5, -.5, -.5);

                // TODO: handle high-contrast outline setting
                renderer.draw(state, level, result.getBlockPos(), mstack, builder);

                mstack.popPose();
            }
            event.setCanceled(true);
        }
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



    private BlockOutlineRenderer() { }
}
