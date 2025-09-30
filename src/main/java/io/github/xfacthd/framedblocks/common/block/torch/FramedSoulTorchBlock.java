package io.github.xfacthd.framedblocks.common.block.torch;

import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.data.BlockType;
import io.github.xfacthd.framedblocks.common.item.block.FramedStandingAndWallBlockItem;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class FramedSoulTorchBlock extends FramedTorchBlock
{
    public FramedSoulTorchBlock(Properties props)
    {
        super(ParticleTypes.SOUL_FIRE_FLAME, props
                .pushReaction(PushReaction.DESTROY)
                .noCollision()
                .strength(0.5F)
                .sound(SoundType.WOOD)
                .lightLevel(state -> 14)
                .noOcclusion()
        );
    }

    @Override
    public BlockType getBlockType()
    {
        return BlockType.FRAMED_SOUL_TORCH;
    }

    @Override
    public BlockItem createBlockItem(Item.Properties props)
    {
        return new FramedStandingAndWallBlockItem(
                FBContent.BLOCK_FRAMED_SOUL_TORCH.value(),
                FBContent.BLOCK_FRAMED_SOUL_WALL_TORCH.value(),
                Direction.DOWN,
                props
        );
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState();
    }

    @Override
    public float getJadeRenderScale(BlockState state)
    {
        return 2F;
    }
}
