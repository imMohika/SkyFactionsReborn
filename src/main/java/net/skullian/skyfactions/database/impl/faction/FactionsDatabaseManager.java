package net.skullian.skyfactions.database.impl.faction;

import net.skullian.skyfactions.SkyFactionsReborn;
import net.skullian.skyfactions.database.tables.records.FactionbansRecord;
import net.skullian.skyfactions.database.tables.records.FactionislandsRecord;
import net.skullian.skyfactions.database.tables.records.FactionmembersRecord;
import net.skullian.skyfactions.database.tables.records.FactionsRecord;
import net.skullian.skyfactions.event.PlayerHandler;
import net.skullian.skyfactions.faction.Faction;
import net.skullian.skyfactions.faction.RankType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.skullian.skyfactions.database.tables.Defencelocations.DEFENCELOCATIONS;
import static net.skullian.skyfactions.database.tables.Factionbans.FACTIONBANS;
import static net.skullian.skyfactions.database.tables.Factioninvites.FACTIONINVITES;
import static net.skullian.skyfactions.database.tables.Factionislands.FACTIONISLANDS;
import static net.skullian.skyfactions.database.tables.Factionmembers.FACTIONMEMBERS;
import static net.skullian.skyfactions.database.tables.Factions.FACTIONS;
import static net.skullian.skyfactions.database.tables.Auditlogs.AUDITLOGS;
import static net.skullian.skyfactions.database.tables.Factionelections.FACTIONELECTIONS;

public class FactionsDatabaseManager {

    private final DSLContext ctx;

    public FactionsDatabaseManager(DSLContext ctx) {
        this.ctx = ctx;
    }

