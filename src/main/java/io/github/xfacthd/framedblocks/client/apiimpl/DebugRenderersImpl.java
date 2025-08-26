package io.github.xfacthd.framedblocks.client.apiimpl;

import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import io.github.xfacthd.framedblocks.api.render.debug.BlockDebugRenderer;
import io.github.xfacthd.framedblocks.api.render.debug.DebugRenderers;
import io.github.xfacthd.framedblocks.client.render.debug.impl.ConnectionPredicateDebugRenderer;
import io.github.xfacthd.framedblocks.client.render.debug.impl.DoubleBlockPartDebugRenderer;
import io.github.xfacthd.framedblocks.client.render.debug.impl.QuadWindingDebugRenderer;

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
