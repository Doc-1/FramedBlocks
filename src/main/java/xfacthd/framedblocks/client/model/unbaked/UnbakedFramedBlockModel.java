package xfacthd.framedblocks.client.model.unbaked;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.client.model.FluidModel;
import xfacthd.framedblocks.client.model.ReinforcementModel;
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
    protected BlockStateModel bakeCached(GeometryFactory.Context context, ModelBaker baker)
    {
        ReinforcementModel reinforcement = ReinforcementModel.getOrCreate(baker);
        return new FramedBlockModel(context, geometryFactory.create(context), reinforcement);
    }

    @Override
    protected void resolveSpecialDependencies(Resolver resolver)
    {
        resolver.markDependency(ReinforcementModel.MODEL_ID);
        resolver.markDependency(FluidModel.BARE_MODEL);
        resolver.markDependency(FluidModel.BARE_MODEL_SINGLE);
    }
}
