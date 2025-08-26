package io.github.xfacthd.framedblocks.common.block.sign;

import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedSignBlockEntity;
import io.github.xfacthd.framedblocks.common.data.BlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractFramedHangingSignBlock extends AbstractFramedSignBlock
{
    protected AbstractFramedHangingSignBlock(BlockType type, Properties props)
    {
        super(type, props);
    }

    @Override
    public int getTextLineHeight()
    {
        return 9;
    }

    @Override
    public int getMaxTextLineWidth()
    {
        return 60;
    }

    @Override
    public SoundEvent getSignInteractionFailedSoundEvent()
    {
        return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL;
    }

    @Override
    public FramedBlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return FramedSignBlockEntity.hangingSign(pos, state);
    }
}
