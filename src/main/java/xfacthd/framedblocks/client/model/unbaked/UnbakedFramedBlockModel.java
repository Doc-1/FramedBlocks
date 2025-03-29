package xfacthd.framedblocks.client.model.unbaked;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.client.model.baked.FramedBlockModel;

public final class UnbakedFramedBlockModel extends AbstractUnbakedFramedBlockModel
{
    private final GeometryFactory geometryFactory;

    public UnbakedFramedBlockModel(ModelFactory.Context ctx, GeometryFactory geometryFactory)
    {
        super(ctx);
        this.geometryFactory = geometryFactory;
    }

    @Override
    protected BlockStateModel bakeCached(GeometryFactory.Context context)
    {
        return new FramedBlockModel(context, geometryFactory.create(context));
    }
}
