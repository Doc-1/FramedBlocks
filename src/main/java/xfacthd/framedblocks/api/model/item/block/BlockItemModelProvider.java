package xfacthd.framedblocks.api.model.item.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.internal.InternalClientAPI;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;

import java.util.function.Supplier;

public interface BlockItemModelProvider
{
    BlockItemModelProvider DEFAULT = (state, baker) -> () -> Minecraft.getInstance().getBlockRenderer().getBlockModel(state);

    Supplier<BlockStateModel> create(BlockState state, ModelBaker baker);

    static Supplier<BlockStateModel> forGeometry(BlockState state, BlockState srcState, GeometryFactory geometry)
    {
        return InternalClientAPI.INSTANCE.createBlockItemModelProviderForGeometry(state, srcState, geometry);
    }
}
