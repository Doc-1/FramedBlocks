package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.Quaternions;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.CornerType;

public final class CornerSlopeOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        CornerType type = state.getValue(PropertyHolder.CORNER_TYPE);
        if (!type.isHorizontal())
        {
            //Back edge
            drawer.drawLine(1, 0, 1, 1, 1, 1);

            //Bottom face
            drawer.drawLine(0, 0, 0, 0, 0, 1);
            drawer.drawLine(0, 0, 0, 1, 0, 0);
            drawer.drawLine(1, 0, 0, 1, 0, 1);
            drawer.drawLine(0, 0, 1, 1, 0, 1);

            //Slope
            drawer.drawLine(0, 0, 0, 1, 1, 1);
            drawer.drawLine(1, 0, 0, 1, 1, 1);
            drawer.drawLine(0, 0, 1, 1, 1, 1);
        }
        else
        {
            //Back face
            drawer.drawLine(0, 0, 1, 1, 0, 1);
            drawer.drawLine(0, 1, 1, 1, 1, 1);
            drawer.drawLine(0, 0, 1, 0, 1, 1);
            drawer.drawLine(1, 0, 1, 1, 1, 1);

            //Back edge
            drawer.drawLine(0, 0, 0, 0, 0, 1);

            //Center slope edge
            drawer.drawLine(0, 0, 0, 1, 1, 1);

            //Side slope edges
            drawer.drawLine(0, 0, 0, 0, 1, 1);
            drawer.drawLine(0, 0, 0, 1, 0, 1);
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
            if (!type.isRight())
            {
                poseStack.mulPose(Quaternions.ZP_90);
            }
            if (type.isTop())
            {
                poseStack.mulPose(type.isRight() ? Quaternions.ZN_90 : Quaternions.ZP_90);
            }
        }
    }
}
