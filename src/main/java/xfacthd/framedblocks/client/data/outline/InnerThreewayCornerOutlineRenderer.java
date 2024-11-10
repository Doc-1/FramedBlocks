package xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.render.OutlineRenderer;

public final class InnerThreewayCornerOutlineRenderer implements OutlineRenderer
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
        drawer.drawLine(0, 1, 1, 1, 1, 1);
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        //Left face
        drawer.drawLine(1, 1, 0, 1, 1, 1);
        drawer.drawLine(1, 0, 0, 1, 1, 0);

        //Slope edges
        drawer.drawLine(0, 0, 0, 0, 1, 1);
        drawer.drawLine(0, 0, 0, 1, 1, 0);
        drawer.drawLine(0, 1, 1, 1, 1, 0);

        //Cross
        drawer.drawLine(  0,   0,   0, .5F, .5F, .5F);
        drawer.drawLine(.5F, .5F, .5F,   0,   1,   1);
        drawer.drawLine(  1,   1,   0, .5F, .5F, .5F);
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
