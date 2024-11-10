package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

public final class SmallCornerSlopePanelWallOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Back face
        drawer.drawLine(.5F, .5F, 1,   1, .5F, 1);
        drawer.drawLine(.5F,   1, 1,   1,   1, 1);
        drawer.drawLine(.5F, .5F, 1, .5F,   1, 1);
        drawer.drawLine(  1, .5F, 1,   1,   1, 1);

        // Horizontal edge
        drawer.drawLine(1, 1, 0, 1, 1, 1);

        // Slopes
        drawer.drawLine(1, 1, 0,   1, .5F, 1);
        drawer.drawLine(1, 1, 0, .5F,   1, 1);
        drawer.drawLine(1, 1, 0, .5F, .5F, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        poseStack.mulPose(SlopePanelOutlineRenderer.ROTATIONS[rotation.ordinal()]);
    }
}
