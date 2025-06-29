package xfacthd.framedblocks.api.block.blockentity;

import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.api.camo.CamoContainer;

// TODO: remove when FramedDoubleBlockEntity is moved to API
@ApiStatus.Internal
public sealed interface IFramedBlockEntity permits FramedBlockEntity, IFramedDoubleBlockEntity
{
    CamoContainer<?, ?> getCamo();
}
