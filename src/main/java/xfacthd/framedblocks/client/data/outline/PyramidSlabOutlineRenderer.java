package xfacthd.framedblocks.client.data.outline;

import net.minecraft.world.level.block.state.BlockState;

public final class PyramidSlabOutlineRenderer extends PyramidOutlineRenderer
{
    @Override
    public void drawTopPart(BlockState state, LineDrawer drawer)
    {
        // Slopes
        drawer.drawLine(0, 0, 0, .5F, .5F, .5F);
        drawer.drawLine(1, 0, 0, .5F, .5F, .5F);
        drawer.drawLine(0, 0, 1, .5F, .5F, .5F);
        drawer.drawLine(1, 0, 1, .5F, .5F, .5F);
    }
}
