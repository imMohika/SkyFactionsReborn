package net.skullian.skyfactions.gui.screens.obelisk.member;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.api.GUIAPI;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.event.PlayerHandler;
import net.skullian.skyfactions.gui.data.GUIData;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.data.PaginationItemData;
import net.skullian.skyfactions.gui.items.EmptyItem;
import net.skullian.skyfactions.gui.items.PaginationBackItem;
import net.skullian.skyfactions.gui.items.PaginationForwardItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskBackItem;
import net.skullian.skyfactions.gui.items.obelisk.member_manage.MemberPaginationItem;
import net.skullian.skyfactions.util.ErrorHandler;
import net.skullian.skyfactions.util.SoundUtil;
import net.skullian.skyfactions.util.text.TextUtility;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

public class MemberManagementUI {

    public static void promptPlayer(Player player) {
        Bukkit.getScheduler().runTask(SkyFactionsReborn.getInstance(), () -> {
            try {
                GUIData data = GUIAPI.getGUIData("obelisk/member_management", player);
                PagedGui.Builder gui = registerItems(PagedGui.items()
                        .setStructure(data.getLAYOUT()), player);

                Window window = Window.single()
                        .setViewer(player)
                        .setTitle(TextUtility.legacyColor(data.getTITLE(), PlayerHandler.getLocale(player.getUniqueId()), player))
                        .setGui(gui)
                        .build();

                SoundUtil.playSound(player, data.getOPEN_SOUND(), data.getOPEN_PITCH(), 1f);
                window.open();
            } catch (IllegalArgumentException error) {
                error.printStackTrace();
                Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "open the member management GUI", "debug", "GUI_LOAD_EXCEPTION");
            }
        });
    }

    private static PagedGui.Builder registerItems(PagedGui.Builder builder, Player player) {
        try {
            builder.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
            List<ItemData> data = GUIAPI.getItemData("obelisk/member_management", player);
            List<PaginationItemData> paginationData = GUIAPI.getPaginationData(player);

            for (ItemData itemData : data) {
                switch (itemData.getITEM_ID()) {
                    case "PROMPT", "BORDER":
                        builder.addIngredient(itemData.getCHARACTER(), new EmptyItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player));
                        break;

                    case "MODEL":
                        builder.setContent(getItems(player, itemData));
                        break;

                    case "BACK":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskBackItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "faction", player));
                        break;
                }
            }
            for (PaginationItemData paginationItem : paginationData) {
                switch (paginationItem.getITEM_ID()) {

                    case "FORWARD_BUTTON":
                        builder.addIngredient(paginationItem.getCHARACTER(), new PaginationForwardItem(paginationItem, GUIAPI.createItem(paginationItem, player.getUniqueId())));
                        break;

                    case "BACK_BUTTON":
                        builder.addIngredient(paginationItem.getCHARACTER(), new PaginationBackItem(paginationItem, GUIAPI.createItem(paginationItem, player.getUniqueId())));
                        break;
                }
            }

            return builder;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return builder;
    }

    private static List<Item> getItems(Player player, ItemData data) {
        List<Item> items = new ArrayList<>();
        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, exc) -> {
            if (exc != null) {
                ErrorHandler.handleError(player, "open the member management GUI", "GUI_LOAD_EXCEPTION", exc);
                return;
            }
            String locale = PlayerHandler.getLocale(player.getUniqueId());

            OfflinePlayer owner = faction.getOwner();
            List<OfflinePlayer> admins = faction.getAdmins();
            List<OfflinePlayer> moderators = faction.getModerators();
            List<OfflinePlayer> fighters = faction.getFighters();
            List<OfflinePlayer> members = faction.getMembers();

            data.setNAME(data.getNAME().replace("player_name", owner.getName()));
            items.add(new MemberPaginationItem(data, GUIAPI.createItem(data, owner.getUniqueId()), Messages.FACTION_OWNER_TITLE.getString(locale), owner, player));

            for (OfflinePlayer admin : admins) {
                data.setNAME(data.getNAME().replace("player_name", admin.getName()));
                items.add(new MemberPaginationItem(data, GUIAPI.createItem(data, admin.getUniqueId()), Messages.FACTION_ADMIN_TITLE.getString(locale), admin, player));
            }
            for (OfflinePlayer moderator : moderators) {
                data.setNAME(data.getNAME().replace("player_name", moderator.getName()));
                items.add(new MemberPaginationItem(data, GUIAPI.createItem(data, moderator.getUniqueId()), Messages.FACTION_MODERATOR_TITLE.getString(locale), moderator, player));
            }
            for (OfflinePlayer fighter : fighters) {
                data.setNAME(data.getNAME().replace("player_name", fighter.getName()));
                items.add(new MemberPaginationItem(data, GUIAPI.createItem(data, fighter.getUniqueId()), Messages.FACTION_FIGHTER_TITLE.getString(locale), fighter, player));
            }
            for (OfflinePlayer member : members) {
                data.setNAME(data.getNAME().replace("player_name", member.getName()));
                items.add(new MemberPaginationItem(data, GUIAPI.createItem(data, member.getUniqueId()), Messages.FACTION_MEMBER_TITLE.getString(locale), member, player));
            }
        });
        return items;
    }
}
