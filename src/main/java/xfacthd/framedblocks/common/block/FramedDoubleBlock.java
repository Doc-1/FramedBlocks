package xfacthd.framedblocks.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import xfacthd.framedblocks.api.block.AbstractFramedDoubleBlock;
import xfacthd.framedblocks.common.data.BlockType;

public abstract class FramedDoubleBlock extends AbstractFramedDoubleBlock implements IFramedDoubleBlockInternal
{
    public FramedDoubleBlock(BlockType blockType, Properties props)
    {
        super(blockType, props);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType)
    {
        return false;
    }

    @Override
    public BlockType getBlockType()
    {
        return (BlockType) super.getBlockType();
    }
}
