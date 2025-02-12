package xfacthd.framedblocks.common.block.slopepanelcorner;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.common.data.BlockType;

public class FramedSmallDoubleCornerSlopePanelWallBlock extends FramedDoubleCornerSlopePanelWallBlock
{
    public FramedSmallDoubleCornerSlopePanelWallBlock(BlockType blockType, Properties props)
    {
        super(blockType, props);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        BlockUtils.removeProperty(builder, FramedProperties.SOLID);
    }
}
