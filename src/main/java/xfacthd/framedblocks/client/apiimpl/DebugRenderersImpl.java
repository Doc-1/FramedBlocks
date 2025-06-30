package xfacthd.framedblocks.client.apiimpl;

import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import xfacthd.framedblocks.api.render.debug.DebugRenderers;
import xfacthd.framedblocks.client.render.debug.impl.ConnectionPredicateDebugRenderer;
import xfacthd.framedblocks.client.render.debug.impl.DoubleBlockPartDebugRenderer;
import xfacthd.framedblocks.client.render.debug.impl.QuadWindingDebugRenderer;

public final class DebugRenderersImpl implements DebugRenderers
{
    @Override
    public BlockDebugRenderer<FramedBlockEntity> connectionPredicate()
    {
        return ConnectionPredicateDebugRenderer.INSTANCE;
    }

    @Override
    public BlockDebugRenderer<FramedBlockEntity> quadWinding()
    {
        return QuadWindingDebugRenderer.INSTANCE;
    }

    @Override
    public BlockDebugRenderer<FramedDoubleBlockEntity> doubleBlockPart()
    {
        return DoubleBlockPartDebugRenderer.INSTANCE;
    }
}
