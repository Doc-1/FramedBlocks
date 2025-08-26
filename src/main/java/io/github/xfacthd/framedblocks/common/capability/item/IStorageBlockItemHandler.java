package io.github.xfacthd.framedblocks.common.capability.item;

import io.github.xfacthd.framedblocks.common.menu.FramedStorageMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public interface IStorageBlockItemHandler extends IItemHandlerModifiable
{
    FramedStorageMenu createMenu(int windowId, Inventory inv);

    boolean stillValid(Player player);

    void open();

    void close();
}
