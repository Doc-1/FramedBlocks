package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.api.render.Quaternions;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.CornerType;

public final class ElevatedCornerSlopeEdgeOutlineRenderer implements OutlineRenderer
{
    public static final ElevatedCornerSlopeEdgeOutlineRenderer INSTANCE = new ElevatedCornerSlopeEdgeOutlineRenderer();

    private ElevatedCornerSlopeEdgeOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back edges
        drawer.drawLine(  1, 1, .5F,   1,   1,   1);
        drawer.drawLine(.5F, 1, .5F, .5F,   1,   1);
        drawer.drawLine(.5F, 1,   1,   1,   1,   1);
        drawer.drawLine(.5F, 1, .5F,   1,   1, .5F);
        drawer.drawLine(  1, 0,   1,   1,   1,   1);

        //Front edges
        drawer.drawLine(  0, 0,   1,   0, .5F,   1);
        drawer.drawLine(  1, 0,   0,   1, .5F,   0);
        drawer.drawLine(  0, 0,   0,   0, .5F,   0);

        //Bottom face
        drawer.drawLine(0, .5F, 0,   0, .5F,   1);
        drawer.drawLine(0, .5F, 0,   1, .5F,   0);
        drawer.drawLine(0,   0, 0,   0,   0,   1);
        drawer.drawLine(1,   0, 0,   1,   0,   1);
        drawer.drawLine(0,   0, 0,   0,   0,   1);
        drawer.drawLine(0,   0, 1,   1,   0,   1);
        drawer.drawLine(0,   0, 0,   1,   0,   0);

        //Slope
        drawer.drawLine(0, .5F, 0, .5F, 1, .5F);
        drawer.drawLine(1, .5F, 0,   1, 1, .5F);
        drawer.drawLine(0, .5F, 1, .5F, 1,   1);
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
