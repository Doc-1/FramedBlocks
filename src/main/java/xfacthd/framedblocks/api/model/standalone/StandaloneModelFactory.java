package xfacthd.framedblocks.api.model.standalone;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public interface StandaloneModelFactory<T extends CachingModel>
{
    T create(Map<BlockState, BlockStateModel> modes);
}
