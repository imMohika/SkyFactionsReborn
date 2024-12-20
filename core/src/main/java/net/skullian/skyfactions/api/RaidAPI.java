package net.skullian.skyfactions.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import net.skullian.skyfactions.api.PlayerAPI;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.config.types.Messages;
import net.skullian.skyfactions.config.types.Settings;
import net.skullian.skyfactions.database.struct.IslandRaidData;
import net.skullian.skyfactions.util.SoundUtil;
import net.skullian.skyfactions.util.text.TextUtility;


public class RaidAPI {

    // attacker, victim
    public static Map<UUID, UUID> processingRaid = new HashMap<>();
    public static Map<UUID, UUID> currentRaids = new HashMap<>();

    public static CompletableFuture<String> getCooldownDuration(Player player) {
        long cooldownDurationInMilliseconds = Settings.RAIDING_COOLDOWN.getLong();
        return PlayerAPI.getPlayerData(player.getUniqueId()).handle((data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return null;
            }

            long currentTime = System.currentTimeMillis();

            if (currentTime - data.getLAST_RAID() >= cooldownDurationInMilliseconds) {
                return null;
            } else {
                long cooldownDuration = cooldownDurationInMilliseconds - (currentTime - data.getLAST_RAID());
                return DurationFormatUtils.formatDuration(cooldownDuration, "HH'h 'mm'm 'ss's'");
            }
        });
    }

    public static void startRaid(Player player) {
        // TODO REFACTOR ALL OF THIS
        try {
            AtomicBoolean cancel = new AtomicBoolean(false);

            Messages.RAID_PROCESSING.send(player, PlayerAPI.getLocale(player.getUniqueId()));

            /*SkyFactionsReborn.getDatabaseManager().updateLastRaid(player, System.currentTimeMillis()).thenAccept(result -> SkyFactionsReborn.getDatabaseManager().getGems(player).thenAccept(count -> SkyFactionsReborn.getDatabaseManager().subtractGems(player, count, Settings.RAIDING_COST.getInt()))).exceptionally(ex -> {
                ex.printStackTrace();
                cancel.set(true);
                Messages.ERROR.send(player, "operation", "start a raid", "debug", "SQL_RAID_START");
                handleRaidExecutionError(player, false);
                return null;
            }).get();*/

            if (cancel.get()) return;
            IslandRaidData island = getRandomRaidable(player);
            if (island != null) {
                UUID playerUUID = UUID.fromString(island.getUuid());

                if (cancel.get()) return;
                handlePlayers(player, playerUUID);
                processingRaid.put(player.getUniqueId(), playerUUID);
            } else {
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "start a raid", "debug", "ISLAND_RETURNED_NULL");
            }

        } catch (Exception error) {
            error.printStackTrace();
            Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "start a raid", "debug", "MAIN_RAID_START");
            handleRaidExecutionError(player, false);
        }
    }

    private static void handlePlayers(Player attacker, UUID uuid) {
        AtomicBoolean cancel = new AtomicBoolean(false);

        if (!isPlayerOnline(uuid)) {
            SkyFactionsReborn.getDiscordHandler().pingRaid(attacker, Bukkit.getOfflinePlayer(uuid).getPlayer());
        } else {
            Player def = Bukkit.getPlayer(uuid);
            if (def == null) return;

            alertPlayer(def, attacker);
            teleportToPreparationArea(def);

            IslandAPI.getPlayerIsland(def.getUniqueId()).thenAccept(is -> SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().setIslandCooldown(is, System.currentTimeMillis())).exceptionally(ex -> {
                cancel.set(true);

                ex.printStackTrace();
                Messages.ERROR.send(def, attacker.locale().getLanguage(), "operation", "start a raid", "debug", "SQL_ISLAND_COOLDOWN");
                Messages.ERROR.send(attacker, attacker.locale().getLanguage(), "operation", "start a raid", "debug", "SQL_ISLAND_COOLDOWN");
                handleRaidExecutionError(def, true);
                handleRaidExecutionError(attacker, false);
                return null;
            });
            if (cancel.get()) return;

            Bukkit.getScheduler().runTaskLater(SkyFactionsReborn.getInstance(), () -> {
                if (!def.isOnline()) return;
                IslandAPI.getPlayerIsland(def.getUniqueId()).thenAccept(island -> {
                    World islandWorld = Bukkit.getWorld(Settings.ISLAND_PLAYER_WORLD.getString());
                    if (islandWorld != null && island != null) {
                        Location returnLoc = island.getCenter(islandWorld);
                        RegionAPI.teleportPlayerToLocation(def, returnLoc);

                        returnLoc.setY(Settings.RAIDING_SPAWN_HEIGHT.getInt());
                        RegionAPI.teleportPlayerToLocation(attacker, returnLoc);

                        showCountdown(uuid, attacker).thenAccept(re -> {
                            SoundUtil.playMusic(def, attacker);
                        }).exceptionally(ex -> {
                            cancel.set(true);
                            ex.printStackTrace();
                            Messages.ERROR.send(attacker, attacker.locale().getLanguage(), "operation", "start a raid", "debug", "MAIN_RAID_COUNTDOWN");
                            Messages.ERROR.send(def, attacker.locale().getLanguage(), "operation", "start a raid", "debug", "MAIN_RAID_COUNTDOWN");
                            handleRaidExecutionError(def, true);
                            handleRaidExecutionError(attacker, false);

                            return null;
                        });
                    }
                });

            }, (Settings.RAIDING_PREPARATION_TIME.getInt() * 20L));
        }
    }

    public static boolean hasEnoughGems(Player player) {
        try {
            int required = Settings.RAIDING_COST.getInt();
            AtomicInteger currentGems = new AtomicInteger();
            SkyFactionsReborn.getDatabaseManager().getCurrencyManager().getGems(player.getUniqueId()).thenAccept(currentGems::set).exceptionally(ex -> {
                ex.printStackTrace();
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "check your gem count", "debug", "SQL_GEMS_GET");
                return null;
            }).get();

            if (currentGems.get() < required) {
                Messages.RAID_INSUFFICIENT_GEMS.send(player, PlayerAPI.getLocale(player.getUniqueId()), "raid_cost", required);
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException | ExecutionException error) {
            error.printStackTrace();
            Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "start a raid", "debug", "SQL_GEMS_GET");
        }

        return false;
    }

    public static IslandRaidData getRandomRaidable(Player player) {
        try {
            AtomicReference<List<IslandRaidData>> data = new AtomicReference<>(new ArrayList<>());
            SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().getRaidableIslands(player).thenAccept(data::set).exceptionally(ex -> {
                ex.printStackTrace();
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "start a raid", "debug", "SQL_RAIDABLE_GET");
                return null;
            }).get();

            if (data.get().isEmpty()) {
                Messages.RAID_NO_PLAYERS.send(player, PlayerAPI.getLocale(player.getUniqueId()));
            } else {
                Random random = new Random();
                return data.get().get(random.nextInt(data.get().size()));
            }
        } catch (InterruptedException | ExecutionException error) {
            error.printStackTrace();
            Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "start a raid", "debug", "SQL_RAIDABLE_GET");
        }
        return null;
    }

    private static boolean isPlayerOnline(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player.isOnline();
    }

    private static void alertPlayer(Player player, Player attacker) {
        SoundUtil.soundAlarm(player);
        List<String> alertList = Messages.RAID_IMMINENT_MESSAGE.getStringList(PlayerAPI.getLocale(player.getUniqueId()));

        if (player.isOnline()) {
            for (String msg : alertList) {
                player.sendMessage(TextUtility.color(msg.replace("player_name", player.getName()).replace("raider", attacker.getName()), PlayerAPI.getLocale(player.getUniqueId()), player));
            }
        }
    }

    private static void teleportToPreparationArea(Player player) {
        if (player.isOnline()) {
            List<Integer> loc = Settings.RAIDING_PREPARATION_POS.getIntegerList();
            World world = Bukkit.getWorld(Settings.RAIDING_PREPARATION_WORLD.getString());
            if (world != null) {
                Location location = new Location(world, loc.get(0), loc.get(1), loc.get(2));
                player.teleport(location);
            }
        }
    }

    private static CompletableFuture<Void> showCountdown(UUID def, Player att) {
        return CompletableFuture.runAsync(() -> {
            try {
                Player defp = Bukkit.getPlayer(def);
                int length = Settings.RAIDING_COUNTDOWN_DURATION.getInt();
                String countdown_sound = Settings.COUNTDOWN_SOUND.getString();
                int countdown_pitch = Settings.COUNTDOWN_SOUND_PITCH.getInt();
                final Component attackerSubtitle = TextUtility.color(Messages.RAIDING_COUNTDOWN_SUBTITLE.getString(att.locale().getLanguage()), att.locale().getLanguage(), att);
                final Component defendantSubtitle = defp != null ? TextUtility.color(Messages.RAIDING_COUNTDOWN_SUBTITLE.getString(defp.locale().getLanguage()), defp.locale().getLanguage(), defp) : null;
                Thread.sleep(3500);
                for (int i = length; i == 0; i--) {
                    final Component mainTitle = Component.text(i + 1, NamedTextColor.RED);

                    final Title attackerTitle = Title.title(mainTitle, attackerSubtitle);
                    if (defp != null) att.showTitle(attackerTitle);

                    SoundUtil.playSound(defp, countdown_sound, countdown_pitch, 1f);
                    SoundUtil.playSound(att, countdown_sound, countdown_pitch, 1f);

                    Thread.sleep(1000);
                }
            } catch (InterruptedException error) {
                throw new RuntimeException(error);
            }
        });

    }

    private static void handleRaidExecutionError(Player player, boolean isDefendant) {
        if (player != null) {
            if (processingRaid.containsKey(player.getUniqueId()) || processingRaid.containsValue(player.getUniqueId())) {
                processingRaid.remove(player.getUniqueId());
            }
        }

        SkyFactionsReborn.getCacheService().getEntry(player.getUniqueId()).setNewLastRaid(player.getUniqueId(), 0);
        SkyFactionsReborn.getCacheService().getEntry(player.getUniqueId()).addGems(Settings.RAIDING_COST.getInt());

        if (isDefendant) {
            IslandAPI.getPlayerIsland(player.getUniqueId()).thenAccept(island -> SkyFactionsReborn.getDatabaseManager().getPlayerIslandManager().setIslandCooldown(island, 0).exceptionally(ex -> {
                ex.printStackTrace();
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "handle raid errors", "debug", "SQL_ISLAND_COOLDOWN");
                return null;
            })).exceptionally(ex -> {
                ex.printStackTrace();
                Messages.ERROR.send(player, PlayerAPI.getLocale(player.getUniqueId()), "operation", "handle raid errors", "debug", "SQL_ISLAND_GET");
                return null;
            });
        }
    }
}