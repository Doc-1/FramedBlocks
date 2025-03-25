package xfacthd.framedblocks.api.model.wrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;

public interface ModelFactory
{
    BlockStateModel create(GeometryFactory.Context ctx);

    default void reset() { }
}
