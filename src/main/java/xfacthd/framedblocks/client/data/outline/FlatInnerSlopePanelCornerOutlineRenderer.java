package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

public final class FlatInnerSlopePanelCornerOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Back edges
        drawer.drawLine(0, 0, .5F, 1, 0, .5F);
        drawer.drawLine(0, 1, .5F, 1, 1, .5F);
        drawer.drawLine(0, 0, .5F, 0, 1, .5F);
        drawer.drawLine(1, 0, .5F, 1, 1, .5F);

        // Side edges
        drawer.drawLine(0, 0, 0, 0, 0, .5F);
        drawer.drawLine(1, 0, 0, 1, 0, .5F);
        drawer.drawLine(0, 1, 0, 0, 1, .5F);

        //Front edges
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(0, 0, 0, 0, 1, 0);

        // Slopes
        drawer.drawLine(0, 1, 0, 1, 1, .5F);
        drawer.drawLine(0, 0, 0, 1, 1, .5F);
        drawer.drawLine(1, 0, 0, 1, 1, .5F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        poseStack.mulPose(SlopePanelOutlineRenderer.ROTATIONS[rotation.ordinal()]);

        if (!state.getValue(PropertyHolder.FRONT))
        {
            poseStack.translate(0, 0, .5);
        }
    }
}
