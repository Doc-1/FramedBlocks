package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.geometry.Geometry;

public interface GeometryFactory
{
    Geometry create(Context ctx);



    record Context(BlockState state, BlockStateModel baseModel, ModelLookup modelLookup, TextureLookup textureLookup) { }
}
