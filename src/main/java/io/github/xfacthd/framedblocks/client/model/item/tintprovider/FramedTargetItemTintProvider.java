package io.github.xfacthd.framedblocks.client.model.item.tintprovider;

import io.github.xfacthd.framedblocks.api.camo.CamoList;
import io.github.xfacthd.framedblocks.api.model.item.tint.FramedBlockItemTintProvider;
import io.github.xfacthd.framedblocks.client.model.geometry.cube.FramedTargetGeometry;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.data.component.TargetColor;
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
            TargetColor targetColor = stack.get(FBContent.DC_TYPE_TARGET_COLOR);
            DyeColor color = targetColor != null ? targetColor.color() : DyeColor.RED;
            return color.getTextColor();
        }
        return super.getColor(stack, camos, tintIndex);
    }
}
