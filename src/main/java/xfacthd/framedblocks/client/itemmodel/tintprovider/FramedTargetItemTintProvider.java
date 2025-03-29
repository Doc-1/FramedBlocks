package xfacthd.framedblocks.client.itemmodel.tintprovider;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.client.model.geometry.cube.FramedTargetGeometry;

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
