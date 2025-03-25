package xfacthd.framedblocks.api.block.item;

import com.google.common.base.Preconditions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.api.util.Utils;

import java.util.Optional;
import java.util.function.Consumer;

public class FramedBlockItem extends BlockItem
{
    private final IFramedBlock block;

    public FramedBlockItem(Block block, Properties props)
    {
        super(block, props);
        Preconditions.checkArgument(block instanceof IFramedBlock);
        this.block = (IFramedBlock) block;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> appender, TooltipFlag flag)
    {
        appendCamoHoverText(block, stack, appender);
    }

    public static void appendCamoHoverText(IFramedBlock block, ItemStack stack, Consumer<Component> appender)
    {
        Optional<MutableComponent> camoText = block.printCamoData(stack.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY), false);
        if (camoText.isPresent())
        {
            appender.accept(Component.translatable(IFramedBlock.CAMO_LABEL, camoText.get()).withStyle(ChatFormatting.GOLD));
        }
    }
}
