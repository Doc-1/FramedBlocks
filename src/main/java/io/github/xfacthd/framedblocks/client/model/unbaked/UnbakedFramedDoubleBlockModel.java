package io.github.xfacthd.framedblocks.client.model.unbaked;

import io.github.xfacthd.framedblocks.api.block.render.NullCullPredicate;
import io.github.xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import io.github.xfacthd.framedblocks.api.model.item.ItemModelInfo;
import io.github.xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import io.github.xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import io.github.xfacthd.framedblocks.client.model.baked.FramedDoubleBlockModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;

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
    protected BlockStateModel bakeCached(GeometryFactory.Context context, ModelBaker baker)
    {
        return new FramedDoubleBlockModel(context, nullCullPredicate, itemModelInfo);
    }
}
