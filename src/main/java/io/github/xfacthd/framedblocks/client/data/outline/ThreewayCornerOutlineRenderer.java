package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class ThreewayCornerOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back edges
        drawer.drawLine(1, 0, 1, 1, 1, 1);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Slope edges
        drawer.drawLine(1, 0, 0, 1, 1, 1);
        drawer.drawLine(1, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 1, 1);

        //Cross
        drawer.drawLine(  1,   0,   0, .5F, .5F, .5F);
        drawer.drawLine(.5F, .5F, .5F,   1,   1,   1);
        drawer.drawLine(  0,   0,   1, .5F, .5F, .5F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        if (state.getValue(FramedProperties.TOP))
        {
            OutlineRenderer.mirrorHorizontally(poseStack, true);
        }
    }
}
