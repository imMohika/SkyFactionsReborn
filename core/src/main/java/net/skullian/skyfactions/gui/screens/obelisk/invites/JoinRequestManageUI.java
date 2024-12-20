package net.skullian.skyfactions.gui.screens.obelisk.invites;

import lombok.Builder;
import net.skullian.skyfactions.api.GUIAPI;
import net.skullian.skyfactions.config.types.GUIEnums;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.database.struct.InviteData;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.EmptyItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskBackItem;
import net.skullian.skyfactions.gui.items.obelisk.invites.FactionJoinRequestAcceptItem;
import net.skullian.skyfactions.gui.items.obelisk.invites.FactionJoinRequestRejectItem;
import net.skullian.skyfactions.gui.screens.Screen;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.item.Item;

public class JoinRequestManageUI extends Screen {
    private final InviteData inviteData;

    @Builder
    public JoinRequestManageUI(Player player, InviteData inviteData) {
        super(player, GUIEnums.OBELISK_JOIN_REQUEST_MANAGE_GUI.getPath());
        this.inviteData = inviteData;

        initWindow();
    }

    public static void promptPlayer(Player player, InviteData inviteData) {
        try {
            JoinRequestManageUI.builder().player(player).inviteData(inviteData).build().show();
        } catch (IllegalArgumentException error) {
            error.printStackTrace();
            Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "open the join request manage GUI", "debug", "GUI_LOAD_EXCEPTION");
        }
    }

    @Nullable
    @Override
    protected Item handleItem(@NotNull ItemData itemData) {
        return switch (itemData.getITEM_ID()) {
            case "PROMPT", "BORDER" ->
                    new EmptyItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player);
            case "BACK" ->
                    new ObeliskBackItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "faction", player);
            case "REJECT" ->
                    new FactionJoinRequestRejectItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), inviteData, player);
            case "ACCEPT" ->
                    new FactionJoinRequestAcceptItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), inviteData, player);
            default -> null;
        };
    }
}
