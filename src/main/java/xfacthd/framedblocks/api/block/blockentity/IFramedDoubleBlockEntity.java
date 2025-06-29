package xfacthd.framedblocks.api.block.blockentity;

import xfacthd.framedblocks.api.camo.CamoContainer;

// TODO: remove when FramedDoubleBlockEntity is moved to API
public non-sealed interface IFramedDoubleBlockEntity extends IFramedBlockEntity
{
    String CAMO_TWO_NBT_KEY = "camo_two";

    CamoContainer<?, ?> getCamoTwo();
}
