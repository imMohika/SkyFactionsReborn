package net.skullian.torrent.skyfactions.util.gui;

import net.skullian.torrent.skyfactions.api.GUIAPI;
import net.skullian.torrent.skyfactions.config.Messages;
import net.skullian.torrent.skyfactions.util.gui.items.*;
import net.skullian.torrent.skyfactions.util.text.TextUtility;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.List;

public class PlayerRaidConfirmationUI {

    public static void promptPlayer(Player player) {
        try {
            GUIData data = GUIAPI.getGUIData("confirmations/start_raid");
            Gui.Builder.Normal gui = registerItems(Gui.normal()
                    .setStructure(data.getLAYOUT()));


            Window window = Window.single()
                    .setViewer(player)
                    .setTitle(TextUtility.color(data.getTITLE()))
                    .setGui(gui)
                    .build();

            window.open();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Messages.ERROR.send(player, "%operation%", "start a raid", "%debug%", "GUI_LOAD_EXCEPTION");
        }
    }

    private static Gui.Builder.Normal registerItems(Gui.Builder.Normal builder) {
        try {
            List<ItemData> data = GUIAPI.getItemData("confirmations/create_island");
            for (ItemData itemData : data) {
                switch (itemData.getITEM_ID()) {

                    case "CANCEL":
                        builder.addIngredient(itemData.getCHARACTER(), new RaidCancelItem(itemData, GUIAPI.createItem(itemData)));
                        break;
                    case "CONFIRM":
                        builder.addIngredient(itemData.getCHARACTER(), new RaidConfirmationItem(itemData, GUIAPI.createItem(itemData)));
                        break;
                    case "PROMPT":
                        builder.addIngredient(itemData.getCHARACTER(), new RaidPromptItem(itemData, GUIAPI.createItem(itemData)));
                        break;
                    case "BORDER":
                        builder.addIngredient(itemData.getCHARACTER(), new GeneralBorderItem(itemData, GUIAPI.createItem(itemData)));
                        break;
                }
            }

            return builder;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return builder;
    }
}
