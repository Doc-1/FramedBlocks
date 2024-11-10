package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.render.OutlineRenderer;

public final class FlatElevatedInnerSlopeSlabCornerOutlineRenderer implements OutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Bottom face
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Edges
        drawer.drawLine(1, 0, 1, 1,   1, 1);
        drawer.drawLine(0, 0, 0, 0, .5F, 0);
        drawer.drawLine(0, 0, 1, 0,   1, 1);
        drawer.drawLine(1, 0, 0, 1,   1, 0);

        //Top edges
        drawer.drawLine(0, 1, 1, 1, 1, 1);
        drawer.drawLine(1, 1, 0, 1, 1, 1);

        //Slope
        drawer.drawLine(0, .5F, 0, 1, 1, 0);
        drawer.drawLine(0, .5F, 0, 1, 1, 1);
        drawer.drawLine(0, .5F, 0, 0, 1, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        if (state.getValue(FramedProperties.TOP))
        {
            OutlineRenderer.mirrorHorizontally(poseStack, true);
        }
    }
}
