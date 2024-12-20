package net.skullian.skyfactions.gui.items.obelisk.election;

import net.skullian.skyfactions.faction.Faction;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.AsyncSkyItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ObeliskFactionElectionMenuItem extends AsyncSkyItem {
    private Faction FACTION;

    public ObeliskFactionElectionMenuItem(ItemData data, ItemStack stack, Faction faction, Player player) {
        super(data, stack, player, null);
        this.FACTION = faction;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (FACTION.isElectionRunning()) {
            player.sendMessage("There's no running elections right now");
            return;
        }
    }
}
