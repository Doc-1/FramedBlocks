package xfacthd.framedblocks.client.model.wrapping;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.api.model.standalone.CachingModel;
import xfacthd.framedblocks.api.model.standalone.StandaloneModelFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.api.model.wrapping.statemerger.StateMerger;

public final class StandaloneModelWrappingHandler<T extends CachingModel> extends ModelWrappingHandler
{
    private final StandaloneModelFactory<T> modelFactory;

    public StandaloneModelWrappingHandler(
            Holder<Block> block,
            ModelFactory blockModelFactory,
            StateMerger stateMerger,
            StandaloneModelFactory<T> modelFactory
    )
    {
        super(block, blockModelFactory, stateMerger);
        this.modelFactory = modelFactory;
    }

    public StandaloneModelFactory<T> getModelFactory()
    {
        return modelFactory;
    }
}
