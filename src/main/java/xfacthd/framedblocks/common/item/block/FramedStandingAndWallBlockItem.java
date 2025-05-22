package xfacthd.framedblocks.common.item.block;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.api.block.item.FramedBlockItem;

import java.util.function.Consumer;

public class FramedStandingAndWallBlockItem extends StandingAndWallBlockItem
{
    public FramedStandingAndWallBlockItem(Block block, Block wallBlock, Direction attachDir, Properties properties)
    {
        super(block, wallBlock, attachDir, properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> appender, TooltipFlag flag)
    {
        FramedBlockItem.appendCamoHoverText(stack, appender);
    }
}
