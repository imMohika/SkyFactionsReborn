package net.skullian.torrent.skyfactions.command.faction.cmds;

import net.skullian.torrent.skyfactions.api.FactionAPI;
import net.skullian.torrent.skyfactions.command.CommandTemplate;
import net.skullian.torrent.skyfactions.command.CooldownHandler;
import net.skullian.torrent.skyfactions.command.PermissionsHandler;
import net.skullian.torrent.skyfactions.config.Messages;
import net.skullian.torrent.skyfactions.faction.Faction;
import org.bukkit.entity.Player;

public class FactionTeleportCommand extends CommandTemplate {
    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getDescription() {
        return "Teleport to your faction's island.";
    }

    @Override
    public String getSyntax() {
        return "/faction teleport";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!PermissionsHandler.hasPerm(player, permission(), true)) return;
        if (CooldownHandler.manageCooldown(player)) return;

        if (!FactionAPI.isInFaction(player)) {
            Messages.NOT_IN_FACTION.send(player);
            return;
        }
        FactionAPI.teleportToFactionIsland(player);
    }

    @Override
    public String permission() {
        return "skyfactions.faction.teleport";
    }
}