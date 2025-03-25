package xfacthd.framedblocks.common.item.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.item.FramedBlockItem;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Map;

public class VerticalAndWallBlockItem extends FramedBlockItem
{
    private final Block wallBlock;

    public VerticalAndWallBlockItem(Block verticalBlock, Block wallBlock, Item.Properties props)
    {
        super(verticalBlock, props);
        this.wallBlock = wallBlock;
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context)
    {
        if (Utils.isY(context.getClickedFace()))
        {
            return getBlock().getStateForPlacement(context);
        }
        else
        {
            return wallBlock.getStateForPlacement(context);
        }
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockToItemMap, Item item)
    {
        super.registerBlocks(blockToItemMap, item);
        blockToItemMap.put(wallBlock, item);
    }
}
