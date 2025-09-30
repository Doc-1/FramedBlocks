package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class VerticalHalfSlopeOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back
        drawer.drawLine(0,   0, 1, 1,   0, 1);
        drawer.drawLine(0, .5F, 1, 1, .5F, 1);
        drawer.drawLine(0,   0, 1, 0, .5F, 1);
        drawer.drawLine(1,   0, 1, 1, .5F, 1);

        //Left side
        drawer.drawLine(1,   0, 0, 1,   0, 1);
        drawer.drawLine(1, .5F, 0, 1, .5F, 1);
        drawer.drawLine(1,   0, 0, 1, .5F, 0);

        //Slope
        drawer.drawLine(1,   0, 0, 0,   0, 1);
        drawer.drawLine(1, .5F, 0, 0, .5F, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        if (state.getValue(FramedProperties.TOP))
        {
            poseStack.translate(0, .5, 0);
        }
    }
}
