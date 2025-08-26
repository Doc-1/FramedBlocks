package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.xfacthd.framedblocks.api.render.OutlineRenderer;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleBlockEntity;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.NullableDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import org.joml.Quaternionf;

public final class CollapsibleBlockOutlineRenderer implements OutlineRenderer
{
    private static final Quaternionf ROTATION = Axis.YN.rotationDegrees(180);

    @Override
    public void rotateMatrix(PoseStack poseStack, BlockState state)
    {
        poseStack.mulPose(ROTATION);

        NullableDirection face = state.getValue(PropertyHolder.NULLABLE_FACE);
        if (face != NullableDirection.NONE)
        {
            Direction faceDir = face.toDirection().getOpposite();

            if (faceDir == Direction.UP)
            {
                poseStack.mulPose(Quaternions.XP_180);
            }
            else if (faceDir != Direction.DOWN)
            {
                poseStack.mulPose(OutlineRenderer.YN_DIR[faceDir.getOpposite().get2DDataValue()]);
                poseStack.mulPose(Quaternions.XN_90);
            }
        }
    }

    @Override
    public void draw(BlockState state, Level level, BlockPos pos, LineDrawer drawer)
    {
        NullableDirection face = state.getValue(PropertyHolder.NULLABLE_FACE);
        if (face == NullableDirection.NONE)
        {
            Shapes.block().forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) ->
                    drawer.drawLine((float) pMinX, (float) pMinY, (float) pMinZ, (float) pMaxX, (float) pMaxY, (float) pMaxZ)
            );
        }
        else
        {
            if (!(level.getBlockEntity(pos) instanceof FramedCollapsibleBlockEntity be))
            {
                return;
            }

            float v0 = 1F - (be.getVertexOffset(0) / 16F);
            float v1 = 1F - (be.getVertexOffset(1) / 16F);
            float v2 = 1F - (be.getVertexOffset(2) / 16F);
            float v3 = 1F - (be.getVertexOffset(3) / 16F);

            //Top
            drawer.drawLine(0, v2, 0, 0, v3, 1);
            drawer.drawLine(0, v2, 0, 1, v1, 0);
            drawer.drawLine(1, v1, 0, 1, v0, 1);
            drawer.drawLine(0, v3, 1, 1, v0, 1);

            //Bottom
            drawer.drawLine(0, 0, 0, 0, 0, 1);
            drawer.drawLine(0, 0, 0, 1, 0, 0);
            drawer.drawLine(1, 0, 0, 1, 0, 1);
            drawer.drawLine(0, 0, 1, 1, 0, 1);

            //Vertical
            drawer.drawLine(1, 0, 1, 1, v0, 1);
            drawer.drawLine(1, 0, 0, 1, v1, 0);
            drawer.drawLine(0, 0, 0, 0, v2, 0);
            drawer.drawLine(0, 0, 1, 0, v3, 1);
        }
    }

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        throw new UnsupportedOperationException();
    }
}