package net.skullian.skyfactions.command.island.cmds;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.event.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Command("island")
public class IslandDeleteCommand extends CommandTemplate {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete your island permanently.";
    }

    @Override
    public String getSyntax() {
        return "/island delete <confirm>";
    }

    @Command("delete [confirm]")
    @Permission(value = {"skyfactions.island.delete", "skyfactions.island"}, mode = Permission.Mode.ANY_OF)
    public void perform(
            CommandSourceStack commandSourceStack,
            @Argument(value = "confirm") @Nullable String confirm
    ) {
        CommandSender sender = commandSourceStack.getSender();
        if (!(sender instanceof Player player)) return;
        if (!CommandsUtility.hasPerm(player, permission(), true)) return;
        SkyFactionsReborn.databaseHandler.hasIsland(player.getUniqueId()).thenAccept(has -> {
            if (!has) {
                Messages.NO_ISLAND.send(player, PlayerHandler.getLocale(player.getUniqueId()));
            } else {

                if (confirm == null) {
                    Messages.DELETION_CONFIRM.send(player, PlayerHandler.getLocale(player.getUniqueId()));
                    IslandAPI.awaitingDeletion.add(player.getUniqueId());
                } else if (confirm != null) {
                    if (confirm.equalsIgnoreCase("confirm")) {

                        if (IslandAPI.awaitingDeletion.contains(player.getUniqueId())) {
                            World hubWorld = Bukkit.getWorld(Settings.HUB_WORLD_NAME.getString());
                            // todo migrate to gui command conf
                            if (hubWorld != null) {
                                Messages.DELETION_PROCESSING.send(player, PlayerHandler.getLocale(player.getUniqueId()));

                                SkyFactionsReborn.worldBorderApi.resetWorldBorderToGlobal(player); // reset the world border
                                List<Integer> hubLocArray = Settings.HUB_LOCATION.getIntegerList();
                                Location location = new Location(hubWorld, hubLocArray.get(0), hubLocArray.get(1), hubLocArray.get(2));
                                IslandAPI.teleportPlayerToLocation(player, location);

                                IslandAPI.awaitingDeletion.remove(player.getUniqueId());
                                IslandAPI.removePlayerIsland(player);
                            } else {
                                Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "delete your island", "debug", "WORLD_NOT_EXIST");
                            }
                        } else {
                            Messages.DELETION_BLOCK.send(player, PlayerHandler.getLocale(player.getUniqueId()));
                        }
                    } else {
                        Messages.INCORRECT_USAGE.send(player, PlayerHandler.getLocale(player.getUniqueId()), "usage", getSyntax());
                    }
                }
            }
        });
    }

    public static List<String> permissions = List.of("skyfactions.island.delete", "skyfactions.island");

    @Override
    public List<String> permission() {
        return permissions;
    }
}
