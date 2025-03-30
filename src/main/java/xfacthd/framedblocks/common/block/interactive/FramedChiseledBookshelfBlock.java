package xfacthd.framedblocks.common.block.interactive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.BlockUtils;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.block.PlacementStateBuilder;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.block.FramedBlock;
import xfacthd.framedblocks.common.blockentity.special.FramedChiseledBookshelfBlockEntity;
import xfacthd.framedblocks.common.data.BlockType;

import java.util.OptionalInt;

public class FramedChiseledBookshelfBlock extends FramedBlock
{
    public FramedChiseledBookshelfBlock(Properties props)
    {
        super(BlockType.FRAMED_CHISELED_BOOKSHELF, props);
        BlockState state = defaultBlockState();
        for (BooleanProperty prop : ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES)
        {
            state = state.setValue(prop, false);
        }
        registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FramedProperties.FACING_HOR, FramedProperties.SOLID);
        ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.forEach(builder::add);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return PlacementStateBuilder.of(this, ctx).withHorizontalFacing(true).build();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);

        //noinspection ConstantConditions
        if (level.isClientSide() || stack.get(DataComponents.BLOCK_ENTITY_DATA) == null)
        {
            return;
        }

        if (level.getBlockEntity(pos) instanceof FramedChiseledBookshelfBlockEntity be)
        {
            be.forceStateUpdate();
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        InteractionResult result = super.useItemOn(stack, state, level, pos, player, hand, hit);
        if (result.consumesAction()) return result;

        if (level.getBlockEntity(pos) instanceof FramedChiseledBookshelfBlockEntity be)
        {
            if (!stack.is(ItemTags.BOOKSHELF_BOOKS))
            {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            OptionalInt slot = ((ChiseledBookShelfBlock) Blocks.CHISELED_BOOKSHELF).getHitSlot(hit, state);
            if (slot.isEmpty())
            {
                return InteractionResult.PASS;
            }
            if (state.getValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(slot.getAsInt())))
            {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            placeBook(level, pos, player, be, stack, slot.getAsInt());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (level.getBlockEntity(pos) instanceof FramedChiseledBookshelfBlockEntity be)
        {
            OptionalInt slot = ((ChiseledBookShelfBlock) Blocks.CHISELED_BOOKSHELF).getHitSlot(hit, state);
            if (slot.isEmpty())
            {
                return InteractionResult.PASS;
            }
            if (!state.getValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(slot.getAsInt())))
            {
                return InteractionResult.CONSUME;
            }

            takeBook(level, pos, player, be, slot.getAsInt());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static void placeBook(
            Level level, BlockPos pos, Player player, FramedChiseledBookshelfBlockEntity be, ItemStack stack, int slot
    )
    {
        if (level.isClientSide())
        {
            return;
        }

        be.placeBook(stack.split(1), slot);
        if (player.isCreative())
        {
            stack.grow(1);
        }

        SoundEvent sound = stack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1F, 1F);

        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    private static void takeBook(Level level, BlockPos pos, Player player, FramedChiseledBookshelfBlockEntity be, int slot)
    {
        if (level.isClientSide())
        {
            return;
        }

        ItemStack stack = be.takeBook(slot);
        Utils.giveToPlayer(player, stack, true);

        SoundEvent sound = stack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving)
    {
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof FramedChiseledBookshelfBlockEntity be)
        {
            return be.getAnalogOutputSignal();
        }
        return 0;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot)
    {
        Direction dir = state.getValue(FramedProperties.FACING_HOR);
        return state.setValue(FramedProperties.FACING_HOR, rot.rotate(dir));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return BlockUtils.mirrorFaceBlock(state, mirror);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedChiseledBookshelfBlockEntity(pos, state);
    }

    @Override
    public BlockState getItemModelSource()
    {
        return defaultBlockState();
    }

    @Override
    public BlockState getJadeRenderState(BlockState state)
    {
        return state.setValue(FramedProperties.FACING_HOR, Direction.NORTH);
    }
}
