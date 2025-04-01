package xfacthd.framedblocks.client.util.duck;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.mixin.client.AccessorBlockModelShaper;

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
