package net.skullian.skyfactions.gui.items.obelisk.invites;

import net.skullian.skyfactions.api.PlayerAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.database.struct.InviteData;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.util.ErrorUtil;

public class FactionJoinRequestRejectItem extends SkyItem {

    private InviteData DATA;

    public FactionJoinRequestRejectItem(ItemData data, ItemStack stack, InviteData inviteData, Player player) {
        super(data, stack, player, null);
        
        this.DATA = inviteData;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.getInventory().close();

        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, ex) -> {
            if (faction == null) {
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "get your Faction", "FACTION_NOT_FOUND");
                return;
            } else if (ex != null) {
                ErrorUtil.handleError(player, "get your Faction", "SQL_FACTION_GET", ex);
                return;
            }

            faction.rejectJoinRequest(DATA, player);

            Messages.FACTION_JOIN_REQUEST_REJECT_SUCCESS.send(player, PlayerAPI.getLocale(player.getUniqueId()), "player_name", DATA.getPlayer().getName());
        });
    }

}
