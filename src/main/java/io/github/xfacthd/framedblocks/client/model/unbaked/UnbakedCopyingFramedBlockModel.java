package io.github.xfacthd.framedblocks.client.model.unbaked;

import io.github.xfacthd.framedblocks.api.model.AbstractUnbakedFramedBlockModel;
import io.github.xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import io.github.xfacthd.framedblocks.api.model.wrapping.ModelFactory;
import io.github.xfacthd.framedblocks.client.model.baked.CopyingFramedBlockModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
