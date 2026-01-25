package xfacthd.framedblocks.common.block.cube;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.PlacementStateBuilder;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.FramedBlock;
import xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleBlockEntity;
import xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleCopycatBlockEntity;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.NullableDirection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FramedCollapsibleCopycatBlock extends FramedBlock
{
    private static final int UP = Direction.UP.ordinal();
    private static final int DOWN = Direction.DOWN.ordinal();
    private static final int NORTH = Direction.NORTH.ordinal();
    private static final int EAST = Direction.EAST.ordinal();
    private static final int SOUTH = Direction.SOUTH.ordinal();
    private static final int WEST = Direction.WEST.ordinal();
    private static final Map<Integer, VoxelShape> SHAPE_CACHE = new ConcurrentHashMap<>();
    public static final int ALL_SOLID = 0b00111111;

    public FramedCollapsibleCopycatBlock()
    {
        super(BlockType.FRAMED_COLLAPSIBLE_COPYCAT_BLOCK, Properties::dynamicShape);
        registerDefaultState(defaultBlockState().setValue(PropertyHolder.SOLID_FACES, ALL_SOLID).setValue(PropertyHolder.ROTATE_MODEL, Rotation.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PropertyHolder.SOLID_FACES, BlockStateProperties.WATERLOGGED, PropertyHolder.ROTATE_MODEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return PlacementStateBuilder.of(this, ctx).withWater().build();
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        FramedBlocks.LOGGER.debug("rotate {}, {}", rotation, state.getValue(PropertyHolder.ROTATE_MODEL));
        return state.setValue(PropertyHolder.ROTATE_MODEL, rotation);
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        if (!newState.getValue(PropertyHolder.ROTATE_MODEL).equals(Rotation.NONE) && level.getBlockEntity(pos) instanceof FramedCollapsibleCopycatBlockEntity be)
        {
            FramedBlocks.LOGGER.debug("onChange {} {}", be.getBlockState().getValue(PropertyHolder.ROTATE_MODEL), newState.getValue(PropertyHolder.ROTATE_MODEL));
            be.syncRotationWithBlockState();
            be.setBlockState(newState.setValue(PropertyHolder.ROTATE_MODEL, Rotation.NONE));
            ((Level) level).setBlock(pos, be.getBlockState(), Block.UPDATE_ALL);
        }
        super.onBlockStateChange(level,pos,oldState,newState);
    }

    @Override
    public boolean handleBlockLeftClick(BlockState state, Level level, BlockPos pos, Player player)
    {
        if (player.getMainHandItem().getItem() == FBContent.ITEM_FRAMED_HAMMER.value())
        {
            if (level.getBlockEntity(pos) instanceof FramedCollapsibleCopycatBlockEntity be)
            {
                if (!level.isClientSide())
                {
                    be.handleDeform(player);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx)
    {
        if (isIntangible(state, level, pos, ctx))
        {
            return Shapes.empty();
        }

        int solid = state.getValue(PropertyHolder.SOLID_FACES);
        if (solid != ALL_SOLID && level.getBlockEntity(pos) instanceof FramedCollapsibleCopycatBlockEntity be)
        {
            return SHAPE_CACHE.computeIfAbsent(be.getPackedOffsets(state), key ->
            {
                byte[] offsets = FramedCollapsibleCopycatBlockEntity.unpackOffsets(key);
                return box(
                        offsets[WEST],
                        offsets[DOWN],
                        offsets[NORTH],
                        16 - offsets[EAST],
                        16 - offsets[UP],
                        16 - offsets[SOUTH]
                );
            });
        }
        return Shapes.block();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos)
    {
        return Shapes.empty();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);

        //noinspection ConstantConditions
        if (!level.isClientSide() && stack.get(DataComponents.BLOCK_ENTITY_DATA) != null)
        {
            //Properly set face solidity when placed from a stack with BE NBT data
            if (level.getBlockEntity(pos) instanceof FramedCollapsibleCopycatBlockEntity be)
            {
                be.updateFaceSolidity();
            }
        }
    }

    @Override
    public boolean doesBlockOccludeBeaconBeam(BlockState state, LevelReader level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof FramedCollapsibleCopycatBlockEntity be)
        {
            return be.doesOccludeBeaconBeam();
        }
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedCollapsibleCopycatBlockEntity(pos, state);
    }

    @Override
    public BlockState getItemModelSource()
    {
        return defaultBlockState();
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return state;
    }
}
