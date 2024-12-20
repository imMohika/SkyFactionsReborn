package net.skullian.skyfactions.command.faction.cmds;

import net.skullian.skyfactions.api.FactionAPI;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.api.PlayerAPI;
import net.skullian.skyfactions.util.ErrorUtil;
import net.skullian.skyfactions.util.text.TextUtility;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

import java.util.List;

@Command("faction")
public class FactionMOTDCommand extends CommandTemplate {
    @Override
    public String getName() {
        return "motd";
    }

    @Override
    public String getDescription() {
        return "Set your faction's MOTD (Message of the Day).";
    }

    @Override
    public String getSyntax() {
        return "/faction motd <motd>";
    }

    @Command("motd <motd>")
    @Permission(value = {"skyfactions.faction.motd", "skyfactions.faction"}, mode = Permission.Mode.ANY_OF)
    public void perform(
            Player player,
            @Argument(value = "motd") @Greedy String motd
    ) {
        if (!CommandsUtility.hasPerm(player, permission(), true)) return;

        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, ex) -> {
            if (ex != null) {
                ErrorUtil.handleError(player, "get your Faction", "SQL_FACTION_GET", ex);
            }

            if (faction == null) {
                Messages.NOT_IN_FACTION.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                return;
            } else if (!Settings.FACTION_MODIFY_MOTD_PERMISSIONS.getList().contains(faction.getRankType(player.getUniqueId()).getRankValue())) {
                Messages.PERMISSION_DENY.send(player, PlayerAPI.getLocale(player.getUniqueId()));
                return;
            }

            Messages.MOTD_CHANGE_PROCESSING.send(player, PlayerAPI.getLocale(player.getUniqueId()));

            if (!TextUtility.hasBlacklistedWords(player, motd)) {
                faction.updateMOTD(motd, player);
                Messages.MOTD_CHANGE_SUCCESS.send(player, PlayerAPI.getLocale(player.getUniqueId()));
            }
        });
    }

    public static List<String> permissions = List.of("skyfactions.faction.motd", "skyfactions.faction");

    @Override
    public List<String> permission() {
        return permissions;
    }
}