    public CompletableFuture<Void> registerFaction(Player factionOwner, String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.insertInto(FACTIONS)
                    .columns(FACTIONS.NAME, FACTIONS.MOTD, FACTIONS.LEVEL, FACTIONS.LAST_RAID, FACTIONS.LOCALE, FACTIONS.LAST_RENAMED)
                    .values(factionName, "<red>None", 1, (long) 0, PlayerHandler.getLocale(factionOwner.getUniqueId()), System.currentTimeMillis())
                    .execute();

            ctx.insertInto(FACTIONMEMBERS)
                    .columns(FACTIONMEMBERS.FACTIONNAME, FACTIONMEMBERS.UUID, FACTIONMEMBERS.RANK)
                    .values(factionName, factionOwner.getUniqueId().toString(), "owner")
                    .execute();
        });
    }

    public CompletableFuture<Faction> getFaction(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
            FactionsRecord result = ctx.selectFrom(FACTIONS)
                    .where(FACTIONS.NAME.eq(factionName))
                    .fetchOne();

            return result != null ?
                    new Faction(
                            SkyFactionsReborn.getDatabaseManager().getFactionIslandManager().getFactionIsland(factionName).join(),
                            result.getName(),
                            result.getLastRaid(),
                            result.getLevel(),
                            getFactionOwner(factionName).join(),
                            getFactionMembersByRank(factionName, RankType.ADMIN).join(),
                            getFactionMembersByRank(factionName, RankType.MODERATOR).join(),
                            getFactionMembersByRank(factionName, RankType.ADMIN).join(),
                            getFactionMembersByRank(factionName, RankType.MEMBER).join(),
                            getFactionMOTD(factionName).join(),
                            SkyFactionsReborn.getDatabaseManager().getCurrencyManager().getRunes(factionName).join(),
                            SkyFactionsReborn.getDatabaseManager().getCurrencyManager().getGems(factionName).join(),
                            result.getLocale(),
                            SkyFactionsReborn.getDatabaseManager().getElectionManager().isElectionRunning(factionName).join(),
                            getBannedPlayers(factionName).join(),
                            result.getLastRenamed(),
                            SkyFactionsReborn.getDatabaseManager().getFactionInvitesManager().getAllInvites(factionName).join(),
                            SkyFactionsReborn.getDatabaseManager().getFactionAuditLogManager().getAuditLogs(factionName).join()
                    ) : null;
        });
    }

    public CompletableFuture<Faction> getFaction(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            FactionmembersRecord result = ctx.selectFrom(FACTIONMEMBERS)
                    .where(FACTIONMEMBERS.UUID.eq(playerUUID.toString()))
                    .fetchOne();

            return result != null ? getFaction(result.getFactionname()).join() : null;
        });
    }

    public CompletableFuture<Faction> getFactionByIslandID(int id) {
        return CompletableFuture.supplyAsync(() -> {
            FactionislandsRecord result = ctx.selectFrom(FACTIONISLANDS)
                    .where(FACTIONISLANDS.ID.eq(id))
                    .fetchOne();

            return result != null ? getFaction(result.getFactionname()).join() : null;
        });
    }

    public CompletableFuture<Void> updateFactionName(String oldName, String newName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                trx.dsl().update(FACTIONS)
                        .set(FACTIONS.NAME, newName)
                        .where(FACTIONS.NAME.eq(oldName))
                        .execute();

                trx.dsl().update(FACTIONBANS)
                        .set(FACTIONBANS.FACTIONNAME, newName)
                        .where(FACTIONBANS.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(FACTIONMEMBERS)
                        .set(FACTIONMEMBERS.FACTIONNAME, newName)
                        .where(FACTIONMEMBERS.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(AUDITLOGS)
                        .set(AUDITLOGS.FACTIONNAME, newName)
                        .where(AUDITLOGS.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(DEFENCELOCATIONS)
                        .set(DEFENCELOCATIONS.FACTIONNAME, newName)
                        .where(DEFENCELOCATIONS.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(FACTIONINVITES)
                        .set(FACTIONINVITES.FACTIONNAME, newName)
                        .where(FACTIONINVITES.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(FACTIONISLANDS)
                        .set(FACTIONISLANDS.FACTIONNAME, newName)
                        .where(FACTIONISLANDS.FACTIONNAME.eq(oldName))
                        .execute();

                trx.dsl().update(FACTIONELECTIONS)
                        .set(FACTIONELECTIONS.FACTIONNAME, newName)
                        .where(FACTIONELECTIONS.FACTIONNAME.eq(oldName))
                        .execute();
            });
        });
    }

    public CompletableFuture<String> getFactionMOTD(String factionName) {
        return CompletableFuture.supplyAsync(() -> ctx.select(FACTIONS.MOTD)
                .from(FACTIONS)
                .where(FACTIONS.NAME.eq(factionName))
                .fetchOneInto(String.class));
    }

    public CompletableFuture<Void> updateFactionMOTD(String factionName, String newMOTD) {
        return CompletableFuture.runAsync(() -> {
            ctx.update(FACTIONS)
                    .set(FACTIONS.MOTD, newMOTD)
                    .where(FACTIONS.NAME.eq(factionName))
                    .execute();
        });
    }

    public CompletableFuture<Void> updateFactionLocale(String factionName, String newLocale) {
        return CompletableFuture.runAsync(() -> {
            if (newLocale == null) return;
            ctx.update(FACTIONS)
                    .set(FACTIONS.LOCALE, newLocale)
                    .where(FACTIONS.NAME.eq(factionName))
                    .execute();
        });
    }

    public CompletableFuture<Void> removeFaction(String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                trx.dsl().deleteFrom(FACTIONISLANDS)
                        .where(FACTIONISLANDS.FACTIONNAME.eq(factionName))
                        .execute();

                trx.dsl().deleteFrom(FACTIONMEMBERS)
                        .where(FACTIONMEMBERS.FACTIONNAME.eq(factionName))
                        .execute();

                trx.dsl().deleteFrom(FACTIONBANS)
                        .where(FACTIONBANS.FACTIONNAME.eq(factionName))
                        .execute();

                trx.dsl().deleteFrom(FACTIONISLANDS)
                        .where(FACTIONISLANDS.FACTIONNAME.eq(factionName))
                        .execute();

                trx.dsl().deleteFrom(FACTIONINVITES)
                        .where(FACTIONISLANDS.FACTIONNAME.eq(factionName))
                        .execute();

                trx.dsl().deleteFrom(AUDITLOGS)
                        .where(AUDITLOGS.FACTIONNAME.eq(factionName))
                        .execute();
            });
        });
    }


    // ------------------ MEMBERS  ------------------ //

    public CompletableFuture<Void> addFactionMembers(List<OfflinePlayer> players, String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                for (OfflinePlayer player : players) {
                    trx.dsl().insertInto(FACTIONMEMBERS)
                            .columns(FACTIONMEMBERS.FACTIONNAME, FACTIONMEMBERS.UUID, FACTIONMEMBERS.RANK)
                            .values(factionName, player.getUniqueId().toString(), "member")
                            .execute();
                }
            });
        });
    }

    public CompletableFuture<Void> updateFactionMemberRanks(String factionName, Map<UUID, RankType> ranks) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                for (Map.Entry<UUID, RankType> entry : ranks.entrySet()) {
                    trx.dsl().update(FACTIONMEMBERS)
                            .set(FACTIONMEMBERS.RANK, entry.getValue().getRankValue())
                            .where(FACTIONMEMBERS.UUID.eq(entry.getKey().toString()), FACTIONMEMBERS.FACTIONNAME.eq(factionName))
                            .execute();
                }
            });
        });
    }

    public CompletableFuture<Boolean> isInFaction(Player player) {
        return CompletableFuture.supplyAsync(() -> ctx.fetchExists(FACTIONMEMBERS, FACTIONMEMBERS.UUID.eq(player.getUniqueId().toString())));
    }

    public CompletableFuture<OfflinePlayer> getFactionOwner(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
            FactionmembersRecord result = ctx.selectFrom(FACTIONMEMBERS)
                    .where(FACTIONMEMBERS.FACTIONNAME.eq(factionName), FACTIONMEMBERS.RANK.eq("owner"))
                    .fetchOne();

            return result != null ? Bukkit.getOfflinePlayer(UUID.fromString(result.getUuid())) : null;
        });
    }

    public CompletableFuture<List<OfflinePlayer>> getFactionMembersByRank(String factionName, RankType rank) {
        return CompletableFuture.supplyAsync(() -> {
            Result<FactionmembersRecord> results = ctx.selectFrom(FACTIONMEMBERS)
                    .where(FACTIONMEMBERS.FACTIONNAME.eq(factionName), FACTIONMEMBERS.RANK.eq(rank.getRankValue()))
                    .fetch();

            List<OfflinePlayer> players = new ArrayList<>();
            for (FactionmembersRecord member : results) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(member.getUuid()));

                if (player.hasPlayedBefore()) players.add(player);
            }

            return players;
        });
    }

    // ------------------ ADMINISTRATION  ------------------ //

    public CompletableFuture<Void> removeMembers(List<OfflinePlayer> players, String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                for (OfflinePlayer player : players) {
                    trx.dsl().deleteFrom(FACTIONMEMBERS)
                            .where(FACTIONMEMBERS.UUID.eq(player.getUniqueId().toString()), FACTIONMEMBERS.FACTIONNAME.eq(factionName))
                            .execute();
                }
            });
        });
    }

    public CompletableFuture<Void> banMembers(List<OfflinePlayer> players, String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                removeMembers(players, factionName).join();

                for (OfflinePlayer player : players) {
                    trx.dsl().insertInto(FACTIONBANS)
                            .columns(FACTIONBANS.FACTIONNAME, FACTIONBANS.UUID)
                            .values(factionName, player.getUniqueId().toString())
                            .execute();
                }
            });
        });
    }

    public CompletableFuture<Void> unbanMembers(List<OfflinePlayer> players, String factionName) {
        return CompletableFuture.runAsync(() -> {
            ctx.transaction((Configuration trx) -> {
                for (OfflinePlayer player : players) {
                    trx.dsl().deleteFrom(FACTIONBANS)
                            .where(FACTIONBANS.UUID.eq(player.getUniqueId().toString()), FACTIONBANS.FACTIONNAME.eq(factionName))
                            .execute();
                }
            });
        });
    }

    public CompletableFuture<List<OfflinePlayer>> getBannedPlayers(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
            Result<FactionbansRecord> results = ctx.selectFrom(FACTIONBANS)
                    .where(FACTIONBANS.FACTIONNAME.eq(factionName))
                    .fetch();

            List<OfflinePlayer> players = new ArrayList<>();
            for (FactionbansRecord bannedPlayer : results) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(bannedPlayer.getUuid()));
                if (player.hasPlayedBefore()) players.add(player);
            }

            return players;
        });
    }

    public CompletableFuture<Boolean> isPlayerBanned(OfflinePlayer player, String factionName) {
        return CompletableFuture.supplyAsync(() -> ctx.fetchExists(FACTIONBANS, FACTIONBANS.FACTIONNAME.eq(factionName), FACTIONBANS.UUID.eq(player.getUniqueId().toString())));
    }

}
