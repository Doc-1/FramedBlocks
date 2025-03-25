package xfacthd.framedblocks.common.block.pane;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.*;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;

import java.util.List;

public class FramedPaneBlock extends IronBarsBlock implements IFramedBlock
{
    private final BlockType type;

    public FramedPaneBlock(BlockType type, Properties props)
    {
        super(IFramedBlock.applyDefaultProperties(props, type));
        this.type = type;
        BlockUtils.configureStandardProperties(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        BlockUtils.addRequiredProperties(builder);
        builder.add(FramedProperties.STATE_LOCKED);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    )
    {
        return handleUse(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        tryApplyCamoImmediately(level, pos, placer, stack);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess tickAccess,
            BlockPos pos,
            Direction side,
            BlockPos adjPos,
            BlockState adjState,
            RandomSource random
    )
    {
        BlockState newState = updateShapeLockable(
                state, level, tickAccess, pos,
                () -> super.updateShape(state, level, tickAccess, pos, side, adjPos, adjState, random)
        );

        if (newState == state)
        {
            updateCulling(level, pos);
        }
        return newState;
    }

    /*@Override // TODO: Missing side context
    public boolean attachsTo(BlockState adjState, boolean sideSolid)
    {
        Direction adjSide = null;
        if (adjSide != null && !Utils.isY(adjSide) && DiagonalBlocksCompat.isFramedPane(adjState) && adjState.getValue(FramedProperties.STATE_LOCKED))
        {
            BooleanProperty prop = CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(adjSide);
            if (!adjState.getValue(prop))
            {
                return false;
            }
        }
        return super.attachsTo(adjState, sideSolid);
    }*/

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving)
    {
        updateCulling(level, pos);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder)
    {
        return getCamoDrops(super.getDrops(state, builder), builder);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx)
    {
        if (isIntangible(state, level, pos, ctx))
        {
            return Shapes.empty();
        }
        return super.getShape(state, level, pos, ctx);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx)
    {
        if (isIntangible(state, level, pos, null))
        {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, ctx);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos)
    {
        return getCamoShadeBrightness(state, level, pos, super.getShadeBrightness(state, level, pos));
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state)
    {
        return state.getValue(FramedProperties.PROPAGATES_SKYLIGHT);
    }

    @Override //The pane handles this through the SideSkipPredicate instead
    protected boolean skipRendering(BlockState state, BlockState adjacentState, Direction side)
    {
        return this == FBContent.BLOCK_FRAMED_BARS.value() && super.skipRendering(state, adjacentState, side);
    }

    @Override
    public BlockType getBlockType()
    {
        return type;
    }

    @Override
    @Nullable
    public BlockState getItemModelSource()
    {
        return null;
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return defaultBlockState().setValue(EAST, true).setValue(WEST, true);
    }
}
