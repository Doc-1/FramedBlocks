package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class PrismCornerOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back edge
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        //Bottom edges
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Front edge
        drawer.drawLine(1, 0, 0, 0, 0, 1);

        //Slope edges
        drawer.drawLine(1, 0, 0, 1, 1, 1);
        drawer.drawLine(0, 0, 1, 1, 1, 1);
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
