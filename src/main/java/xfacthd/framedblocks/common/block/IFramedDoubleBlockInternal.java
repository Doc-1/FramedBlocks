package xfacthd.framedblocks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.common.FBContent;

public interface IFramedDoubleBlockInternal extends IFramedDoubleBlock
{
    @Override
    default FramedDoubleBlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedDoubleBlockEntity(FBContent.BE_TYPE_FRAMED_DOUBLE_BLOCK.value(), pos, state);
    }
}
