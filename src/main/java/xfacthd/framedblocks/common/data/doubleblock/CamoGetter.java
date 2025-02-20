package xfacthd.framedblocks.common.data.doubleblock;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.common.blockentity.doubled.FramedDoubleBlockEntity;

public enum CamoGetter
{
    NONE
    {
        @Override
        public CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be)
        {
            return EmptyCamoContainer.EMPTY;
        }

        @Override
        @Nullable
        public BlockState getComponent(DoubleBlockParts parts)
        {
            return null;
        }
    },
    FIRST
    {
        @Override
        public CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be)
        {
            return be.getCamo();
        }

        @Override
        public BlockState getComponent(DoubleBlockParts parts)
        {
            return parts.stateOne();
        }
    },
    SECOND
    {
        @Override
        public CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be)
        {
            return be.getCamoTwo();
        }

        @Override
        public BlockState getComponent(DoubleBlockParts parts)
        {
            return parts.stateTwo();
        }
    };

    public abstract CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be);

    @Nullable
    public abstract BlockState getComponent(DoubleBlockParts parts);



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
