package xfacthd.framedblocks.api.block.item;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.camo.CamoPrinter;
import xfacthd.framedblocks.api.util.Utils;

import java.util.function.Consumer;

public class FramedBlockItem extends BlockItem
{
    public FramedBlockItem(Block block, Properties props)
    {
        super(block, props);
        Preconditions.checkArgument(block instanceof IFramedBlock);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> appender, TooltipFlag flag)
    {
        appendCamoHoverText(stack, appender);
    }

    public static void appendCamoHoverText(ItemStack stack, Consumer<Component> appender)
    {
        CamoPrinter.printCamoList(appender, stack.get(Utils.DC_TYPE_CAMO_LIST), false);
    }
}
