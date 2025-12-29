package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.world.level.block.state.BlockState;

public final class FlatInnerSlopeSlabCornerOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Bottom face
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Back edges
        drawer.drawLine(1, 0, 1, 1, .5F, 1);
        drawer.drawLine(0, 0, 1, 0, .5F, 1);
        drawer.drawLine(1, 0, 0, 1, .5F, 0);

        //Top edges
        drawer.drawLine(0, .5F, 1, 1, .5F, 1);
        drawer.drawLine(1, .5F, 0, 1, .5F, 1);

        //Slope
        drawer.drawLine(0, 0, 0, 1, .5F, 0);
        drawer.drawLine(0, 0, 0, 1, .5F, 1);
        drawer.drawLine(0, 0, 0, 0, .5F, 1);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        boolean top = state.getValue(FramedProperties.TOP);
        boolean topHalf = state.getValue(PropertyHolder.TOP_HALF);
        if (top)
        {
            OutlineRenderer.mirrorHorizontally(poseStack, true);
        }
        if (topHalf != top)
        {
            poseStack.translate(0, .5, 0);
        }
    }
}
