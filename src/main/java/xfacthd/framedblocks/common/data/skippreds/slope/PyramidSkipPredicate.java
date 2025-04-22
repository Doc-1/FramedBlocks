package xfacthd.framedblocks.common.data.skippreds.slope;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.predicate.cull.SideSkipPredicate;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.block.pillar.FramedLatticeBlock;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.PillarConnection;
import xfacthd.framedblocks.common.data.skippreds.CullTest;

@CullTest(BlockType.FRAMED_PYRAMID)
public final class PyramidSkipPredicate implements SideSkipPredicate
{
    @Override
    @CullTest.TestTarget(BlockType.FRAMED_PYRAMID) // Test against self does not make any sense here, so just shut up the validator
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        if (side != dir) return false;
        PillarConnection connection = state.getValue(PropertyHolder.PILLAR_CONNECTION);
        if (connection == PillarConnection.NONE) return false;

        if (adjState.getBlock() instanceof IFramedBlock block && block.getBlockType() instanceof BlockType blockType)
        {
            return switch (blockType)
            {
                case FRAMED_FENCE -> testAgainstFence(dir, connection);
                case FRAMED_WALL -> testAgainstWall(dir, connection, adjState);
                case FRAMED_PILLAR -> testAgainstPillar(dir, connection, adjState);
                case FRAMED_HALF_PILLAR -> testAgainstHalfPillar(dir, connection, adjState);
                case FRAMED_POST -> testAgainstPost(dir, connection, adjState);
                case FRAMED_LATTICE_BLOCK -> testAgainstThinLattice(dir, connection, adjState);
                case FRAMED_THICK_LATTICE -> testAgainstThickLattice(dir, connection, adjState);
                default -> false;
            };
        }
        return false;
    }

    @CullTest.TestTarget(BlockType.FRAMED_FENCE)
    private static boolean testAgainstFence(Direction dir, PillarConnection connection)
    {
        return connection == PillarConnection.POST && Utils.isY(dir);
    }

    @CullTest.TestTarget(BlockType.FRAMED_WALL)
    private static boolean testAgainstWall(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.PILLAR && Utils.isY(dir) && adjState.getValue(WallBlock.UP);
    }

    @CullTest.TestTarget(BlockType.FRAMED_PILLAR)
    private static boolean testAgainstPillar(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.PILLAR && adjState.getValue(BlockStateProperties.AXIS) == dir.getAxis();
    }

    @CullTest.TestTarget(BlockType.FRAMED_HALF_PILLAR)
    private static boolean testAgainstHalfPillar(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.PILLAR && adjState.getValue(BlockStateProperties.FACING) == dir.getOpposite();
    }

    @CullTest.TestTarget(BlockType.FRAMED_POST)
    private static boolean testAgainstPost(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.POST && adjState.getValue(BlockStateProperties.AXIS) == dir.getAxis();
    }

    @CullTest.TestTarget(BlockType.FRAMED_LATTICE_BLOCK)
    private static boolean testAgainstThinLattice(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.POST && adjState.getValue(FramedLatticeBlock.getPropFromAxis(dir));
    }

    @CullTest.TestTarget(BlockType.FRAMED_THICK_LATTICE)
    private static boolean testAgainstThickLattice(Direction dir, PillarConnection connection, BlockState adjState)
    {
        return connection == PillarConnection.PILLAR && adjState.getValue(FramedLatticeBlock.getPropFromAxis(dir));
    }
}
