package io.github.xfacthd.framedblocks.common.data.conpreds.slab;

import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.api.predicate.contex.ConnectionPredicate;
import io.github.xfacthd.framedblocks.api.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class SlabConnectionPredicate implements ConnectionPredicate
{
    @Override
    public boolean canConnectFullEdge(BlockState state, Direction side, @Nullable Direction edge)
    {
        boolean top = state.getValue(FramedProperties.TOP);
        Direction fullFace = top ? Direction.UP : Direction.DOWN;
        if (!Utils.isY(side))
        {
            return edge == fullFace;
        }
        return side == fullFace;
    }

    @Override
    public boolean canConnectDetailed(BlockState state, Direction side, Direction edge)
    {
        if (!Utils.isY(side))
        {
            return !Utils.isY(edge);
        }

        Direction fullFace = state.getValue(FramedProperties.TOP) ? Direction.DOWN : Direction.UP;
        return fullFace == side;
    }
}
