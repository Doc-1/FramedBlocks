package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.api.render.outline.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.CornerType;
import net.minecraft.world.level.block.state.BlockState;

public final class InnerCornerSlopeOutlineRenderer implements SimpleOutlineRenderer
{
    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        CornerType type = state.getValue(PropertyHolder.CORNER_TYPE);
        if (!type.isHorizontal())
        {
            //Back face
            drawer.drawLine(0, 0, 1, 1, 0, 1);
            drawer.drawLine(0, 1, 1, 1, 1, 1);
            drawer.drawLine(0, 0, 1, 0, 1, 1);
            drawer.drawLine(1, 0, 1, 1, 1, 1);

            //Left face
            drawer.drawLine(1, 0, 0, 1, 0, 1);
            drawer.drawLine(1, 1, 0, 1, 1, 1);
            drawer.drawLine(1, 0, 0, 1, 1, 0);

            //Bottom face
            drawer.drawLine(0, 0, 0, 0, 0, 1);
            drawer.drawLine(0, 0, 0, 1, 0, 0);

            //Slope edges
            drawer.drawLine(0, 0, 0, 0, 1, 1);
            drawer.drawLine(0, 0, 0, 1, 1, 0);
            drawer.drawLine(0, 0, 0, 1, 1, 1);
        }
        else
        {
            //Top face
            drawer.drawLine(0, 1, 1, 1, 1, 1);
            drawer.drawLine(0, 1, 0, 0, 1, 1);
            drawer.drawLine(0, 1, 0, 1, 1, 1);

            //Bottom face
            drawer.drawLine(0, 0, 0, 1, 0, 0);
            drawer.drawLine(0, 0, 1, 1, 0, 1);
            drawer.drawLine(0, 0, 0, 0, 0, 1);
            drawer.drawLine(1, 0, 0, 1, 0, 1);

            //Right face
            drawer.drawLine(0, 0, 0, 0, 1, 0);
            drawer.drawLine(0, 0, 1, 0, 1, 1);

            //Left face
            drawer.drawLine(1, 0, 1, 1, 1, 1);
            drawer.drawLine(1, 0, 0, 1, 1, 1);

            //Slope edge
            drawer.drawLine(0, 0, 0, 1, 1, 1);
        }
    }

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        SimpleOutlineRenderer.super.rotateMatrix(poseStack, state);

        CornerType type = state.getValue(PropertyHolder.CORNER_TYPE);
        if (!type.isHorizontal())
        {
            if (type.isTop())
            {
                OutlineRenderer.mirrorHorizontally(poseStack, true);
            }
        }
        else
        {
            if (!type.isRight())
            {
                poseStack.mulPose(Quaternions.ZP_90);
            }
            if (type.isTop())
            {
                poseStack.mulPose(type.isRight() ? Quaternions.ZN_90 : Quaternions.ZP_90);
            }
        }
    }
}
