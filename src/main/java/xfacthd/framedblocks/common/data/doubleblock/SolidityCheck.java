package xfacthd.framedblocks.common.data.doubleblock;

import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.CamoContent;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;

import java.util.function.Predicate;

public enum SolidityCheck
{
    NONE(data -> false, null),
    FIRST(data -> data.unwrap(false).getCamoContent().isSolid(), FramedDoubleBlockEntity::getCamo),
    SECOND(data -> data.unwrap(true).getCamoContent().isSolid(), FramedDoubleBlockEntity::getCamoTwo),
    BOTH(data -> FIRST.isSolid(data) && SECOND.isSolid(data), null);

    private final Predicate<AbstractFramedBlockData> predicate;
    @Nullable
    private final CamoGetter plantableCamoGetter;

    SolidityCheck(Predicate<AbstractFramedBlockData> predicate, @Nullable CamoGetter plantableCamoGetter)
    {
        this.predicate = predicate;
        this.plantableCamoGetter = plantableCamoGetter;
    }

    public boolean isSolid(AbstractFramedBlockData data)
    {
        return predicate.test(data);
    }

    public TriState canSustainPlant(FramedDoubleBlockEntity be, BlockGetter level, Direction side, BlockState plant)
    {
        if (plantableCamoGetter == null) return TriState.DEFAULT;
        CamoContent<?> camo = plantableCamoGetter.get(be).getContent();
        return camo.canSustainPlant(level, be.getBlockPos(), side, plant);
    }

    @FunctionalInterface
    private interface CamoGetter
    {
        CamoContainer<?, ?> get(FramedDoubleBlockEntity be);
    }
}
