package net.skullian.skyfactions.gui.screens.obelisk.invites;

import lombok.Builder;
import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.api.GUIAPI;
import net.skullian.skyfactions.config.types.GUIEnums;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.database.struct.InviteData;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.data.PaginationItemData;
import net.skullian.skyfactions.gui.items.EmptyItem;
import net.skullian.skyfactions.gui.items.PaginationBackItem;
import net.skullian.skyfactions.gui.items.PaginationForwardItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskBackItem;
import net.skullian.skyfactions.gui.items.obelisk.invites.OutgoingInvitePaginationItem;
import net.skullian.skyfactions.gui.screens.PaginatedScreen;
import net.skullian.skyfactions.util.ErrorUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.List;

public class OutgoingInvitesUI extends PaginatedScreen {
    private final List<InviteData> inviteData;

    @Builder
    public OutgoingInvitesUI(Player player, List<InviteData> inviteData) {
        super(player, GUIEnums.OBELISK_INVITE_OUTGOING_GUI.getPath());
        this.inviteData = inviteData;

        initWindow();
    }

    public static void promptPlayer(Player player) {
        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, exc) -> {
            if (exc != null) {
                ErrorUtil.handleError(player, "open the outgoing invites GUI", "GUI_LOAD_EXCEPTION", exc);
                return;
            }

            if (faction == null) {
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "open the outgoing invutes GUI", "debug", "FACTION_NOT_FOUND");
                return;
            }

            List<InviteData> outgoingInvites = faction.getOutgoingInvites();
            try {
                OutgoingInvitesUI.builder().player(player).inviteData(outgoingInvites).build().show();
            } catch (IllegalArgumentException error) {
                error.printStackTrace();
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "open the outgoing invites GUI", "debug", "GUI_LOAD_EXCEPTION");
            }
        });
    }

    @Nullable
    @Override
    protected Item handleItem(@NotNull ItemData itemData) {
        return switch (itemData.getITEM_ID()) {
            case "PROMPT", "BORDER" ->
                    new EmptyItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player);
            case "BACK" ->
                    new ObeliskBackItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "faction", player);
            default -> null;
        };
    }

    @Nullable
    @Override
    protected Item handlePaginationItem(@NotNull PaginationItemData paginationItem) {
        return switch (paginationItem.getITEM_ID()) {
            case "FORWARD_BUTTON" ->
                    new PaginationForwardItem(paginationItem, GUIAPI.createItem(paginationItem, player.getUniqueId()));
            case "BACK_BUTTON" ->
                    new PaginationBackItem(paginationItem, GUIAPI.createItem(paginationItem, player.getUniqueId()));
            default -> null;
        };
    }

    @NotNull
    @Override
    protected List<Item> getModels(Player player, ItemData itemData) {
        List<Item> items = new ArrayList<>();
        for (InviteData data : inviteData) {
            itemData.setNAME(itemData.getNAME().replace("player_name", data.getPlayer().getName()));
            items.add(new OutgoingInvitePaginationItem(itemData, GUIAPI.createItem(itemData, data.getPlayer().getUniqueId()), data, player));
        }

        return items;
    }
}
