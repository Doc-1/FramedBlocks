package xfacthd.framedblocks.common.data.conpreds.pane;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.predicate.contex.ConnectionPredicate;

public final class BoardConnectionPredicate implements ConnectionPredicate
{
    @Override
    public boolean canConnectFullEdge(BlockState state, Direction side, @Nullable Direction edge)
    {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        if (side == dir)
        {
            return true;
        }
        return side.getAxis() != dir.getAxis() && edge == dir;
    }

    @Override
    public boolean canConnectDetailed(BlockState state, Direction side, Direction edge)
    {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        if (side == dir.getOpposite())
        {
            return true;
        }
        return side.getAxis() != dir.getAxis() && edge.getAxis() != dir.getAxis();
    }
}
