package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.common.block.ISlopeBlock;
import io.github.xfacthd.framedblocks.common.data.property.SlopeType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class SlopeOutlineRenderer implements OutlineRenderer
{
    public static final SlopeOutlineRenderer INSTANCE = new SlopeOutlineRenderer();

    private SlopeOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        SlopeType type = ((ISlopeBlock) state.getBlock()).getSlopeType(state);

        if (type != SlopeType.HORIZONTAL)
        {
            //Back edges
            drawer.drawLine(0, 0, 1, 0, 1, 1);
            drawer.drawLine(1, 0, 1, 1, 1, 1);

            //Bottom face
            drawer.drawLine(0, 0, 0, 0, 0, 1);
            drawer.drawLine(0, 0, 0, 1, 0, 0);
            drawer.drawLine(1, 0, 0, 1, 0, 1);
            drawer.drawLine(0, 0, 1, 1, 0, 1);

            //Top edge
            drawer.drawLine(0, 1, 1, 1, 1, 1);

            //Slope
            drawer.drawLine(0, 0, 0, 0, 1, 1);
            drawer.drawLine(1, 0, 0, 1, 1, 1);
        }
        else
        {
            //Back
            drawer.drawLine(0, 0, 1, 1, 0, 1);
            drawer.drawLine(0, 1, 1, 1, 1, 1);
            drawer.drawLine(0, 0, 1, 0, 1, 1);
            drawer.drawLine(1, 0, 1, 1, 1, 1);

            //Left side
            drawer.drawLine(1, 0, 0, 1, 0, 1);
            drawer.drawLine(1, 1, 0, 1, 1, 1);
            drawer.drawLine(1, 0, 0, 1, 1, 0);

            //Slope
            drawer.drawLine(1, 0, 0, 0, 0, 1);
            drawer.drawLine(1, 1, 0, 0, 1, 1);
        }
    }

    @Override
    public Direction getRotationDir(BlockState state)
    {
        return ((ISlopeBlock) state.getBlock()).getFacing(state);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        OutlineRenderer.super.rotateMatrix(poseStack, state);

        if (((ISlopeBlock) state.getBlock()).getSlopeType(state) == SlopeType.TOP)
        {
            OutlineRenderer.mirrorHorizontally(poseStack, false);
        }
    }
}
