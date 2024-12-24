package xfacthd.framedblocks.api.block.render;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import xfacthd.framedblocks.api.block.blockentity.IFramedDoubleBlockEntity;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.util.ModelUtils;

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
}
