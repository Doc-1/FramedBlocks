package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class InnerPrismCornerOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Bottom face
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        //Back face
        drawer.drawLine(1, 0, 0, 1, 1, 0);
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 1, 0, 1, 1, 1);

        //Right face
        drawer.drawLine(0, 1, 1, 1, 1, 1);
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        //Slope edges
        drawer.drawLine(1, 1, 0, 0, 1, 1);
        drawer.drawLine(0, 0, 0, 1, 1, 0);
        drawer.drawLine(0, 0, 0, 0, 1, 1);
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
