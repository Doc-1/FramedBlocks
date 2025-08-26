package io.github.xfacthd.framedblocks.common.capability.item;

import io.github.xfacthd.framedblocks.common.blockentity.special.FramedChestBlockEntity;
import io.github.xfacthd.framedblocks.common.blockentity.special.FramedStorageBlockEntity;
import io.github.xfacthd.framedblocks.common.menu.FramedStorageMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StorageBlockItemStackHandler extends ItemStackHandler implements IStorageBlockItemHandler
{
    @Nullable
    private final FramedStorageBlockEntity be;

    public StorageBlockItemStackHandler(@Nullable FramedStorageBlockEntity be, int slots)
    {
        super(slots);
        this.be = be;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        if (be != null)
        {
            be.setChanged();
        }
    }

    @Override
    public FramedStorageMenu createMenu(int windowId, Inventory inv)
    {
        return FramedStorageMenu.createSingle(windowId, inv, this);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return be != null && be.isUsableByPlayer(player);
    }

    @Override
    public void open()
    {
        if (be instanceof FramedChestBlockEntity chest)
        {
            chest.doOpen();
        }
    }

    @Override
    public void close()
    {
        if (be instanceof FramedChestBlockEntity chest)
        {
            chest.close();
        }
    }

    public List<ItemStack> getBackingList()
    {
        return stacks;
    }
}
