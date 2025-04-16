package xfacthd.framedblocks.common.data.facepreds.misc;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.predicate.fullface.FullFacePredicate;
import xfacthd.framedblocks.common.block.pane.FramedBoardBlock;

public final class BoardFullFacePredicate implements FullFacePredicate
{
    @Override
    public boolean test(BlockState state, Direction side)
    {
        return FramedBoardBlock.isFacePresent(state, side);
    }
}
