package net.skullian.skyfactions.gui.items.rune_submit;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.RunesAPI;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import xyz.xenondevs.invui.inventory.VirtualInventory;

public class RuneSubmitItem extends SkyItem {

    private VirtualInventory INVENTORY;
    private String TYPE;

    public RuneSubmitItem(ItemData data, ItemStack stack, String type, VirtualInventory inventory, Player player) {
        super(data, stack, player, null);
        this.INVENTORY = inventory;
        this.TYPE = type;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        List<ItemStack> stacks = Arrays.asList(INVENTORY.getItems());

        for (int i = 0; i < INVENTORY.getSize(); i++) {
            INVENTORY.setItemSilently(i, null);
        }

        player.closeInventory();
        player.removeMetadata("rune_ui", SkyFactionsReborn.getInstance());
        if (TYPE.equals("player")) {
            RunesAPI.handleRuneConversion(stacks, player);
        } else if (TYPE.equals("faction")) {
            RunesAPI.handleRuneFactionConversion(stacks, player);
        }
    }
}
