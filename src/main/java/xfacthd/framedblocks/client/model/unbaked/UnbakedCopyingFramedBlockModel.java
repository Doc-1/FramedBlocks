package xfacthd.framedblocks.client.model.unbaked;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import xfacthd.framedblocks.client.model.baked.CopyingFramedBlockModel;

public final class UnbakedCopyingFramedBlockModel extends AbstractUnbakedFramedBlockModel
{
    private final Block srcBlock;

    public UnbakedCopyingFramedBlockModel(ModelFactory.Context ctx, Block srcBlock)
    {
        super(ctx);
        this.srcBlock = srcBlock;
    }

    @Override
    protected BlockStateModel bakeCached(GeometryFactory.Context ctx, ModelBaker baker)
    {
        BlockState srcState = srcBlock.withPropertiesOf(ctx.state());
        return new CopyingFramedBlockModel(ctx.baseModel(), srcState);
    }
}
