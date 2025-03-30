package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.blockentity.FramedBlockEntity;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.capability.item.IStorageBlockItemHandler;
import xfacthd.framedblocks.common.capability.item.StorageBlockItemStackHandler;
import xfacthd.framedblocks.common.menu.FramedStorageMenu;

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
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        nbt.put(INVENTORY_NBT_KEY, itemHandler.serializeNBT(provider));
        if (customName != null)
        {
            nbt.putString("custom_name", Component.Serializer.toJson(customName, provider));
        }
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        super.loadAdditional(nbt, registries);
        itemHandler.deserializeNBT(registries, nbt.getCompoundOrEmpty(INVENTORY_NBT_KEY));
        customName = parseCustomNameSafe(nbt.get("custom_name"), registries);
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
