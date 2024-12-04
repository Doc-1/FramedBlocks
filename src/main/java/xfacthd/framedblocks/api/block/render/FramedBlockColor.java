package xfacthd.framedblocks.api.block.render;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.block.blockentity.IFramedDoubleBlockEntity;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.util.CamoList;
import xfacthd.framedblocks.api.util.ConfigView;
import xfacthd.framedblocks.api.util.Utils;

public class FramedBlockColor implements BlockColor
{
    public static final FramedBlockColor INSTANCE = new FramedBlockColor();

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex)
    {
        if (level != null && pos != null)
        {
            ModelData modelData = level.getModelData(pos);
            if (tintIndex < -1 && state.getBlock() instanceof IFramedDoubleBlock)
            {
                FramedBlockData fbData = unpackData(state, modelData, true);
                if (fbData != null)
                {
                    tintIndex = ModelUtils.decodeSecondaryTintIndex(tintIndex);
                    return fbData.getCamoContainer().getTintColor(level, pos, tintIndex);
                }
            }
            else if (tintIndex >= 0)
            {
                FramedBlockData fbData = unpackData(state, modelData, false);
                if (fbData != null)
                {
                    return fbData.getCamoContainer().getTintColor(level, pos, tintIndex);
                }
            }
        }
        return -1;
    }

    @Nullable
    private static FramedBlockData unpackData(BlockState state, ModelData data, boolean secondary)
    {
        if (secondary)
        {
            ModelData innerData = data.get(IFramedDoubleBlockEntity.DATA_TWO);
            return innerData != null ? innerData.get(FramedBlockData.PROPERTY) : null;
        }
        else if (state.getBlock() instanceof IFramedDoubleBlock)
        {
            ModelData innerData = data.get(IFramedDoubleBlockEntity.DATA_ONE);
            return innerData != null ? innerData.get(FramedBlockData.PROPERTY) : null;
        }
        return data.get(FramedBlockData.PROPERTY);
    }

    // FIXME: move to custom ItemTintSource
    /*@Override
    public int getColor(ItemStack stack, int tintIndex)
    {
        if (!(stack.getItem() instanceof BlockItem item) || !(item.getBlock() instanceof IFramedBlock block))
        {
            return -1;
        }
        if (!ConfigView.Client.INSTANCE.shouldRenderItemModelsWithCamo() || block.getItemModelSource() == null)
        {
            return -1;
        }

        CamoList camos = stack.getOrDefault(Utils.DC_TYPE_CAMO_LIST, CamoList.EMPTY);
        if (tintIndex < -1 && block.getBlockType().isDoubleBlock())
        {
            tintIndex = ModelUtils.decodeSecondaryTintIndex(tintIndex);
            return ARGB.opaque(camos.getCamo(1).getTintColor(stack, tintIndex));
        }
        else if (tintIndex >= 0)
        {
            return ARGB.opaque(camos.getCamo(0).getTintColor(stack, tintIndex));
        }
        return -1;
    }*/
}
