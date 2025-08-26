package io.github.xfacthd.framedblocks.common.item.block;

import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.block.sign.FramedWallHangingSignBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class FramedHangingSignItem extends FramedSignItem
{
    public FramedHangingSignItem(Properties props)
    {
        super(FBContent.BLOCK_FRAMED_HANGING_SIGN, FBContent.BLOCK_FRAMED_WALL_HANGING_SIGN, Direction.UP, props);
    }

    @Override
    protected boolean canPlace(LevelReader level, BlockState state, BlockPos pos)
    {
        if (state.getBlock() instanceof FramedWallHangingSignBlock && !FramedWallHangingSignBlock.canPlace(state, level, pos))
        {
            return false;
        }
        return super.canPlace(level, state, pos);
    }
}
