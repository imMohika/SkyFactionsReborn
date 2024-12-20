package net.skullian.skyfactions.command.island.cmds;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.util.ErrorUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;

import java.util.List;
import java.util.stream.Collectors;

@Command("island")
public class IslandUntrustCommand extends CommandTemplate {
    @Override
    public String getName() {
        return "untrust";
    }

    @Override
    public String getDescription() {
        return "Untrust a player, so they can not longer visit";
    }

    @Override
    public String getSyntax() {
        return "/untrust <player name>";
    }

    @Suggestions("onlinePlayers")
    public List<String> suggestPlayers(CommandContext<CommandSourceStack> context, CommandInput input) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Command("untrust <target>")
    @Permission(value = {"skyfactions.island.untrust", "skyfactions.island"}, mode = Permission.Mode.ANY_OF)
    public void perform(
            Player player,
            @Argument(value = "target", suggestions = "onlinePlayers") String playerName
    ) {
        if (!CommandsUtility.hasPerm(player, permission(), true)) return;
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

        PlayerAPI.isPlayerRegistered(target.getUniqueId()).whenComplete((isRegistered, err) -> {
            if (err != null) {
                ErrorUtil.handleError(player, "check if that player is registered", "SQL_PLAYER_GET", err);
                return;
            } else if (!isRegistered) {
                Messages.UNKNOWN_PLAYER.send(player, PlayerAPI.getLocale(player.getUniqueId()), "player", playerName);
                return;
            }

            IslandAPI.getPlayerIsland(player.getUniqueId()).whenComplete((is, ex) -> {
                if (ex != null) {
                    ErrorUtil.handleError(player, "get your island", "SQL_ISLAND_GET", ex);
                    return;
                } else if (is == null) {
                    Messages.NO_ISLAND.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                    return;
                }

                SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().isPlayerTrusted(target.getUniqueId(), is.getId()).whenComplete((isTrusted, throwable) -> {
                    if (throwable != null) {
                        ErrorUtil.handleError(player, "check if a player is trusted", "SQL_TRUST_GET", throwable);
                        return;
                    }

                    if (isTrusted) {
                        SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().removePlayerTrust(target.getUniqueId(), is.getId()).whenComplete((ignored, exc) -> {
                            if (exc != null) {
                                ErrorUtil.handleError(player, "untrust a player", "SQL_TRUST_REMOVE", exc);
                                return;
                            }

                            Messages.UNTRUST_SUCCESS.send(player, PlayerAPI.getLocale(player.getUniqueId()), "player", target.getName());
                        });
                    } else {
                        Messages.UNTRUST_FAILURE.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                    }
                });
            });
        });
    }

    public static List<String> permissions = List.of("skyfactions.island.untrust", "skyfactions.island");

    @Override
    public List<String> permission() {
        return permissions;
    }
}
