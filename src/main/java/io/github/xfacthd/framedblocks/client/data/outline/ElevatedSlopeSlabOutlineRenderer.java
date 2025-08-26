package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class ElevatedSlopeSlabOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back edges
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        //Front edges
        drawer.drawLine(0,   0, 0, 0, .5F, 0);
        drawer.drawLine(1,   0, 0, 1, .5F, 0);
        drawer.drawLine(0, .5F, 0, 1, .5F, 0);

        //Bottom face
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Top edge
        drawer.drawLine(0, 1, 1, 1, 1, 1);

        //Slope
        drawer.drawLine(0, .5F, 0, 0, 1, 1);
        drawer.drawLine(1, .5F, 0, 1, 1, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        if (state.getValue(FramedProperties.TOP))
        {
            OutlineRenderer.mirrorHorizontally(poseStack, false);
        }
    }
}
