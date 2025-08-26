package io.github.xfacthd.framedblocks.client.render.color;

import io.github.xfacthd.framedblocks.api.block.render.FramedBlockColor;
import io.github.xfacthd.framedblocks.client.model.geometry.cube.FramedTargetGeometry;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedTargetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class FramedTargetBlockColor extends FramedBlockColor
{
    public static final FramedTargetBlockColor INSTANCE = new FramedTargetBlockColor();

    private FramedTargetBlockColor() { }

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex)
    {
        if (tintIndex == FramedTargetGeometry.OVERLAY_TINT_IDX && level != null && pos != null)
        {
            if (level.getBlockEntity(pos) instanceof FramedTargetBlockEntity be)
            {
                return be.getOverlayColor();
            }
        }
        return super.getColor(state, level, pos, tintIndex);
    }
}
