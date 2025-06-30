package xfacthd.framedblocks.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import xfacthd.framedblocks.api.block.AbstractFramedBlock;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.common.data.BlockType;

import java.util.function.UnaryOperator;

public abstract class FramedBlock extends AbstractFramedBlock implements IFramedBlockInternal
{
    protected FramedBlock(BlockType blockType, Properties props)
    {
        this(blockType, props, UnaryOperator.identity());
    }

    protected FramedBlock(BlockType blockType, Properties props, UnaryOperator<Properties> propertyModifier)
    {
        super(blockType, propertyModifier.apply(IFramedBlock.applyDefaultProperties(props, blockType)));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type)
    {
        if (getBlockType() != BlockType.FRAMED_CUBE)
        {
            return false;
        }
        return super.isPathfindable(state, type);
    }

    @Override
    public BlockType getBlockType()
    {
        return (BlockType) super.getBlockType();
    }
}
