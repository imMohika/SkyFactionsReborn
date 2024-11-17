package net.skullian.skyfactions.gui.items.obelisk.invites;

import java.util.concurrent.CompletableFuture;

import net.skullian.skyfactions.event.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.api.NotificationAPI;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.faction.JoinRequestData;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.util.ErrorUtil;

public class FactionPlayerJoinRequestConfirmItem extends SkyItem {
    private JoinRequestData DATA;

    public FactionPlayerJoinRequestConfirmItem(ItemData data, ItemStack stack, JoinRequestData joinRequestData, Player player) {
        super(data, stack, player, null);
        
        this.DATA = joinRequestData;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.getInventory().close();

        FactionAPI.getFaction(DATA.getFactionName()).whenComplete((faction, ex) -> {
            if (faction == null) {
                Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "get your Faction", "FACTION_NOT_FOUND");
                return;
            } else if (ex != null) {
                ErrorUtil.handleError(player, "get your Faction", "SQL_FACTION_GET", ex);
                return;
            }

            CompletableFuture.allOf(
                    SkyFactionsReborn.getDatabaseManager().getFactionInvitesManager().revokeInvite(player.getUniqueId(), DATA.getFactionName(), "incoming")
            ).whenComplete((ignored, exc) -> {
                if (exc != null) {
                    ErrorUtil.handleError(player, "accept a join request", "SQL_FACTION_GET", exc);
                    return;
                }
                faction.addFactionMember(player);
                Messages.PLAYER_FACTION_JOIN_SUCCESS.send(player, PlayerHandler.getLocale(player.getUniqueId()), "faction_name", DATA.getFactionName());
                NotificationAPI.factionInviteStore.replace(faction.getName(), (NotificationAPI.factionInviteStore.get(faction.getName()) - 1));
            });
        });
    }

}
