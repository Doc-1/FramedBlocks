package io.github.xfacthd.framedblocks.common.data.conpreds.misc;

import io.github.xfacthd.framedblocks.api.predicate.contex.ConnectionPredicate;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class HopperConnectionPredicate implements ConnectionPredicate
{
    @Override
    public boolean canConnectFullEdge(BlockState state, Direction side, @Nullable Direction edge)
    {
        if (side == Direction.UP)
        {
            return edge != null;
        }
        if (side != Direction.DOWN)
        {
            return edge == Direction.UP;
        }
        return false;
    }

    @Override
    public boolean canConnectDetailed(BlockState state, Direction side, Direction edge)
    {
        if (side == Direction.DOWN)
        {
            return true;
        }
        if (side != Direction.UP)
        {
            return !Utils.isY(edge);
        }
        return false;
    }
}
