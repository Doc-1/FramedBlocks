package io.github.xfacthd.framedblocks.common.block.slopepanelcorner;

import io.github.xfacthd.framedblocks.api.block.BlockUtils;
import io.github.xfacthd.framedblocks.api.block.FramedProperties;
import io.github.xfacthd.framedblocks.common.data.BlockType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

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
