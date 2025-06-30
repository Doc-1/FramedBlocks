package xfacthd.framedblocks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.common.FBContent;

public interface IFramedBlockInternal extends IFramedBlock
{
    @Override
    default FramedBlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedBlockEntity(FBContent.BE_TYPE_FRAMED_BLOCK.value(), pos, state);
    }
}
