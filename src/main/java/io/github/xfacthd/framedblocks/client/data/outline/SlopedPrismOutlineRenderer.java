package io.github.xfacthd.framedblocks.client.data.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.xfacthd.framedblocks.api.render.Quaternions;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.common.data.PropertyHolder;
import io.github.xfacthd.framedblocks.common.data.property.CompoundDirection;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

public class SlopedPrismOutlineRenderer extends PrismOutlineRenderer
{
    private static final Quaternionf[][] ZP_DIR = makeQuaternionArray();

    @Override
    public void drawCenterAndTriangle(LineDrawer drawer)
    {
        // Center line
        drawer.drawLine(.5F, .5F, .5F, .5F, .5F, 1);

        // Front sloped triangle
        drawer.drawLine(0, 0, 0, .5F, .5F, .5F);
        drawer.drawLine(.5F, .5F, .5F, 1, 0, 0);
    }

    @Override
    public void rotateMatrix(PoseStack pstack, BlockState state)
    {
        CompoundDirection cmpDir = state.getValue(PropertyHolder.FACING_DIR);
        Direction facing = cmpDir.direction();
        Direction orientation = cmpDir.orientation();

        if (Utils.isY(facing))
        {
            if (orientation != Direction.SOUTH)
            {
                pstack.mulPose(YN_DIR[orientation.get2DDataValue()]);
            }
            if (facing == Direction.DOWN)
            {
                pstack.mulPose(Quaternions.ZP_180);
            }
        }
        else
        {
            if (facing != Direction.SOUTH)
            {
                pstack.mulPose(YN_DIR[facing.get2DDataValue()]);
            }
            if (orientation != Direction.DOWN)
            {
                pstack.mulPose(ZP_DIR[facing.get2DDataValue()][orientation.ordinal()]);
            }
            pstack.mulPose(Quaternions.XP_90);
        }
    }



    private static Quaternionf[][] makeQuaternionArray()
    {
        Quaternionf[][] array = new Quaternionf[4][6];
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            array[dir.get2DDataValue()] = new Quaternionf[6];
            for (Direction orientation : Direction.values())
            {
                int mult = 2;
                if (orientation == dir.getCounterClockWise())
                {
                    mult = 1;
                }
                else if (orientation == dir.getClockWise())
                {
                    mult = 3;
                }
                array[dir.get2DDataValue()][orientation.ordinal()] = Axis.ZP.rotation(Mth.PI / 2F * mult);
            }
        }
        return array;
    }
}
