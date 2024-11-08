package net.skullian.skyfactions.gui.screens.obelisk;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.GUIAPI;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.event.PlayerHandler;
import net.skullian.skyfactions.gui.data.GUIData;
import net.skullian.skyfactions.gui.data.ItemData;
import net.skullian.skyfactions.gui.items.EmptyItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskHeadItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskInvitesItem;
import net.skullian.skyfactions.gui.items.obelisk.notification.ObeliskPlayerNotificationsItem;
import net.skullian.skyfactions.gui.items.obelisk.ObeliskRuneItem;
import net.skullian.skyfactions.gui.items.obelisk.defence.ObeliskDefencePurchaseItem;
import net.skullian.skyfactions.util.SoundUtil;
import net.skullian.skyfactions.util.text.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class PlayerObeliskUI {

    public static void promptPlayer(Player player) {
        Bukkit.getScheduler().runTask(SkyFactionsReborn.getInstance(), () -> {
            try {
                GUIData data = GUIAPI.getGUIData("obelisk/player_obelisk", player);
                Gui.Builder.Normal gui = registerItems(Gui.normal()
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
                Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "open your obelisk", "debug", "GUI_LOAD_EXCEPTION");
            }
        });
    }

    private static Gui.Builder.Normal registerItems(Gui.Builder.Normal builder, Player player) {
        try {
            List<ItemData> data = GUIAPI.getItemData("obelisk/player_obelisk", player);
            for (ItemData itemData : data) {
                switch (itemData.getITEM_ID()) {

                    case "HEAD":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskHeadItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player));
                        break;

                    case "DEFENCES":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskDefencePurchaseItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "player", null, player));
                        break;

                    case "RUNES_CONVERSION":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskRuneItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "player", player));
                        break;

                    case "INVITES":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskInvitesItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), "player", null, player));
                        break;

                    case "NOTIFICATIONS":
                        builder.addIngredient(itemData.getCHARACTER(), new ObeliskPlayerNotificationsItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player));
                        break;

                    case "BORDER":
                        builder.addIngredient(itemData.getCHARACTER(), new EmptyItem(itemData, GUIAPI.createItem(itemData, player.getUniqueId()), player));
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
