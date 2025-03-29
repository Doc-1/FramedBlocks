package xfacthd.framedblocks.api.model.item.tint;

import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.camo.CamoList;
import xfacthd.framedblocks.api.util.ConfigView;

public class FramedBlockItemTintProvider implements DynamicItemTintProvider
{
    public static final FramedBlockItemTintProvider INSTANCE_SINGLE = new FramedBlockItemTintProvider(false);
    public static final FramedBlockItemTintProvider INSTANCE_DOUBLE = new FramedBlockItemTintProvider(true);

    private final boolean doubleBlock;

    protected FramedBlockItemTintProvider(boolean doubleBlock)
    {
        this.doubleBlock = doubleBlock;
    }

    @Override
    public int getColor(ItemStack stack, CamoList camos, int tintIndex)
    {
        if (!ConfigView.Client.INSTANCE.shouldRenderItemModelsWithCamo()) return -1;

        if (tintIndex < -1 && doubleBlock)
        {
            tintIndex = ModelUtils.decodeSecondaryTintIndex(tintIndex);
            return ARGB.opaque(camos.getCamo(1).getTintColor(stack, tintIndex));
        }
        else if (tintIndex >= 0)
        {
            return ARGB.opaque(camos.getCamo(0).getTintColor(stack, tintIndex));
        }
        return -1;
    }



    public static FramedBlockItemTintProvider of(IFramedBlock block)
    {
        return block.getBlockType().isDoubleBlock() ? INSTANCE_DOUBLE : INSTANCE_SINGLE;
    }
}
