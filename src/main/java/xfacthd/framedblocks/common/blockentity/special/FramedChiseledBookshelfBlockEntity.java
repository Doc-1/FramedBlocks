package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;

public class FramedChiseledBookshelfBlockEntity extends FramedBlockEntity implements Clearable
{
    public static final String INVENTORY_NBT_KEY = ContainerHelper.TAG_ITEMS;
    public static final String LAST_SLOT_NBT_KEY = "last_slot";

    private final ItemStackHandler itemHandler = new ItemStackHandler(6);
    private int lastInteractedSlot = -1;

    public FramedChiseledBookshelfBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_CHISELED_BOOKSHELF.value(), pos, state);
    }

    public void placeBook(ItemStack stack, int slot)
    {
        itemHandler.setStackInSlot(slot, stack);
        updateState(slot);
        setChanged();
    }

    public ItemStack takeBook(int slot)
    {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        updateState(slot);
        setChanged();
        return stack;
    }

    private void updateState(int slot)
    {
        lastInteractedSlot = slot;

        BlockState state = getBlockState();
        for (int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++)
        {
            BooleanProperty prop = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
            state = state.setValue(prop, !itemHandler.getStackInSlot(i).isEmpty());
        }
        level().setBlockAndUpdate(worldPosition, state);
    }

    public void forceStateUpdate()
    {
        updateState(lastInteractedSlot);
    }

    public IItemHandler getItemHandler()
    {
        return itemHandler;
    }

    @Override
    public void clearContent()
    {
        Utils.clearItemHandler(itemHandler);
    }

    public int getAnalogOutputSignal()
    {
        return lastInteractedSlot + 1;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null)
        {
            Utils.dropItemHandlerContents(level, pos, itemHandler);
            clearContent();
        }
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        itemHandler.serialize(valueOutput.child(INVENTORY_NBT_KEY));
        valueOutput.putInt(LAST_SLOT_NBT_KEY, lastInteractedSlot);
        super.saveAdditional(valueOutput);
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        itemHandler.deserialize(valueInput.childOrEmpty(INVENTORY_NBT_KEY));
        lastInteractedSlot = valueInput.getIntOr(LAST_SLOT_NBT_KEY, -1);
    }
}
