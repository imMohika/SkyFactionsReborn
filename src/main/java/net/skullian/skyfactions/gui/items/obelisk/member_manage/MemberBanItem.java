package net.skullian.skyfactions.gui.items.obelisk.member_manage;

import net.skullian.skyfactions.event.PlayerHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.faction.AuditLogType;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.util.ErrorHandler;

public class MemberBanItem extends SkyItem {

    private OfflinePlayer SUBJECT;

    public MemberBanItem(ItemData data, ItemStack stack, OfflinePlayer player, Player viewer) {
        super(data, stack, viewer, null);

        this.SUBJECT = player;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, exc) -> {
            if (exc != null) {
                ErrorHandler.handleError(player, "ban a player", "SQL_FACTION_GET", exc);
                return;
            }

            if (faction != null) {
                if (faction.getAllMembers().contains(SUBJECT)) {
                    faction.createAuditLog(SUBJECT.getUniqueId(), AuditLogType.PLAYER_BAN, "banned", SUBJECT.getName(), "player", player.getName());
                    faction.banPlayer(SUBJECT, player);

                    Messages.FACTION_MANAGE_BAN_SUCCESS.send(player, PlayerHandler.getLocale(player.getUniqueId()), "player", SUBJECT.getName());
                } else {
                    Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "ban a player", "debug", "FACTION_MEMBER_UNKNOWN");
                    event.getInventory().close();
                }
            } else {
                Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "ban a player", "debug", "FACTION_NOT_EXIST");
                event.getInventory().close();
            }
        });

    }

}
