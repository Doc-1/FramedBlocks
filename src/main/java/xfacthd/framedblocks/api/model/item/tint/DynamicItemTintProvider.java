package xfacthd.framedblocks.api.model.item.tint;

import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.camo.CamoList;

public interface DynamicItemTintProvider
{
    int getColor(ItemStack stack, CamoList camos, int tintIndex);
}
