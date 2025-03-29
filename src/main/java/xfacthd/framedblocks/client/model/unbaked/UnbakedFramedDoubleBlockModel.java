package xfacthd.framedblocks.client.model.unbaked;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import xfacthd.framedblocks.api.model.item.ItemModelInfo;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.client.model.baked.FramedDoubleBlockModel;
import xfacthd.framedblocks.common.data.doubleblock.NullCullPredicate;

public final class UnbakedFramedDoubleBlockModel extends AbstractUnbakedFramedBlockModel
{
    private final NullCullPredicate nullCullPredicate;
    private final ItemModelInfo itemModelInfo;

    public UnbakedFramedDoubleBlockModel(ModelFactory.Context ctx, NullCullPredicate nullCullPredicate, ItemModelInfo itemModelInfo)
    {
        super(ctx);
        this.nullCullPredicate = nullCullPredicate;
        this.itemModelInfo = itemModelInfo;
    }

    @Override
    protected BlockStateModel bakeCached(GeometryFactory.Context context)
    {
        return new FramedDoubleBlockModel(context, nullCullPredicate, itemModelInfo);
    }
}
