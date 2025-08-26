package io.github.xfacthd.framedblocks.api.block.render;

import io.github.xfacthd.framedblocks.api.block.IFramedDoubleBlock;
import io.github.xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import io.github.xfacthd.framedblocks.api.model.data.FramedBlockData;
import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

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
                FramedBlockData fbData = unpackData(modelData, true);
                if (fbData != null)
                {
                    tintIndex = ModelUtils.decodeSecondaryTintIndex(tintIndex);
                    return fbData.getCamoContainer().getTintColor(level, pos, tintIndex);
                }
            }
            else if (tintIndex >= 0)
            {
                FramedBlockData fbData = unpackData(modelData, false);
                if (fbData != null)
                {
                    return fbData.getCamoContainer().getTintColor(level, pos, tintIndex);
                }
            }
        }
        return -1;
    }

    @Nullable
    private static FramedBlockData unpackData(ModelData data, boolean secondary)
    {
        AbstractFramedBlockData blockData = data.get(AbstractFramedBlockData.PROPERTY);
        return blockData != null ? blockData.unwrap(secondary) : null;
    }
}
