package xfacthd.framedblocks.common.block.pane;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.block.PlacementStateBuilder;
import xfacthd.framedblocks.common.block.FramedBlock;
import xfacthd.framedblocks.common.data.BlockType;

public class FramedBoardBlock extends FramedBlock
{
    public FramedBoardBlock(Properties props)
    {
        super(BlockType.FRAMED_BOARD, props);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.FACING, BlockStateProperties.WATERLOGGED, FramedProperties.SOLID);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return PlacementStateBuilder.of(this, ctx)
                .withTargetFacing()
                .withWater()
                .build();
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot)
    {
        Direction dir = state.getValue(BlockStateProperties.FACING);
        return state.setValue(BlockStateProperties.FACING, rot.rotate(dir));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return BlockUtils.mirrorFaceBlock(state, BlockStateProperties.FACING, mirror);
    }

    @Override
    public BlockState getItemModelSource()
    {
        return defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH);
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState().setValue(BlockStateProperties.FACING, Direction.SOUTH);
    }
}
