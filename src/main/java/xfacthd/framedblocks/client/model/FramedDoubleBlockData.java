package xfacthd.framedblocks.client.model;

import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.data.AbstractFramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;

public final class FramedDoubleBlockData extends AbstractFramedBlockData
{
    private final DoubleBlockParts parts;
    private final FramedBlockData dataOne;
    private final FramedBlockData dataTwo;

    public FramedDoubleBlockData(DoubleBlockParts parts, FramedBlockData dataOne, FramedBlockData dataTwo)
    {
        this.parts = parts;
        this.dataOne = dataOne;
        this.dataTwo = dataTwo;
    }

    @Override
    public FramedBlockData unwrap(BlockState partState)
    {
        return partState == parts.stateTwo() ? dataTwo : dataOne;
    }

    @Override
    public FramedBlockData unwrap(boolean secondary)
    {
        return secondary ? dataTwo : dataOne;
    }

    @Override
    public boolean isCamoEmissive()
    {
        return dataOne.isCamoEmissive() || dataTwo.isCamoEmissive();
    }
}
