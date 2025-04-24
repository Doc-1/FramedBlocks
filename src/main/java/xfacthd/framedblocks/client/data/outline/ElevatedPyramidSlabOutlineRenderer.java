package xfacthd.framedblocks.client.data.outline;

import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.common.data.PropertyHolder;

public final class ElevatedPyramidSlabOutlineRenderer extends PyramidOutlineRenderer
{
    @Override
    public void drawTopPart(BlockState state, LineDrawer drawer)
    {
        // Upper "base" edges
        drawer.drawLine(0, .5F, 0, 1, .5F, 0);
        drawer.drawLine(0, .5F, 1, 1, .5F, 1);
        drawer.drawLine(0, .5F, 0, 0, .5F, 1);
        drawer.drawLine(1, .5F, 0, 1, .5F, 1);

        // Lower vertical edges
        drawer.drawLine(0, 0, 0, 0, .5F, 0);
        drawer.drawLine(1, 0, 0, 1, .5F, 0);
        drawer.drawLine(0, 0, 1, 0, .5F, 1);
        drawer.drawLine(0, 0, 1, 0, .5F, 1);

        switch (state.getValue(PropertyHolder.PILLAR_CONNECTION))
        {
            case PILLAR ->
            {
                // Slopes
                drawer.drawLine(0, .5F, 0, .25F, .75F, .25F);
                drawer.drawLine(1, .5F, 0, .75F, .75F, .25F);
                drawer.drawLine(0, .5F, 1, .25F, .75F, .75F);
                drawer.drawLine(1, .5F, 1, .75F, .75F, .75F);

                // Upper vertical edges
                drawer.drawLine(.25F, .75F, .25F, .25F, 1, .25F);
                drawer.drawLine(.75F, .75F, .25F, .75F, 1, .25F);
                drawer.drawLine(.25F, .75F, .75F, .25F, 1, .75F);
                drawer.drawLine(.75F, .75F, .75F, .75F, 1, .75F);

                // Lower ring
                drawer.drawLine(.25F, .75F, .25F, .25F, .75F, .75F);
                drawer.drawLine(.75F, .75F, .25F, .75F, .75F, .75F);
                drawer.drawLine(.25F, .75F, .25F, .75F, .75F, .25F);
                drawer.drawLine(.25F, .75F, .75F, .75F, .75F, .75F);

                // Upper ring
                drawer.drawLine(.25F, 1, .25F, .25F, 1, .75F);
                drawer.drawLine(.75F, 1, .25F, .75F, 1, .75F);
                drawer.drawLine(.25F, 1, .25F, .75F, 1, .25F);
                drawer.drawLine(.25F, 1, .75F, .75F, 1, .75F);
            }
            case POST ->
            {
                // Slopes
                drawer.drawLine(0, .5F, 0, .375F, .875F, .375F);
                drawer.drawLine(1, .5F, 0, .625F, .875F, .375F);
                drawer.drawLine(0, .5F, 1, .375F, .875F, .625F);
                drawer.drawLine(1, .5F, 1, .625F, .875F, .625F);

                // Upper vertical edges
                drawer.drawLine(.375F, .875F, .375F, .375F, 1, .375F);
                drawer.drawLine(.625F, .875F, .375F, .625F, 1, .375F);
                drawer.drawLine(.375F, .875F, .625F, .375F, 1, .625F);
                drawer.drawLine(.625F, .875F, .625F, .625F, 1, .625F);

                // Lower ring
                drawer.drawLine(.375F, .875F, .375F, .375F, .875F, .625F);
                drawer.drawLine(.625F, .875F, .375F, .625F, .875F, .625F);
                drawer.drawLine(.375F, .875F, .375F, .625F, .875F, .375F);
                drawer.drawLine(.375F, .875F, .625F, .625F, .875F, .625F);

                // Upper ring
                drawer.drawLine(.375F, 1, .375F, .375F, 1, .625F);
                drawer.drawLine(.625F, 1, .375F, .625F, 1, .625F);
                drawer.drawLine(.375F, 1, .375F, .625F, 1, .375F);
                drawer.drawLine(.375F, 1, .625F, .625F, 1, .625F);
            }
            case NONE ->
            {
                // Slopes
                drawer.drawLine(0, .5F, 0, .5F, 1, .5F);
                drawer.drawLine(1, .5F, 0, .5F, 1, .5F);
                drawer.drawLine(0, .5F, 1, .5F, 1, .5F);
                drawer.drawLine(1, .5F, 1, .5F, 1, .5F);
            }
        }
    }
}
