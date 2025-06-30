package xfacthd.framedblocks.common.block.slab;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.doubleblock.CamoGetter;
import xfacthd.framedblocks.api.block.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.api.block.doubleblock.DoubleBlockTopInteractionMode;
import xfacthd.framedblocks.api.block.doubleblock.SolidityCheck;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.FramedDoubleBlock;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;

public class FramedCheckeredCubeBlock extends FramedDoubleBlock
{
    public FramedCheckeredCubeBlock(Properties props)
    {
        super(BlockType.FRAMED_CHECKERED_CUBE, props);
    }

    @Override
    public DoubleBlockParts calculateParts(BlockState state)
    {
        BlockState segmentState = FBContent.BLOCK_FRAMED_CHECKERED_CUBE_SEGMENT.value().defaultBlockState();
        return new DoubleBlockParts(segmentState, segmentState.setValue(PropertyHolder.SECOND, true));
    }

    @Override
    public DoubleBlockTopInteractionMode calculateTopInteractionMode(BlockState state)
    {
        return DoubleBlockTopInteractionMode.EITHER;
    }

    @Override
    public CamoGetter calculateCamoGetter(BlockState state, Direction side, @Nullable Direction edge)
    {
        return CamoGetter.NONE;
    }

    @Override
    public SolidityCheck calculateSolidityCheck(BlockState state, Direction side)
    {
        return SolidityCheck.BOTH;
    }

    @Override
    public BlockState getItemModelSource()
    {
        return defaultBlockState();
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState();
    }
}
