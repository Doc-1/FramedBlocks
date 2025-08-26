package io.github.xfacthd.framedblocks.client.util.duck;

import io.github.xfacthd.framedblocks.mixin.client.AccessorBlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

@SuppressWarnings("unused") // Used via interface injection
public interface DefaultedAccessorBlockModelShaper extends AccessorBlockModelShaper
{
    @Override
    default Map<BlockState, BlockStateModel> framedblocks$getModelByStateCache()
    {
        throw new AssertionError();
    }
}
