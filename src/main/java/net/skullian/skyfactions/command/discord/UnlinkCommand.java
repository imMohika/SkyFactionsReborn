package net.skullian.skyfactions.command.discord;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.command.CommandTemplate;
import net.skullian.skyfactions.command.CommandsUtility;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.util.ErrorHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;

import java.util.List;

public class UnlinkCommand extends CommandTemplate {
    @Command("unlink")
    public boolean handleUnlink(
            CommandSender sender
    ) {

        if (sender instanceof Player player) {
            if (!CommandsUtility.hasPerm(player, List.of("skyfactions.command.unlink", "skyfactions.discord"), true))
                return true;
            if (CommandsUtility.manageCooldown(player)) return true;

            SkyFactionsReborn.databaseHandler.getDiscordLink(player).whenComplete((id, ex) -> {
                if (ex != null) {
                    ErrorHandler.handleError(player, "unlink your Discord", "SQL_DISCORD_UNLINK", ex);
                    return;
                }

                if (id == null) {
                    Messages.DISCORD_NOT_LINKED.send(player);
                } else {
                    Messages.DISCORD_UNLINK_SUCCESS.send(player);
                }
            });
        }
        return true;
    }

    @Override
    public String getName() {
        return "unlink";
    }

    @Override
    public String getDescription() {
        return "Remove the link between your discord account and your SkyFactions account.";
    }

    @Override
    public String getSyntax() {
        return "/unlink";
    }

    @Override
    public List<String> permission() {
        return List.of("skyfactions.command.unlink");
    }
}
