package xfacthd.framedblocks.common.data.doubleblock;

import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;

import java.util.function.Predicate;

public enum SolidityCheck
{
    NONE(
            data -> false,
            (be, level, side, plant) -> TriState.DEFAULT
    ),
    FIRST(
            data -> data.unwrap(false).getCamoContent().isSolid(),
            (be, level, side, plant) -> be.getCamo().getContent().canSustainPlant(level, be.getBlockPos(), side, plant)
    ),
    SECOND(
            data -> data.unwrap(true).getCamoContent().isSolid(),
            (be, level, side, plant) -> be.getCamoTwo().getContent().canSustainPlant(level, be.getBlockPos(), side, plant)
    ),
    BOTH(
            data -> FIRST.isSolid(data) && SECOND.isSolid(data),
            (be, level, side, plant) -> TriState.DEFAULT
    );

    private final Predicate<AbstractFramedBlockData> predicate;
    private final PlantablePredicate plantablePredicate;

    SolidityCheck(Predicate<AbstractFramedBlockData> predicate, PlantablePredicate plantablePredicate)
    {
        this.predicate = predicate;
        this.plantablePredicate = plantablePredicate;
    }

    public boolean isSolid(AbstractFramedBlockData data)
    {
        return predicate.test(data);
    }

    public TriState canSustainPlant(FramedDoubleBlockEntity be, BlockGetter level, Direction side, BlockState plant)
    {
        return plantablePredicate.test(be, level, side, plant);
    }
}
