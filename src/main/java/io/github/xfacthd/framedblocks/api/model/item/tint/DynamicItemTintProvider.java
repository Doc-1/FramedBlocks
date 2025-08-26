package io.github.xfacthd.framedblocks.api.model.item.tint;

import io.github.xfacthd.framedblocks.api.camo.CamoList;
import net.minecraft.world.item.ItemStack;

public interface DynamicItemTintProvider
{
    int getColor(ItemStack stack, CamoList camos, int tintIndex);
}
