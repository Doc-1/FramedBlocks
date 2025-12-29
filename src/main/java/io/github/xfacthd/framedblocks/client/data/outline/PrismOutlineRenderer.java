package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.api.render.outline.SimpleOutlineRenderer;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.DirectionAxis;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class PrismOutlineRenderer implements SimpleOutlineRenderer
{
    protected static final Quaternionf[] YN_DIR = makeQuaternionArray();

    @Override
    public void draw(BlockState state, LineDrawer drawer)
    {
        // Base edges
        drawer.drawLine(0, 0, 0, 0, 0, 1);
        drawer.drawLine(1, 0, 0, 1, 0, 1);
        drawer.drawLine(0, 0, 0, 1, 0, 0);
        drawer.drawLine(0, 0, 1, 1, 0, 1);

        // Back triangle
        drawer.drawLine(0, 0, 1, .5F, .5F, 1);
        drawer.drawLine(.5F, .5F, 1, 1, 0, 1);

        drawCenterAndTriangle(drawer);
    }

    protected void drawCenterAndTriangle(LineDrawer drawer)
    {
        // Center line
        drawer.drawLine(.5F, .5F, 0, .5F, .5F, 1);

        // Front triangle
        drawer.drawLine(0, 0, 0, .5F, .5F, 0);
        drawer.drawLine(.5F, .5F, 0, 1, 0, 0);
    }

    @Override
    public void rotateMatrix(PoseStack pstack, BlockState state)
    {
        DirectionAxis dirAxis = state.getValue(PropertyHolder.FACING_AXIS);
        Direction facing = dirAxis.direction();
        Direction.Axis axis = dirAxis.axis();

        if (Utils.isY(facing))
        {
            if (facing == Direction.DOWN)
            {
                pstack.mulPose(Quaternions.ZP_180);
            }
            if (axis == Direction.Axis.X)
            {
                pstack.mulPose(Quaternions.YP_90);
            }
        }
        else
        {
            if (facing != Direction.SOUTH)
            {
                pstack.mulPose(YN_DIR[facing.get2DDataValue()]);
            }
            if (axis != Direction.Axis.Y)
            {
                pstack.mulPose(Quaternions.ZP_90);
            }
            pstack.mulPose(Quaternions.XP_90);
        }
    }



    private static Quaternionf[] makeQuaternionArray()
    {
        Quaternionf[] array = new Quaternionf[4];
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            array[dir.get2DDataValue()] = Axis.YN.rotation(Mth.PI / 2F * dir.get2DDataValue());
        }
        return array;
    }
}
