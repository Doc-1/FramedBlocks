package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.api.render.Quaternions;
import xfacthd.framedblocks.common.data.PropertyHolder;

public class SlopeEdgeOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Horizontal perpendicular edges
        drawer.drawLine(0F,  0F,  1F, 1F,  0F,  1F);
        drawer.drawLine(0F,  0F, .5F, 1F,  0F, .5F);
        drawer.drawLine(0F, .5F,  1F, 1F, .5F,  1F);

        // Vertical edges
        drawer.drawLine(0F, 0F, 1F, 0F, .5F, 1F);
        drawer.drawLine(1F, 0F, 1F, 1F, .5F, 1F);

        // Horizontal parallel edges
        drawer.drawLine(0F, 0F, .5F, 0F, 0F, 1F);
        drawer.drawLine(1F, 0F, .5F, 1F, 0F, 1F);

        // Sloped edges
        drawer.drawLine(0F, 0F, .5F, 0F, .5F, 1F);
        drawer.drawLine(1F, 0F, .5F, 1F, .5F, 1F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);
        switch (state.getValue(PropertyHolder.SLOPE_TYPE))
        {
            case TOP -> poseStack.mulPose(Quaternions.ZP_180);
            case HORIZONTAL -> poseStack.mulPose(Quaternions.ZP_90);
        }
        if (state.getValue(PropertyHolder.ALT_TYPE))
        {
            poseStack.translate(0F, .5F, -.5F);
        }
    }
}
