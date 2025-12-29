package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class SmallInnerCornerSlopePanelOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Base
        drawer.drawLine(.5F, 0, .5F,   1, 0, .5F);
        drawer.drawLine(.5F, 0, .5F, .5F, 0,   1);
        drawer.drawLine(.5F, 0,   1,   1, 0,   1);
        drawer.drawLine(  1, 0, .5F,   1, 0,   1);

        // Back vertical
        drawer.drawLine(.5F, 0, .5F, .5F, 1, .5F);
        drawer.drawLine(  1, 0, .5F,   1, 1, .5F);
        drawer.drawLine(.5F, 0,   1, .5F, 1,   1);

        // Top
        drawer.drawLine(.5F, 1, .5F,   1, 1, .5F);
        drawer.drawLine(.5F, 1, .5F, .5F, 1,   1);

        // Slopes
        drawer.drawLine(1, 0, 1, .5F, 1,   1);
        drawer.drawLine(1, 0, 1,   1, 1, .5F);
        drawer.drawLine(1, 0, 1, .5F, 1, .5F);
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
