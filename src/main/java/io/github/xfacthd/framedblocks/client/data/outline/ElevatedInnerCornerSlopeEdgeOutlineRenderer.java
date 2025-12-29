package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.CornerType;
import net.minecraft.world.level.block.state.BlockState;

public final class ElevatedInnerCornerSlopeEdgeOutlineRenderer implements SimpleOutlineRenderer
{
    public static final ElevatedInnerCornerSlopeEdgeOutlineRenderer INSTANCE = new ElevatedInnerCornerSlopeEdgeOutlineRenderer();

    private ElevatedInnerCornerSlopeEdgeOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Top face
        drawer.drawLine(  0,   1,   1,   1,   1,   1);
        drawer.drawLine(  1,   1,   0,   1,   1,   1);
        drawer.drawLine(  0,   1, .5F, .5F,   1, .5F);
        drawer.drawLine(.5F,   1,   0, .5F,   1, .5F);
        drawer.drawLine(.5F,   1,   0,   1,   1,   0);
        drawer.drawLine(  0,   1, .5F,   0,   1,   1);

        //Bottom face
        drawer.drawLine(0, 0, 1, 1, 0, 1);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(0, 0, 0, 0, 0, 1);

        //Vertical edges
        drawer.drawLine(1, 0, 1, 1,   1, 1);
        drawer.drawLine(1, 0, 0, 1,   1, 0);
        drawer.drawLine(0, 0, 1, 0,   1, 1);
        drawer.drawLine(0, 0, 0, 0, .5F, 0);

        //Slope edges
        drawer.drawLine(0, .5F, 0,   0, 1, .5F);
        drawer.drawLine(0, .5F, 0, .5F, 1,   0);
        drawer.drawLine(0, .5F, 0, .5F, 1, .5F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

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
