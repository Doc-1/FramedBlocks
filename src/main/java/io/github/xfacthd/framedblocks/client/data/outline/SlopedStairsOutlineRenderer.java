package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.SimpleOutlineRenderer;
import net.minecraft.world.level.block.state.BlockState;

public final class SlopedStairsOutlineRenderer implements SimpleOutlineRenderer
{
    public static final SlopedStairsOutlineRenderer INSTANCE = new SlopedStairsOutlineRenderer();

    private SlopedStairsOutlineRenderer() { }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Bottom face
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        // Center face
        drawer.drawLine(0, .5F, 0, 0, .5F, 1);
        drawer.drawLine(0, .5F, 0, 1, .5F, 0);

        // Top face
        drawer.drawLine(1, 1, 0, 1, 1, 1);
        drawer.drawLine(0, 1, 1, 1, 1, 1);

        // Vertical edges
        drawer.drawLine(0, 0, 1, 0, 1, 1);
        drawer.drawLine(1, 0, 0, 1, 1, 0);
        drawer.drawLine(1, 0, 1, 1, 1, 1);

        // Front vertical edge
        drawer.drawLine(0, 0, 0, 0, .5F, 0);

        // Top diagonal edge
        drawer.drawLine(0, 1, 1, 1, 1, 0);

        // Center diagonal edge
        drawer.drawLine(0, .5F, 1, 1, .5F, 0);
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
