package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

public final class VerticalSlopedStairsOutlineRenderer implements OutlineRenderer
{
    public static final VerticalSlopedStairsOutlineRenderer INSTANCE = new VerticalSlopedStairsOutlineRenderer();

    private VerticalSlopedStairsOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Back face
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 0, 1, 1, 1, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);
        drawer.drawLine(0, 1, 1, 1, 1, 1);

        // Front face
        drawer.drawLine(0, 1, .5F, 1, 1, .5F);
        drawer.drawLine(1, 0, .5F, 1, 1, .5F);
        drawer.drawLine(1, 0, .5F, 0, 1, .5F);

        // Middle face
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(0, 0, 0, 0, 1, 0);
        drawer.drawLine(1, 0, 0, 0, 1, 0);

        // Horizontal side edges
        drawer.drawLine(0, 0,   0, 0, 0, 1);
        drawer.drawLine(0, 1,   0, 0, 1, 1);
        drawer.drawLine(1, 0,   0, 1, 0, 1);
        drawer.drawLine(1, 1, .5F, 1, 1, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        poseStack.mulPose(SlopePanelOutlineRenderer.ROTATIONS[rotation.ordinal()]);
    }
}
