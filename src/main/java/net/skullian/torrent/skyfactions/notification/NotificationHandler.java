package net.skullian.torrent.skyfactions.notification;

import net.skullian.torrent.skyfactions.SkyFactionsReborn;
import net.skullian.torrent.skyfactions.api.FactionAPI;
import net.skullian.torrent.skyfactions.config.Settings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationHandler {

    public static Map<UUID, Integer> playerInviteStore = new HashMap<>();
    public static Map<String, Integer> factionInviteStore = new HashMap<>();

    public static Map<UUID, BukkitTask> tasks = new HashMap<>();

    public static void createCycle(Player player) {
        BukkitTask task = NotificationTask.initialise(player, FactionAPI.isInFaction(player)).runTaskTimerAsynchronously(SkyFactionsReborn.getInstance(), 0L, (Settings.NOTIFICATIONS_INTERVAL.getInt() * 20L));
        tasks.put(player.getUniqueId(), task);
    }
}
