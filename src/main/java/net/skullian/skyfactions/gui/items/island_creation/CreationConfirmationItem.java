package net.skullian.skyfactions.gui.items.island_creation;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;

public class CreationConfirmationItem extends SkyItem {

    public CreationConfirmationItem(ItemData data, ItemStack stack) {
        super(data, stack, null, null);
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        IslandAPI.createIsland((Player) event.getWhoClicked());
    }

}
