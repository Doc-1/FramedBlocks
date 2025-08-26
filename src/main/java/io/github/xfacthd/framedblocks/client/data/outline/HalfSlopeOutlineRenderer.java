package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.world.level.block.state.BlockState;

public final class HalfSlopeOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back edges
        drawer.drawLine(  0, 0, 1,   0, 1, 1);
        drawer.drawLine(.5F, 0, 1, .5F, 1, 1);

        //Bottom face
        drawer.drawLine(  0, 0, 0,   0, 0, 1);
        drawer.drawLine(  0, 0, 0, .5F, 0, 0);
        drawer.drawLine(.5F, 0, 0, .5F, 0, 1);
        drawer.drawLine(  0, 0, 1, .5F, 0, 1);

        //Top edge
        drawer.drawLine(0, 1, 1, .5F, 1, 1);

        //Slope
        drawer.drawLine(  0, 0, 0,   0, 1, 1);
        drawer.drawLine(.5F, 0, 0, .5F, 1, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        boolean top = state.getValue(FramedProperties.TOP);

        if (state.getValue(PropertyHolder.RIGHT) == top)
        {
            poseStack.translate(top ? -.5 : .5, 0, 0);
        }

        if (top)
        {
            OutlineRenderer.mirrorHorizontally(poseStack, false);
        }
    }
}
