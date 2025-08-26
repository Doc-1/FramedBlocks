package io.github.xfacthd.framedblocks.client.render.color;

import io.github.xfacthd.framedblocks.api.block.render.FramedBlockColor;
import io.github.xfacthd.framedblocks.api.model.util.ModelUtils;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedFlowerPotBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class FramedFlowerPotColor extends FramedBlockColor
{
    public static final FramedFlowerPotColor INSTANCE = new FramedFlowerPotColor();

    private FramedFlowerPotColor() { }

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex)
    {
        if (tintIndex < -1 && level != null && pos != null)
        {
            Block plantBlock = level.getModelData(pos).get(FramedFlowerPotBlockEntity.FLOWER_BLOCK);
            if (plantBlock != null && plantBlock != Blocks.AIR)
            {
                BlockState plantState = plantBlock.defaultBlockState();
                tintIndex = ModelUtils.decodeSecondaryTintIndex(tintIndex);
                return Minecraft.getInstance().getBlockColors().getColor(plantState, level, pos, tintIndex);
            }
            return -1;
        }
        return super.getColor(state, level, pos, tintIndex);
    }
}
