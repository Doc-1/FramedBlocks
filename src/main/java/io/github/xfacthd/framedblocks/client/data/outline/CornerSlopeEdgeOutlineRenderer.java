package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.CornerType;
import net.minecraft.world.level.block.state.BlockState;

public final class CornerSlopeEdgeOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        if (state.getValue(PropertyHolder.ALT_TYPE))
        {
            //Back edges
            drawer.drawLine(.5F, .5F, .5F, .5F, 1, .5F);
            drawer.drawLine(  1, .5F, .5F,   1, 1, .5F);
            drawer.drawLine(.5F, .5F,   1, .5F, 1,   1);
            drawer.drawLine(.5F, .5F, .5F, .5F, .5F,   1);
            drawer.drawLine(.5F, .5F, .5F,   1, .5F, .5F);
            drawer.drawLine(.5F,   1, .5F, .5F,   1,   1);
            drawer.drawLine(.5F,   1, .5F,   1,   1, .5F);

            //Bottom face
            drawer.drawLine(0, .5F, 0,   0, .5F,   1);
            drawer.drawLine(0, .5F, 0,   1, .5F,   0);
            drawer.drawLine(1, .5F, 0,   1, .5F, .5F);
            drawer.drawLine(0, .5F, 1, .5F, .5F,   1);

            //Slope
            drawer.drawLine(0, .5F, 0, .5F, 1, .5F);
            drawer.drawLine(1, .5F, 0,   1, 1, .5F);
            drawer.drawLine(0, .5F, 1, .5F, 1,   1);
        }
        else
        {
            //Back edge
            drawer.drawLine(1, 0, 1, 1, .5F, 1);

            //Bottom face
            drawer.drawLine(.5F, 0, .5F, .5F, 0,   1);
            drawer.drawLine(.5F, 0, .5F,   1, 0, .5F);
            drawer.drawLine(  1, 0, .5F,   1, 0,   1);
            drawer.drawLine(.5F, 0,   1,   1, 0,   1);

            //Slope
            drawer.drawLine(.5F, 0, .5F, 1, .5F, 1);
            drawer.drawLine(  1, 0, .5F, 1, .5F, 1);
            drawer.drawLine(.5F, 0,   1, 1, .5F, 1);
        }
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        CornerType type = state.getValue(PropertyHolder.CORNER_TYPE);
        if (!type.isHorizontal())
        {
            if (type.isTop())
            {
                OutlineRenderer.mirrorHorizontally(poseStack, true);
            }
        }
        else
        {
            poseStack.mulPose(Quaternions.XN_90);
            switch (type)
            {
                case HORIZONTAL_TOP_RIGHT -> poseStack.mulPose(Quaternions.YN_90);
                case HORIZONTAL_BOTTOM_LEFT -> poseStack.mulPose(Quaternions.YP_90);
                case HORIZONTAL_BOTTOM_RIGHT -> poseStack.mulPose(Quaternions.YP_180);
            }
        }
    }
}
