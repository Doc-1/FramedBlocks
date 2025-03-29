package xfacthd.framedblocks.api.render.debug;

import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.util.Utils;

@ApiStatus.NonExtendable
@SuppressWarnings("unused")
public interface DebugRenderers
{
    DebugRenderers INSTANCE = Utils.loadService(DebugRenderers.class);

    BlockDebugRenderer<FramedBlockEntity> connectionPredicate();

    BlockDebugRenderer<FramedBlockEntity> quadWinding();
}
