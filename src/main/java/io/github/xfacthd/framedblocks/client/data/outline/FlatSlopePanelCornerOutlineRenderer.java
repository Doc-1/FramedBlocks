package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.HorizontalRotation;
import net.minecraft.world.level.block.state.BlockState;

public final class FlatSlopePanelCornerOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Back edges
        drawer.drawLine(0, 0, .5F, 1, 0, .5F);
        drawer.drawLine(0, 1, .5F, 1, 1, .5F);
        drawer.drawLine(0, 0, .5F, 0, 1, .5F);
        drawer.drawLine(1, 0, .5F, 1, 1, .5F);

        // Bottom edge
        drawer.drawLine(0, 0, 0, 0, 0, .5F);

        // Slopes
        drawer.drawLine(0, 0, 0, 1, 0, .5F);
        drawer.drawLine(0, 0, 0, 1, 1, .5F);
        drawer.drawLine(0, 0, 0, 0, 1, .5F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        poseStack.mulPose(SlopePanelOutlineRenderer.ROTATIONS[rotation.ordinal()]);

        if (!state.getValue(PropertyHolder.FRONT))
        {
            poseStack.translate(0, 0, .5);
        }
    }
}
