package xfacthd.framedblocks.client.modelwrapping;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.util.Lazy;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class CopyingModelFactory implements ModelFactory
{
    private final Holder<Block> srcBlock;
    private final Lazy<ModelWrappingHandler> sourceWrapper;

    public CopyingModelFactory(Holder<Block> srcBlock)
    {
        this.srcBlock = srcBlock;
        this.sourceWrapper = Lazy.of(() -> ModelWrappingManager.getHandler(srcBlock.value()));
    }

    @Override
    public BlockStateModel create(GeometryFactory.Context ctx)
    {
        BlockState state = ctx.state();
        BlockState srcState = srcBlock.value().defaultBlockState();
        for (Property prop : state.getProperties())
        {
            if (srcState.hasProperty(prop))
            {
                srcState = srcState.setValue(prop, state.getValue(prop));
            }
        }
        BlockStateModel baseModel = ctx.modelLookup().getBlockStateModel(srcState);
        return sourceWrapper.get().wrapBlockModel(
                baseModel, srcState, ctx.modelLookup(), ctx.textureLookup(), null
        );
    }
}
