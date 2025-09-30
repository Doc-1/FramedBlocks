package io.github.xfacthd.framedblocks.client.data.outline;

import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class InverseDoubleSlopeSlabOutlineRenderer implements SimpleOutlineRenderer
{
    public static final InverseDoubleSlopeSlabOutlineRenderer INSTANCE = new InverseDoubleSlopeSlabOutlineRenderer();

    private InverseDoubleSlopeSlabOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        //Back vertical edges
        drawer.drawLine(0, .5F, 1, 0, 1, 1);
        drawer.drawLine(1, .5F, 1, 1, 1, 1);

        //Center horizontal edges
        drawer.drawLine(0, .5F, 0, 1, .5F, 0);
        drawer.drawLine(0, .5F, 1, 1, .5F, 1);

        //Top edge
        drawer.drawLine(0, 1, 1, 1, 1, 1);

        //Top slope
        drawer.drawLine(0, .5F, 0, 0, 1, 1);
        drawer.drawLine(1, .5F, 0, 1, 1, 1);

        //Bottom edge
        drawer.drawLine(0, 0, 0, 1, 0, 0);

        //Bottom slope
        drawer.drawLine(0, 0, 0, 0, .5F, 1);
        drawer.drawLine(1, 0, 0, 1, .5F, 1);

        //Front vertical edges
        drawer.drawLine(0, 0, 0, 0, .5F, 0);
        drawer.drawLine(1, 0, 0, 1, .5F, 0);
    }
}
