package xfacthd.framedblocks.api.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import xfacthd.framedblocks.api.type.IBlockType;

public abstract class AbstractFramedDoubleBlock extends AbstractFramedBlock implements IFramedDoubleBlock
{
    public AbstractFramedDoubleBlock(IBlockType blockType, Properties props)
    {
        super(blockType, props);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FramedProperties.SOLID);
    }
}
