package xfacthd.framedblocks.common.data.skippreds.pane;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.predicate.cull.SideSkipPredicate;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.SlopeType;
import xfacthd.framedblocks.common.data.skippreds.CullTest;
import xfacthd.framedblocks.common.data.skippreds.HalfDir;

@CullTest(BlockType.FRAMED_BOARD)
public final class BoardSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        if (side.getAxis() != dir.getAxis())
        {
            if (adjState.getBlock() == state.getBlock())
            {
                return testAgainstBoard(dir, adjState);
            }
            else if (adjState.getBlock() == FBContent.BLOCK_FRAMED_CORNER_STRIP.value())
            {
                return testAgainstCornerStrip(dir, adjState, side);
            }
        }

        return false;
    }

    @CullTest.TestTarget(BlockType.FRAMED_BOARD)
    private static boolean testAgainstBoard(Direction dir, BlockState adjState)
    {
        return dir == adjState.getValue(BlockStateProperties.FACING);
    }

    @CullTest.TestTarget(BlockType.FRAMED_CORNER_STRIP)
    private static boolean testAgainstCornerStrip(Direction dir, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        SlopeType adjType = adjState.getValue(PropertyHolder.SLOPE_TYPE);

        return getHalfDir(dir, side).isEqualTo(CornerStripSkipPredicate.getHalfDir(adjDir, adjType, side.getOpposite()));
    }



    public static HalfDir getHalfDir(Direction dir, Direction side)
    {
        if (side.getAxis() != dir.getAxis())
        {
            return HalfDir.fromDirections(side, dir);
        }
        return HalfDir.NULL;
    }
}
