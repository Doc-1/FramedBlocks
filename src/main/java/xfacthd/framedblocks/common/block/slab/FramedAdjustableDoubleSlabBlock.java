package xfacthd.framedblocks.common.block.slab;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.common.blockentity.doubled.slab.FramedAdjustableDoubleBlockEntity;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.doubleblock.CamoGetter;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockTopInteractionMode;
import xfacthd.framedblocks.common.data.doubleblock.SolidityCheck;

import java.util.function.Function;

public class FramedAdjustableDoubleSlabBlock extends FramedAdjustableDoubleBlock
{
    private FramedAdjustableDoubleSlabBlock(
            BlockType type,
            Properties props,
            Function<BlockState, DoubleBlockParts> partsBuilder,
            BlockEntityType.BlockEntitySupplier<FramedAdjustableDoubleBlockEntity> beSupplier
    )
    {
        super(type, props, state -> Direction.UP, partsBuilder, beSupplier);
    }

    @Override
    public DoubleBlockTopInteractionMode calculateTopInteractionMode(BlockState state)
    {
        return DoubleBlockTopInteractionMode.SECOND;
    }

    @Override
    public SolidityCheck calculateSolidityCheck(BlockState state, Direction side)
    {
        return switch (side)
        {
            case DOWN -> SolidityCheck.FIRST;
            case UP -> SolidityCheck.SECOND;
            case NORTH, SOUTH, WEST, EAST -> SolidityCheck.BOTH;
        };
    }

    @Override
    public CamoGetter calculateCamoGetter(BlockState state, Direction side, @Nullable Direction edge)
    {
        return switch (side)
        {
            case DOWN -> CamoGetter.FIRST;
            case UP -> CamoGetter.SECOND;
            case NORTH, SOUTH, WEST, EAST ->
            {
                if (edge == Direction.DOWN) yield CamoGetter.FIRST;
                if (edge == Direction.UP) yield CamoGetter.SECOND;
                yield CamoGetter.NONE;
            }
        };
    }



    public static FramedAdjustableDoubleSlabBlock standard(Properties props)
    {
        return new FramedAdjustableDoubleSlabBlock(
                BlockType.FRAMED_ADJ_DOUBLE_SLAB,
                props,
                FramedAdjustableDoubleBlock::makeStandardParts,
                FramedAdjustableDoubleBlockEntity::standard
        );
    }

    public static FramedAdjustableDoubleSlabBlock copycat(Properties props)
    {
        return new FramedAdjustableDoubleSlabBlock(
                BlockType.FRAMED_ADJ_DOUBLE_COPYCAT_SLAB,
                props,
                FramedAdjustableDoubleBlock::makeCopycatParts,
                FramedAdjustableDoubleBlockEntity::copycat
        );
    }
}
