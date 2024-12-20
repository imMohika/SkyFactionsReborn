package net.skullian.skyfactions.gui.items.obelisk.invites;

import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.api.InvitesAPI;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.faction.JoinRequestData;
import net.skullian.skyfactions.util.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.impl.SkyItem;
import net.skullian.skyfactions.gui.screens.obelisk.invites.JoinRequestsUI;
import net.skullian.skyfactions.gui.screens.obelisk.invites.PlayerOutgoingRequestManageUI;
import net.skullian.skyfactions.util.ErrorUtil;

public class JoinRequestsTypeItem extends SkyItem {

    private String TYPE;

    public JoinRequestsTypeItem(ItemData data, ItemStack stack, String type, Player player) {
        super(data, stack, player, null);
        
        this.TYPE = type;
    }

    @Override
    public void onClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (TYPE.equals("faction")) {
            JoinRequestsUI.promptPlayer(player);
        } else if (TYPE.equals("player")) {
            InvitesAPI.getPlayerJoinRequest(player.getUniqueId()).whenComplete((joinRequest, ex) -> {
                if (ex != null) {
                    ErrorUtil.handleError(player, "get your outgoing join request", "SQL_JOIN_REQUEST_GET", ex);
                    return;
                } else if (joinRequest == null) {
                    SoundUtil.playSound(player, Settings.ERROR_SOUND.getString(), Settings.ERROR_SOUND_PITCH.getInt(), 1);
                    Messages.FACTION_JOIN_REQUEST_NOT_EXIST.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                } else {
                    PlayerOutgoingRequestManageUI.promptPlayer(player, joinRequest);
                }
            });
        }
    }

}
