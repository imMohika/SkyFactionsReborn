package net.skullian.torrent.skyfactions.gui.obelisk.member;

import net.skullian.torrent.skyfactions.api.FactionAPI;
import net.skullian.torrent.skyfactions.api.GUIAPI;
import net.skullian.torrent.skyfactions.config.Messages;
import net.skullian.torrent.skyfactions.faction.Faction;
import net.skullian.torrent.skyfactions.gui.data.GUIData;
import net.skullian.torrent.skyfactions.gui.data.ItemData;
import net.skullian.torrent.skyfactions.gui.items.GeneralBorderItem;
import net.skullian.torrent.skyfactions.gui.items.GeneralPromptItem;
import net.skullian.torrent.skyfactions.gui.items.obelisk.ObeliskBackItem;
import net.skullian.torrent.skyfactions.gui.items.obelisk.member_manage.MemberBanItem;
import net.skullian.torrent.skyfactions.gui.items.obelisk.member_manage.MemberKickItem;
import net.skullian.torrent.skyfactions.util.SoundUtil;
import net.skullian.torrent.skyfactions.util.text.TextUtility;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class ManageMemberUI {

    public static void promptPlayer(Player player, OfflinePlayer subject) {
        try {
            GUIData data = GUIAPI.getGUIData("obelisk/manage_member");
            Gui.Builder.Normal gui = registerItems(Gui.normal()
                    .setStructure(data.getLAYOUT()), subject, player);

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle(TextUtility.color(data.getTITLE()))
                    .setGui(gui)
                    .build();

            SoundUtil.playSound(player, data.getOPEN_SOUND(), data.getOPEN_PITCH(), 1f);
            window.open();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Messages.ERROR.send(player, "%operation%", "manage a member", "%debug%", "GUI_LOAD_EXCEPTION");
        }
    }

    private static Gui.Builder.Normal registerItems(Gui.Builder.Normal builder, OfflinePlayer player, Player actor) {
        try {
            Faction faction = FactionAPI.getFaction(actor);
            if (faction == null) {
                Messages.ERROR.send(actor, "%operation%", "manage a member", "%debug%", "FACTION_NOT_FOUND");
            }

            List<ItemData> data = GUIAPI.getItemData("obelisk/manage_member", player.getPlayer());
            for (ItemData itemData : data) {
                switch (itemData.getITEM_ID()) {

                    case "BORDER":
                        builder.addIngredient(itemData.getCHARACTER(), new GeneralBorderItem(itemData, GUIAPI.createItem(itemData, player)));
                        break;

                    case "PLAYER_HEAD":
                        builder.addIngredient(itemData.getCHARACTER(), new GeneralPromptItem(itemData, GUIAPI.createItem(itemData, player)));
                        break;

                    case "KICK":
                        builder.addIngredient(itemData.getCHARACTER(), new MemberKickItem(itemData, GUIAPI.createItem(itemData, player), player));
                        break;

                    case "BAN":
                        if (faction.isOwner(actor) || faction.isAdmin(actor)) {
                            builder.addIngredient(itemData.getCHARACTER(), new MemberBanItem(itemData, GUIAPI.createItem(itemData, player), player));
                        }
                        break;

                    case "BACK":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskBackItem(itemData, GUIAPI.createItem(itemData, player), "faction"));
                        break;
                }
            }

            return builder;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return builder;
    }
}