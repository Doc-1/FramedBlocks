package xfacthd.framedblocks.api.model.item;

import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.util.CamoList;

public interface DynamicItemTintProvider
{
    int getColor(ItemStack stack, CamoList camos, int tintIndex);
}
