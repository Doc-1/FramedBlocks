package io.github.xfacthd.framedblocks.api.model.wrapping;

import io.github.xfacthd.framedblocks.api.model.ExtendedBlockModelPart;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DelegateBlockModelPart extends ExtendedBlockModelPart
{
    ExtendedBlockModelPart wrapped();

    @Override
    default List<BakedQuad> getQuads(@Nullable Direction side)
    {
        return wrapped().getQuads(side);
    }

    @Override
    default ChunkSectionLayer getRenderType(BlockState state)
    {
        return wrapped().getRenderType(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    default boolean useAmbientOcclusion()
    {
        return wrapped().useAmbientOcclusion();
    }

    @Override
    default TriState ambientOcclusion()
    {
        return wrapped().ambientOcclusion();
    }

    @Override
    default TextureAtlasSprite particleIcon()
    {
        return wrapped().particleIcon();
    }

    @Nullable
    @Override
    default BlockState getBlockAppearance()
    {
        return wrapped().getBlockAppearance();
    }
}
