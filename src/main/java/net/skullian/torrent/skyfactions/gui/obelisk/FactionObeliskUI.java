package net.skullian.torrent.skyfactions.gui.obelisk;

import net.skullian.torrent.skyfactions.api.FactionAPI;
import net.skullian.torrent.skyfactions.api.GUIAPI;
import net.skullian.torrent.skyfactions.config.Messages;
import net.skullian.torrent.skyfactions.faction.Faction;
import net.skullian.torrent.skyfactions.faction.RankType;
import net.skullian.torrent.skyfactions.gui.data.GUIData;
import net.skullian.torrent.skyfactions.gui.data.ItemData;
import net.skullian.torrent.skyfactions.gui.items.GeneralBorderItem;
import net.skullian.torrent.skyfactions.gui.items.obelisk.*;
import net.skullian.torrent.skyfactions.util.SoundUtil;
import net.skullian.torrent.skyfactions.util.text.TextUtility;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.List;

public class FactionObeliskUI {

    public static void promptPlayer(Player player) {
        try {
            GUIData data = GUIAPI.getGUIData("obelisk/faction_obelisk");
            Gui.Builder.Normal gui = registerItems(Gui.normal()
                    .setStructure(data.getLAYOUT()), player);

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle(TextUtility.color(data.getTITLE()))
                    .setGui(gui)
                    .build();

            SoundUtil.playSound(player, data.getOPEN_SOUND(), data.getOPEN_PITCH(), 1f);
            window.open();
        } catch (IllegalArgumentException error) {
            error.printStackTrace();
            Messages.ERROR.send(player, "%operation%", "open your obelisk", "%debug%", "GUI_LOAD_EXCEPTION");
        }
    }

    private static Gui.Builder.Normal registerItems(Gui.Builder.Normal builder, Player player) {
        try {
            List<ItemData> data = GUIAPI.getItemData("obelisk/faction_obelisk", player);
            Faction faction = FactionAPI.getFaction(player);
            if (faction == null) {
                Messages.ERROR.send(player, "%operation%", "open your obelisk", "%debug%", "FACTION_NOT_FOUND");
                return null;
            }
            for (ItemData itemData : data) {
                switch (itemData.getITEM_ID()) {

                    case "FACTION":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskFactionOverviewItem(itemData, GUIAPI.createItem(itemData, player), player));
                        break;

                    case "DEFENCES":
                        // todo probably will need to add type for when actually functioning
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskDefencesItem(itemData, GUIAPI.createItem(itemData, player)));
                        break;

                    case "RUNES_CONVERSION":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskRuneItem(itemData, GUIAPI.createItem(itemData, player), "faction", player));
                        break;

                    case "MEMBER_MANAGEMENT":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskMemberManagementItem(itemData, GUIAPI.createItem(itemData, player)));
                        break;

                    case "AUDIT_LOGS":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskAuditLogItem(itemData, GUIAPI.createItem(itemData, player)));
                        break;

                    case "INVITES":
                        if (faction.isOwner(player) || faction.isAdmin(player) || faction.isModerator(player)) {
                            builder.addIngredient(itemData.getCHARACTER(), new ObeliskInvitesItem(itemData, GUIAPI.createItem(itemData, player), "faction"));
                        } else {
                            Messages.OBELISK_GUI_DENY.send(player, "%rank%", Messages.FACTION_MODERATOR_TITLE.get());
                        }
                        break;

                    case "BORDER":
                        builder.addIngredient(itemData.getCHARACTER(), new GeneralBorderItem(itemData, GUIAPI.createItem(itemData, player)));
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