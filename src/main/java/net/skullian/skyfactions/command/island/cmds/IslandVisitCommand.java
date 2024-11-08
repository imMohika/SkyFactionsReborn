package net.skullian.skyfactions.command.island.cmds;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.api.IslandAPI;
import net.skullian.skyfactions.api.RaidAPI;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.command.island.IslandCommandHandler;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.event.PlayerHandler;
import net.skullian.skyfactions.util.ErrorHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Command("island")
public class IslandVisitCommand extends CommandTemplate {

    IslandCommandHandler handler;

    public IslandVisitCommand(IslandCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "visit";
    }

    @Override
    public String getDescription() {
        return "Visit another player's island if you are trusted.";
    }

    @Override
    public String getSyntax() {
        return "/island visit <player>";
    }

    @Suggestions("onlinePlayers")
    public List<String> suggestPlayers(CommandContext<CommandSourceStack> context, CommandInput input) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Command("visit <player>")
    @Permission(value = {"skyfactions.island.visit", "skyfactions.island"}, mode = Permission.Mode.ANY_OF)
    public void perform(
            CommandSourceStack commandSourceStack,
            @Argument(value = "player", suggestions = "onlinePlayers") String playerName
    ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        CommandSender sender = commandSourceStack.getSender();
        if (!(sender instanceof Player player)) return;
        if (!CommandsUtility.hasPerm(player, permission(), true)) return;

        Messages.VISIT_PROCESSING.send(player, PlayerHandler.getLocale(player.getUniqueId()));
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

        if (!target.hasPlayedBefore()) {
            Messages.UNKNOWN_PLAYER.send(player, PlayerHandler.getLocale(player.getUniqueId()), "player", playerName);
            return;
        } else if (target.getUniqueId().equals(player.getUniqueId())) {
            CommandTemplate template = this.handler.getSubCommands().get("teleport");
            Method method = template.getClass().getDeclaredMethod("perform");

            method.invoke(template, commandSourceStack);
        }

        IslandAPI.getPlayerIsland(target.getUniqueId()).whenComplete((is, ex) -> {
            if (ex != null) {
                ErrorHandler.handleError(player, "get that player's island", "SQL_ISLAND_GET", ex);
                return;
            } else if (is == null) {
                Messages.VISIT_NO_ISLAND.send(player, PlayerHandler.getLocale(player.getUniqueId()));
                return;
            } else if (FactionAPI.isLocationInRegion(player.getLocation(), target.getUniqueId().toString())) {
                Messages.VISIT_ALREADY_ON_ISLAND.send(player, PlayerHandler.getLocale(player.getUniqueId()), "player", target.getName());
                return;
            }

            if ((RaidAPI.currentRaids.containsValue(player.getUniqueId()) || RaidAPI.processingRaid.containsValue(player.getUniqueId())) || (RaidAPI.currentRaids.containsValue(target.getUniqueId()) || RaidAPI.processingRaid.containsValue(target.getUniqueId()))) {
                Messages.VISIT_IN_RAID.send(player, PlayerHandler.getLocale(player.getUniqueId()));
            } else {
                SkyFactionsReborn.databaseHandler.isPlayerTrusted(player.getUniqueId(), is.getId()).whenComplete((isTrusted, throwable) -> {
                    if (throwable != null) {
                        ErrorHandler.handleError(player, "check if your are trusted", "SQL_TRUST_GET", throwable);
                        return;
                    }

                    World world = Bukkit.getWorld(Settings.ISLAND_PLAYER_WORLD.getString());
                    if (world == null) {
                        Messages.ERROR.send(player, PlayerHandler.getLocale(player.getUniqueId()), "operation", "visit a player", "debug", "WORLD_NOT_EXIST");
                    } else {
                        if (isTrusted) {
                            IslandAPI.modifyDefenceOperation(FactionAPI.DefenceOperation.DISABLE, player.getUniqueId());

                            IslandAPI.handlePlayerJoinBorder(player, is); // shift the worldborder
                            IslandAPI.teleportPlayerToLocation(player, is.getCenter(world));

                            IslandAPI.onIslandLoad(target.getUniqueId());
                        } else {
                            Messages.PLAYER_NOT_TRUSTED.send(player, PlayerHandler.getLocale(player.getUniqueId()));
                        }
                    }
                });
            }
        });
    }

    public static List<String> permissions = List.of("skyfactions.island.visit", "skyfactions.island");

    @Override
    public List<String> permission() {
        return permissions;
    }
}
