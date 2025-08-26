package io.github.xfacthd.framedblocks.client.model.item.tintprovider;

import io.github.xfacthd.framedblocks.api.camo.CamoList;
import io.github.xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;
import io.github.xfacthd.framedblocks.client.model.geometry.cube.FramedTargetGeometry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public final class FramedTargetItemTintProvider extends FramedBlockItemTintProvider
{
    public static final FramedTargetItemTintProvider INSTANCE = new FramedTargetItemTintProvider();

    private FramedTargetItemTintProvider()
    {
        super(false);
    }

    @Override
    public int getColor(ItemStack stack, CamoList camos, int tintIndex)
    {
        if (tintIndex == FramedTargetGeometry.OVERLAY_TINT_IDX)
        {
            return DyeColor.RED.getTextColor();
        }
        return super.getColor(stack, camos, tintIndex);
    }
}
