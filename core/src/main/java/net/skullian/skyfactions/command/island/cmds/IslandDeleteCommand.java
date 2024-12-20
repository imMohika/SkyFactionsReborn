package net.skullian.skyfactions.command.island.cmds;

import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.util.ErrorUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
            Player player,
            @Argument(value = "confirm") @Nullable String confirm
    ) {
        if (!CommandsUtility.hasPerm(player, permission(), true)) return;

        IslandAPI.hasIsland(player.getUniqueId()).whenComplete((hasIsland, ex) -> {
            if (ex != null) {
                ErrorUtil.handleError(player, "check if you have an island", "SQL_ISLAND_CHECK", ex);
                return;
            }

            if (confirm == null) {
                Messages.DELETION_CONFIRM.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                IslandAPI.awaitingDeletion.add(player.getUniqueId());
            } else if (confirm.equalsIgnoreCase("confirm")) {

                if (IslandAPI.awaitingDeletion.contains(player.getUniqueId())) {
                    World hubWorld = Bukkit.getWorld(Settings.HUB_WORLD_NAME.getString());
                    // todo migrate to gui command conf
                    if (hubWorld != null) {
                        Messages.DELETION_PROCESSING.send(player, PlayerAPI.getLocale(player.getUniqueId()));

                        IslandAPI.onIslandRemove(player);
                    } else {
                        Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "delete your island", "debug", "WORLD_NOT_EXIST");
                    }
                } else {
                    Messages.DELETION_BLOCK.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                }
            } else {
                Messages.INCORRECT_USAGE.send(player, PlayerAPI.getLocale(player.getUniqueId()), "usage", getSyntax());
            }
        });
    }

    public static List<String> permissions = List.of("skyfactions.island.delete", "skyfactions.island");

    @Override
    public List<String> permission() {
        return permissions;
    }
}
