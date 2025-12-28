package io.github.xfacthd.framedblocks.client.model.unbaked;

import io.github.xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import io.github.xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import io.github.xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import io.github.xfacthd.framedblocks.client.model.ReinforcementModel;
import io.github.xfacthd.framedblocks.client.model.baked.FramedBlockModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;

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
    }
}
