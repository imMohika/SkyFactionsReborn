package net.skullian.skyfactions.api;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.event.defence.DefencePlacementHandler;
import net.skullian.skyfactions.notification.NotificationData;
import net.skullian.skyfactions.notification.NotificationTask;
import net.skullian.skyfactions.notification.NotificationType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NotificationAPI {

    public static Map<String, Integer> factionInviteStore = new HashMap<>();
    public static Map<UUID, BukkitTask> tasks = new HashMap<>();

    public static void createCycle(Player player) {
        FactionAPI.getFaction(player.getUniqueId()).whenComplete((faction, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            if (faction != null && !factionInviteStore.containsKey(faction.getName())) {
                DefencePlacementHandler.addPlacedDefences(faction.getName());

                faction.getJoinRequests().whenComplete((requests, exc) -> {
                    if (exc != null) {
                        exc.printStackTrace();
                        return;
                    }

                    factionInviteStore.put(faction.getName(), requests.size());

                    BukkitTask task = NotificationTask.initialise(player, true).runTaskTimerAsynchronously(SkyFactionsReborn.getInstance(), 0L, (Settings.NOTIFICATIONS_INTERVAL.getInt() * 20L));
                    tasks.put(player.getUniqueId(), task);
                });
            }
        });
    }

    /**
     * Create a notification for a Player.
     *
     * @param playerUUID   UUID of the player who the notification should be sent to [{@link UUID}]
     * @param type         Type of notification [{@link net.skullian.skyfactions.faction.AuditLogType}]
     * @param replacements Replacements for the notification title / desc.
     */
    public static CompletableFuture<Void> createNotification(UUID playerUUID, NotificationType type, Object... replacements) {
        return SkyFactionsReborn.getDatabaseManager().getNotificationManager().createNotification(playerUUID, type.name(), Arrays.toString(replacements));
    }

    /**
     * Get all notifications (unread) of a Player.
     *
     * @param player Player who the notifications should be fetched from [{@link Player}]
     * @return {@link List<NotificationData>}
     */
    public static CompletableFuture<List<NotificationData>> getNotifications(OfflinePlayer player) {
        return SkyFactionsReborn.getDatabaseManager().getNotificationManager().getNotifications(player);
    }
}
