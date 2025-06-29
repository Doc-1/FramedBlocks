package xfacthd.framedblocks.api.model.data;

import net.minecraft.core.BlockPos;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import xfacthd.framedblocks.api.block.doubleblock.DoubleBlockParts;

public final class FramedDoubleBlockData extends AbstractFramedBlockData
{
    private final BlockState partStateOne;
    private final BlockState partStateTwo;
    private final FramedBlockData dataOne;
    private final FramedBlockData dataTwo;

    public FramedDoubleBlockData(DoubleBlockParts parts, FramedBlockData dataOne, FramedBlockData dataTwo)
    {
        this.partStateOne = parts.stateOne();
        this.partStateTwo = parts.stateTwo();
        this.dataOne = dataOne;
        this.dataTwo = dataTwo;
    }

    @Override
    public FramedBlockData unwrap(BlockState partState)
    {
        if (partState == partStateOne) return dataOne;
        if (partState == partStateTwo) return dataTwo;
        if (!FMLEnvironment.production)
        {
            throw new IllegalArgumentException("Received invalid part state " + partState + ", expected " + partStateOne + " or " + partStateTwo);
        }
        return FramedBlockData.EMPTY;
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

    @Override
    public float getCamoShadeBrightness(BlockGetter level, BlockPos pos, float frameShade)
    {
        return Math.max(
                dataOne.getCamoShadeBrightness(level, pos, frameShade),
                dataTwo.getCamoShadeBrightness(level, pos, frameShade)
        );
    }

    @Override
    public TriState isViewBlocking()
    {
        return dataOne.isViewBlocking();
    }
}
