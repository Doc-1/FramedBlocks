package xfacthd.framedblocks.common.block.slab;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.api.block.doubleblock.DoubleBlockParts;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.FramedDoubleBlock;
import xfacthd.framedblocks.common.block.cube.FramedCollapsibleCopycatBlock;
import xfacthd.framedblocks.common.blockentity.doubled.slab.FramedAdjustableDoubleBlockEntity;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.NullableDirection;

import java.util.function.Function;

public abstract class FramedAdjustableDoubleBlock extends FramedDoubleBlock
{
    private final Function<BlockState, Direction> facingGetter;
    private final Function<BlockState, DoubleBlockParts> partsBuilder;
    private final BlockEntityType.BlockEntitySupplier<FramedAdjustableDoubleBlockEntity> beSupplier;

    protected FramedAdjustableDoubleBlock(
            BlockType type,
            Properties props,
            Function<BlockState, Direction> facingGetter,
            Function<BlockState, DoubleBlockParts> partsBuilder,
            BlockEntityType.BlockEntitySupplier<FramedAdjustableDoubleBlockEntity> beSupplier
    )
    {
        super(type, props);
        this.facingGetter = facingGetter;
        this.partsBuilder = partsBuilder;
        this.beSupplier = beSupplier;
    }

    @Override
    public boolean handleBlockLeftClick(BlockState state, Level level, BlockPos pos, Player player)
    {
        if (player.getMainHandItem().getItem() == FBContent.ITEM_FRAMED_HAMMER.value())
        {
            if (level.getBlockEntity(pos) instanceof FramedAdjustableDoubleBlockEntity be)
            {
                return be.handleDeform(player);
            }
        }
        return false;
    }

    @Override
    public FramedDoubleBlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return beSupplier.create(pos, state);
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState();
    }

    @Override
    public DoubleBlockParts calculateParts(BlockState state)
    {
        return partsBuilder.apply(state);
    }

    public Direction getFacing(BlockState state)
    {
        return facingGetter.apply(state);
    }

    @Override
    public BlockState getItemModelSource()
    {
        return defaultBlockState();
    }



    protected static DoubleBlockParts makeStandardParts(BlockState state)
    {
        Direction facing = ((FramedAdjustableDoubleBlock) state.getBlock()).getFacing(state);
        BlockState defState = FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK.value().defaultBlockState();
        return new DoubleBlockParts(
                defState.setValue(PropertyHolder.NULLABLE_FACE, NullableDirection.fromDirection(facing)),
                defState.setValue(PropertyHolder.NULLABLE_FACE, NullableDirection.fromDirection(facing.getOpposite()))
        );
    }

    protected static DoubleBlockParts makeCopycatParts(BlockState state)
    {
        Direction facing = ((FramedAdjustableDoubleBlock) state.getBlock()).getFacing(state);
        BlockState defState = FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK.value().defaultBlockState();
        int solidFirst = ~(1 << facing.ordinal()) & FramedCollapsibleCopycatBlock.ALL_SOLID;
        int solidSecond = ~(1 << facing.getOpposite().ordinal()) & FramedCollapsibleCopycatBlock.ALL_SOLID;
        return new DoubleBlockParts(
                defState.setValue(PropertyHolder.SOLID_FACES, solidFirst),
                defState.setValue(PropertyHolder.SOLID_FACES, solidSecond)
        );
    }
}
