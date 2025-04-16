package xfacthd.framedblocks.common.data.conpreds.pane;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.predicate.contex.ConnectionPredicate;
import xfacthd.framedblocks.common.block.pane.FramedBoardBlock;

public final class BoardConnectionPredicate implements ConnectionPredicate
{
    @Override
    public boolean canConnectFullEdge(BlockState state, Direction side, @Nullable Direction edge)
    {
        if (FramedBoardBlock.isFacePresent(state, side))
        {
            return true;
        }
        return edge != null && FramedBoardBlock.isFacePresent(state, edge);
    }

    @Override
    public boolean canConnectDetailed(BlockState state, Direction side, Direction edge)
    {
        if (FramedBoardBlock.isFacePresent(state, side))
        {
            return false;
        }
        return !FramedBoardBlock.isFacePresent(state, edge);
    }
}
