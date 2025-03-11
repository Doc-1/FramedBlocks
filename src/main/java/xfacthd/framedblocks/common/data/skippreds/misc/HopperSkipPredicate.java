package xfacthd.framedblocks.common.data.skippreds.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.predicate.cull.SideSkipPredicate;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.skippreds.CullTest;

@CullTest(BlockType.FRAMED_HOPPER)
public final class HopperSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        if (side != Direction.DOWN && adjState.getBlock() instanceof IFramedBlock block && block.getBlockType() instanceof BlockType type)
        {
            return switch (type)
            {
                case FRAMED_HOPPER -> testAgainstHopper(side);
                case FRAMED_TUBE -> testAgainstTube(adjState, side);
                case FRAMED_CORNER_TUBE -> testAgainstCornerTube(adjState, side);
                default -> false;
            };
        }
        return false;
    }

    @CullTest.TestTarget(BlockType.FRAMED_HOPPER)
    private static boolean testAgainstHopper(Direction side)
    {
        return !Utils.isY(side);
    }

    @CullTest.TestTarget(BlockType.FRAMED_TUBE)
    private static boolean testAgainstTube(BlockState adjState, Direction side)
    {
        return side == Direction.UP && !adjState.getValue(PropertyHolder.THICK) && adjState.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y;
    }

    @CullTest.TestTarget(BlockType.FRAMED_CORNER_TUBE)
    private static boolean testAgainstCornerTube(BlockState adjState, Direction side)
    {
        return side == Direction.UP && !adjState.getValue(PropertyHolder.THICK) && adjState.getValue(PropertyHolder.CORNER_TYPE_ORIENTATION).isSideOpen(Direction.DOWN);
    }
}
