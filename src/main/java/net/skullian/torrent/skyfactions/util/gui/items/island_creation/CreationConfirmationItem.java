package net.skullian.torrent.skyfactions.util.gui.items.island_creation;


import net.skullian.torrent.skyfactions.api.IslandAPI;
import net.skullian.torrent.skyfactions.util.SoundUtil;
import net.skullian.torrent.skyfactions.util.gui.data.ItemData;
import net.skullian.torrent.skyfactions.util.text.TextUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public class CreationConfirmationItem extends AbstractItem {

    private String NAME;
    private String SOUND;
    private int PITCH;
    private List<String> LORE;
    public ItemStack STACK;

    public CreationConfirmationItem(ItemData data, ItemStack stack) {
        this.NAME = data.getNAME();
        this.SOUND = data.getSOUND();
        this.PITCH = data.getPITCH();
        this.LORE = data.getLORE();
        this.STACK = stack;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(STACK)
                .setDisplayName(TextUtility.color(NAME));

        for (String loreLine : LORE) {
            builder.addLoreLines(TextUtility.color(loreLine));
        }

        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        event.getInventory().close();

        if (!SOUND.equalsIgnoreCase("none")) {
            SoundUtil.playSound(player, SOUND, PITCH, 1);
        }

        IslandAPI.createIsland((Player) event.getWhoClicked());
    }

}