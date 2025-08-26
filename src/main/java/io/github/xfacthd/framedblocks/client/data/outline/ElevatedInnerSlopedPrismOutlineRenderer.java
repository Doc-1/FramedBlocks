package io.github.xfacthd.framedblocks.client.data.outline;

import net.minecraft.world.level.block.state.BlockState;

public final class ElevatedInnerSlopedPrismOutlineRenderer extends SlopedPrismOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Base edges
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        // Top edges
        drawer.drawLine(0, 1, 0, 0, 1, 1);
        drawer.drawLine(1, 1, 0, 1, 1, 1);
        drawer.drawLine(0, 1, 0, 1, 1, 0);

        // Vertical edges
        drawer.drawLine(0, 0, 0, 0, 1, 0);
        drawer.drawLine(1, 0, 0, 1, 1, 0);
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        // Back triangle
        drawer.drawLine(0, 1, 1, .5F, .5F, 1);
        drawer.drawLine(.5F, .5F, 1, 1, 1, 1);

        // Center line
        drawer.drawLine(.5F, .5F, .5F, .5F, .5F, 1);

        // Front sloped triangle
        drawer.drawLine(0, 1, 0, .5F, .5F, .5F);
        drawer.drawLine(.5F, .5F, .5F, 1, 1, 0);
    }
}
