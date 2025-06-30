package xfacthd.framedblocks.client.screen.widget;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class BlockPreviewTooltipComponent implements ClientTooltipComponent
{
    private static final int SIZE = 36;
    private static final float STACK_SCALE = 48;
    private static final Quaternionf ROT_22_5_XP = Axis.XP.rotationDegrees(22.5F);

    private final TrackingItemStackRenderState renderState;

    public BlockPreviewTooltipComponent(Component component)
    {
        this.renderState = component.renderState;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics)
    {
        graphics.submitPictureInPictureRenderState(new BlockPreviewPictureInPictureRenderState(
                renderState,
                (int) (System.currentTimeMillis() / 20 % 360),
                x, y, x + SIZE, y + SIZE, STACK_SCALE, graphics.peekScissorStack()
        ));
    }

    @Override
    public int getWidth(Font font)
    {
        return SIZE;
    }

    @Override
    public int getHeight(Font font)
    {
        return SIZE;
    }



    public record Component(TrackingItemStackRenderState renderState) implements TooltipComponent { }

    public record BlockPreviewPictureInPictureRenderState(
            TrackingItemStackRenderState renderState,
            int rotY,
            int x0,
            int y0,
            int x1,
            int y1,
            float scale,
            @Nullable ScreenRectangle bounds,
            @Nullable ScreenRectangle scissorArea
    ) implements PictureInPictureRenderState
    {
        public BlockPreviewPictureInPictureRenderState(
                TrackingItemStackRenderState renderState,
                int rotY,
                int x0,
                int y0,
                int x1,
                int y1,
                float scale,
                @Nullable ScreenRectangle scissorArea
        )
        {
            this(renderState, rotY, x0, y0, x1, y1, scale, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea), scissorArea);
        }
    }

    public static final class BlockPreviewPictureInPictureRenderer extends PictureInPictureRenderer<BlockPreviewPictureInPictureRenderState>
    {
        @Nullable
        private Object lastModelIdentity = null;
        private int lastRotY = 0;

        public BlockPreviewPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource)
        {
            super(bufferSource);
        }

        @Override
        protected void renderToTexture(BlockPreviewPictureInPictureRenderState state, PoseStack poseStack)
        {
            TrackingItemStackRenderState renderState = state.renderState;

            poseStack.scale(1, -1, -1);
            poseStack.mulPose(new Matrix4f()
                    .rotate(ROT_22_5_XP)
                    .rotate(Axis.YP.rotationDegrees(state.rotY))
            );

            // FIXME: renders way too dark (using a block renderer doesn't fix it)
            Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
            renderState.render(poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

            lastModelIdentity = renderState.getModelIdentity();
            lastRotY = state.rotY;
        }

        @Override
        protected float getTranslateY(int height, int guiScale)
        {
            return height / 2F;
        }

        @Override
        protected boolean textureIsReadyToBlit(BlockPreviewPictureInPictureRenderState state)
        {
            if (state.rotY != lastRotY) return false;

            TrackingItemStackRenderState renderState = state.renderState;
            return !renderState.isAnimated() && renderState.getModelIdentity().equals(lastModelIdentity);
        }

        @Override
        protected String getTextureLabel()
        {
            return "framedblocks saw preview";
        }

        @Override
        public Class<BlockPreviewPictureInPictureRenderState> getRenderStateClass()
        {
            return BlockPreviewPictureInPictureRenderState.class;
        }
    }
}
