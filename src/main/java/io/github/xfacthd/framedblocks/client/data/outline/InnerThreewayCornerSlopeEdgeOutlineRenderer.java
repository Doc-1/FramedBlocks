package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class InnerThreewayCornerSlopeEdgeOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        if (state.getValue(PropertyHolder.ALT_TYPE))
        {
            //Back face
            drawer.drawLine(  0, .5F, .5F, .5F, .5F, .5F);
            drawer.drawLine(  0,   1, .5F, .5F,   1, .5F);
            drawer.drawLine(  0, .5F, .5F,   0,   1, .5F);
            drawer.drawLine(.5F, .5F, .5F, .5F,   1, .5F);

            //Left face
            drawer.drawLine(.5F, .5F, 0, .5F, .5F, .5F);
            drawer.drawLine(.5F,   1, 0, .5F,   1, .5F);
            drawer.drawLine(.5F, .5F, 0, .5F,   1,   0);

            //Bottom face
            drawer.drawLine(0, .5F, 0,   0, .5F, .5F);
            drawer.drawLine(0, .5F, 0, .5F, .5F,   0);

            //Slope edges
            drawer.drawLine(   0,  .5F,    0,    0,    1,  .5F);
            drawer.drawLine(   0,  .5F,    0,  .5F,    1,    0);
            drawer.drawLine( .5F,    1,    0,    0,    1,  .5F);
            drawer.drawLine(   0,  .5F,    0, .25F, .75F, .25F);
            drawer.drawLine(.25F, .75F, .25F,    0,    1,  .5F);
            drawer.drawLine(.25F, .75F, .25F,  .5F,    1,    0);
        }
        else
        {
            //Back face
            drawer.drawLine(  0,   0,   1,   1,   0,   1);
            drawer.drawLine(  0, .5F,   1, .5F, .5F,   1);
            drawer.drawLine(  0,   0,   1,   0, .5F,   1);
            drawer.drawLine(  1,   0,   1,   1,   1,   1);
            drawer.drawLine(.5F, .5F,   1, .5F,   1,   1);
            drawer.drawLine(.5F,   1,   1,   1,   1,   1);

            //Left face
            drawer.drawLine(1,   0,   0, 1,   0,   1);
            drawer.drawLine(1, .5F,   0, 1, .5F, .5F);
            drawer.drawLine(1,   0,   0, 1, .5F,   0);
            drawer.drawLine(1, .5F, .5F, 1,   1, .5F);
            drawer.drawLine(1,   1, .5F, 1,   1,   1);

            //Bottom face
            drawer.drawLine(  0, 0, .5F,   0, 0,   1);
            drawer.drawLine(.5F, 0,   0,   1, 0,   0);
            drawer.drawLine(  0, 0, .5F, .5F, 0, .5F);
            drawer.drawLine(.5F, 0,   0, .5F, 0, .5F);

            //Slope edges
            drawer.drawLine(   0,    0,  .5F,    0,  .5F,    1);
            drawer.drawLine( .5F,    0,    0,    1,  .5F,    0);
            drawer.drawLine( .5F,    0,  .5F, .75F, .25F, .75F);
            drawer.drawLine(.75F, .25F, .75F,  .5F,  .5F,    1);
            drawer.drawLine(.75F, .25F, .75F,    1,  .5F,  .5F);
            drawer.drawLine(   1,    1,  .5F,  .5F,    1,    1);
        }
    }

    @Override
    public Direction getRotationDir(BlockState state)
    {
        Direction dir = SimpleOutlineRenderer.super.getRotationDir(state);
        if (state.getValue(PropertyHolder.RIGHT))
        {
            dir = dir.getClockWise();
        }
        return dir;
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
