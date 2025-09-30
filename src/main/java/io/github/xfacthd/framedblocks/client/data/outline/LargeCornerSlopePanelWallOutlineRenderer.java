package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.HorizontalRotation;
import net.minecraft.world.level.block.state.BlockState;

public final class LargeCornerSlopePanelWallOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Back face
        drawer.drawLine(  0,   0, 1,   1,   0, 1);
        drawer.drawLine(  0,   0, 1,   0,   1, 1);
        drawer.drawLine(  0,   1, 1, .5F,   1, 1);
        drawer.drawLine(  1,   0, 1,   1, .5F, 1);
        drawer.drawLine(.5F, .5F, 1,   1, .5F, 1);
        drawer.drawLine(.5F, .5F, 1, .5F,   1, 1);

        // Horizontal edges
        drawer.drawLine(.5F, .5F, 0, .5F, .5F, 1);
        drawer.drawLine(.5F,   1, 0, .5F,   1, 1);
        drawer.drawLine(  1, .5F, 0,   1, .5F, 1);

        // Front edges
        drawer.drawLine(.5F, .5F, 0,   1, .5F, 0);
        drawer.drawLine(.5F, .5F, 0, .5F,   1, 0);

        // Slopes
        drawer.drawLine(.5F,   1, 0, 0, 1, 1);
        drawer.drawLine(  1, .5F, 0, 1, 0, 1);
        drawer.drawLine(.5F, .5F, 0, 0, 0, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        poseStack.mulPose(SlopePanelOutlineRenderer.ROTATIONS[rotation.ordinal()]);
    }
}
