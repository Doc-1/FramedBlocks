package xfacthd.framedblocks.common.data.doubleblock;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.empty.EmptyCamoContainer;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
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
        public CamoContainer<?, ?> getCamo(AbstractFramedBlockData data)
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
        public CamoContainer<?, ?> getCamo(AbstractFramedBlockData data)
        {
            return data.unwrap(false).getCamoContainer();
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
        public CamoContainer<?, ?> getCamo(AbstractFramedBlockData data)
        {
            return data.unwrap(true).getCamoContainer();
        }

        @Override
        public BlockState getComponent(DoubleBlockParts parts)
        {
            return parts.stateTwo();
        }
    };

    public abstract CamoContainer<?, ?> getCamo(FramedDoubleBlockEntity be);

    public abstract CamoContainer<?, ?> getCamo(AbstractFramedBlockData data);

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
