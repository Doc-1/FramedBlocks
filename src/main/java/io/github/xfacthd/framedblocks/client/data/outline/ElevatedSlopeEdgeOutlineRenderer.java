package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import net.minecraft.world.level.block.state.BlockState;

public final class ElevatedSlopeEdgeOutlineRenderer implements SimpleOutlineRenderer
{
    public static final ElevatedSlopeEdgeOutlineRenderer INSTANCE = new ElevatedSlopeEdgeOutlineRenderer();

    private ElevatedSlopeEdgeOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Bottom face
        drawer.drawLine(0F, 0F, 0F, 1F, 0F, 0F);
        drawer.drawLine(0F, 0F, 1F, 1F, 0F, 1F);
        drawer.drawLine(0F, 0F, 0F, 0F, 0F, 1F);
        drawer.drawLine(1F, 0F, 0F, 1F, 0F, 1F);

        // Back face
        drawer.drawLine(0F, 1F, 1F, 1F, 1F, 1F);
        drawer.drawLine(0F, 0F, 1F, 0F, 1F, 1F);
        drawer.drawLine(1F, 0F, 1F, 1F, 1F, 1F);

        // Top face
        drawer.drawLine(0F, 1F, .5F, 0F, 1F, 1F);
        drawer.drawLine(1F, 1F, .5F, 1F, 1F, 1F);

        // Front face
        drawer.drawLine(0F, 0F, 0F, 0F, .5F, 0F);
        drawer.drawLine(1F, 0F, 0F, 1F, .5F, 0F);

        // Horizontal edges
        drawer.drawLine(0F, .5F,  0F, 1F, .5F,  0F);
        drawer.drawLine(0F,  1F, .5F, 1F,  1F, .5F);

        // Sloped edges
        drawer.drawLine(0F, .5F, 0F, 0F, 1F, .5F);
        drawer.drawLine(1F, .5F, 0F, 1F, 1F, .5F);
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);
        switch (state.getValue(PropertyHolder.SLOPE_TYPE))
        {
            case TOP -> poseStack.mulPose(Quaternions.ZP_180);
            case HORIZONTAL -> poseStack.mulPose(Quaternions.ZP_90);
        }
    }
}
