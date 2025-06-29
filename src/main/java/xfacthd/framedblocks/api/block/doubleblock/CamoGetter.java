package xfacthd.framedblocks.api.block.doubleblock;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;

import java.util.function.Function;

public enum CamoGetter
{
    NONE(be -> EmptyCamoContainer.EMPTY, data -> FramedBlockData.EMPTY, parts -> null),
    FIRST(FramedDoubleBlockEntity::getCamo, data -> data.unwrap(false), DoubleBlockParts::stateOne),
    SECOND(FramedDoubleBlockEntity::getCamoTwo, data -> data.unwrap(true), DoubleBlockParts::stateTwo),
    ;

    private final Function<FramedDoubleBlockEntity, CamoContainer<?, ?>> entityCamoGetter;
    private final Function<AbstractFramedBlockData, FramedBlockData> modelDataUnwrapper;
    private final Function<DoubleBlockParts, @Nullable BlockState> partGetter;

    CamoGetter(
            Function<FramedDoubleBlockEntity, CamoContainer<?, ?>> entityCamoGetter,
            Function<AbstractFramedBlockData, FramedBlockData> modelDataUnwrapper,
            Function<DoubleBlockParts, @Nullable BlockState> partGetter
    )
    {
        this.entityCamoGetter = entityCamoGetter;
        this.modelDataUnwrapper = modelDataUnwrapper;
        this.partGetter = partGetter;
    }

    public CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be)
    {
        return entityCamoGetter.apply(be);
    }

    public CamoContainer<?, ?> getCamo(AbstractFramedBlockData data)
    {
        return modelDataUnwrapper.apply(data).getCamoContainer();
    }

    @Nullable
    public BlockState getComponent(DoubleBlockParts parts)
    {
        return partGetter.apply(parts);
    }



    public static CamoGetter get(boolean first, boolean second)
    {
        if (first && second)
        {
            throw new IllegalArgumentException("Only first or second may be true");
        }
        if (first)
        {
            return FIRST;
        }
        if (second)
        {
            return SECOND;
        }
        return NONE;
    }
}
