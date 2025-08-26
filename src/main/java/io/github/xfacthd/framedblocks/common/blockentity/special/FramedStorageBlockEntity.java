package io.github.xfacthd.framedblocks.common.blockentity.special;

import io.github.xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import io.github.xfacthd.framedblocks.api.util.Utils;
import io.github.xfacthd.framedblocks.common.FBContent;
import io.github.xfacthd.framedblocks.common.capability.item.IStorageBlockItemHandler;
import io.github.xfacthd.framedblocks.common.capability.item.StorageBlockItemStackHandler;
import io.github.xfacthd.framedblocks.common.menu.FramedStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class FramedStorageBlockEntity extends FramedBlockEntity implements MenuProvider, Nameable, Clearable
{
    public static final Component TITLE = Utils.translate("title", "framed_secret_storage");
    public static final int SLOTS = 9 * 3;
    public static final String INVENTORY_NBT_KEY = "inventory";

    private final StorageBlockItemStackHandler itemHandler = createItemHandler(this, false);
    @Nullable
    private Component customName = null;

    public FramedStorageBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_SECRET_STORAGE.value(), pos, state);
    }

    protected FramedStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public void open(ServerPlayer player)
    {
        player.openMenu(this);
    }

    public boolean isUsableByPlayer(Player player)
    {
        if (level().getBlockEntity(worldPosition) != this)
        {
            return false;
        }
        return !(player.distanceToSqr((double)worldPosition.getX() + 0.5D, (double)worldPosition.getY() + 0.5D, (double)worldPosition.getZ() + 0.5D) > 64.0D);
    }

    @Override
    public void clearContent()
    {
        Utils.clearItemHandler(itemHandler);
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

    public int getAnalogOutputSignal()
    {
        return getAnalogOutputSignal(itemHandler);
    }

    protected static int getAnalogOutputSignal(IStorageBlockItemHandler itemHandler)
    {
        int stacks = 0;
        float fullness = 0;

        for(int i = 0; i < itemHandler.getSlots(); ++i)
        {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                float sizeLimit = Math.min(itemHandler.getSlotLimit(i), stack.getMaxStackSize());
                fullness += (float)stack.getCount() / sizeLimit;

                stacks++;
            }
        }

        fullness /= (float)itemHandler.getSlots();
        return Mth.floor(fullness * 14F) + (stacks > 0 ? 1 : 0);
    }

    public IStorageBlockItemHandler getItemHandler()
    {
        return itemHandler;
    }

    public void setCustomName(Component customName)
    {
        this.customName = customName;
        setChangedWithoutSignalUpdate();
    }

    @Override
    public Component getName()
    {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    @Nullable
    public Component getCustomName()
    {
        return customName;
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput)
    {
        itemHandler.serialize(valueOutput.child(INVENTORY_NBT_KEY));
        valueOutput.storeNullable("custom_name", ComponentSerialization.CODEC, customName);
        super.saveAdditional(valueOutput);
    }

    @Override
    public void loadAdditional(ValueInput valueInput)
    {
        super.loadAdditional(valueInput);
        itemHandler.deserialize(valueInput.childOrEmpty(INVENTORY_NBT_KEY));
        customName = parseCustomNameSafe(valueInput, "custom_name");
    }

    protected Component getDefaultName()
    {
        return TITLE;
    }

    @Override
    public Component getDisplayName()
    {
        return getName();
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player)
    {
        return FramedStorageMenu.createSingle(windowId, inv, itemHandler);
    }



    public static StorageBlockItemStackHandler createItemHandler(@Nullable FramedStorageBlockEntity be, boolean doubleChest)
    {
        return new StorageBlockItemStackHandler(be, SLOTS * (doubleChest ? 2 : 1));
    }
}
