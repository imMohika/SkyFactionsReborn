package net.skullian.torrent.skyfactions.db;

import com.google.common.net.HostAndPort;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import net.skullian.torrent.skyfactions.SkyFactionsReborn;
import net.skullian.torrent.skyfactions.config.Settings;
import net.skullian.torrent.skyfactions.faction.Faction;
import net.skullian.torrent.skyfactions.island.FactionIsland;
import net.skullian.torrent.skyfactions.island.PlayerIsland;
import net.skullian.torrent.skyfactions.notification.NotificationData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "SkyFactionsReborn")
public class HikariHandler {

    private transient HikariDataSource dataSource;
    public int cachedPlayerIslandID;
    public int cachedFactionIslandID;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void initialise(String type) throws SQLException {
        LOGGER.info("Setting up Database.");

        createDataSource(new File(SkyFactionsReborn.getInstance().getDataFolder(), "/data/data.sqlite3"), type);
        setupTables();
    }

    private void createDataSource(
            @NotNull File file, @NotNull String type) {
        if (type.equals("sqlite")) {

            HikariConfig sqliteConfig = new HikariConfig();
            sqliteConfig.setDataSourceClassName("org.sqlite.SQLiteDataSource");
            sqliteConfig.addDataSourceProperty("url", JDBC.PREFIX + file.getAbsolutePath());
            sqliteConfig.addDataSourceProperty("encoding", "UTF-8");
            sqliteConfig.addDataSourceProperty("enforceForeignKeys", "true");
            sqliteConfig.addDataSourceProperty("synchronous", "NORMAL");
            sqliteConfig.addDataSourceProperty("journalMode", "WAL");
            sqliteConfig.setPoolName("SQLite");
            sqliteConfig.setMaximumPoolSize(1);

            dataSource = new HikariDataSource(sqliteConfig);

            LOGGER.info("Using SQLite Database.");
        } else if (type.equals("sql")) {

            String rawHost = Settings.DATABASE_HOST.getString();
            String databaseName = Settings.DATABASE_NAME.getString();
            String username = Settings.DATABASE_USERNAME.getString();
            String password = Settings.DATABASE_PASSWORD.getString();

            List<String> missingProperties = new ArrayList<>();

            if (rawHost == null || rawHost.isBlank()) {
                missingProperties.add("DATABASE_HOST");
            }

            if (databaseName == null || databaseName.isBlank()) {
                missingProperties.add("DATABASE_NAME");
            }

            if (username == null || username.isBlank()) {
                missingProperties.add("DATABASE_USERNAME");
            }

            if (password == null || password.isBlank()) {
                missingProperties.add("DATABASE_PASSWORD");
            }

            if (!missingProperties.isEmpty()) {
                throw new IllegalStateException("Missing MySQL Configuration Properties: " + missingProperties);
            }

            HostAndPort host = HostAndPort.fromHost(rawHost);
            HikariConfig mysqlConfig = new HikariConfig();
            mysqlConfig.setPoolName("SkyFactions");
            mysqlConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
                    host.getHost(), host.getPortOrDefault(3306), databaseName));
            mysqlConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(Settings.DATABASE_MAX_LIFETIME.getInt()));
            mysqlConfig.setUsername(username);
            mysqlConfig.setPassword(password);
            mysqlConfig.setMaximumPoolSize(2);

            LOGGER.info("Using MySQL database '{}' on: {}:{}.",
                    databaseName, host.getHost(), host.getPortOrDefault(3306));
            dataSource = new HikariDataSource(mysqlConfig);
        } else {
            throw new IllegalStateException("Unknown database type: " + type);
        }
    }

    private void setupTables() throws SQLException {
        LOGGER.info("Registering SQL Tables.");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement islandsTable = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS islands (
                     [id] INTEGER PRIMARY KEY,
                     [uuid] BLOB NOT NULL,
                     [level] INTEGER NOT NULL,
                     [gems] INTEGER NOT NULL,
                     [runes] INTEGER NOT NULL,
                     [last_raided] INTEGER NOT NULL
                     );
                     """);

            PreparedStatement playerDataTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS playerData (
                    [uuid] BLOB PRIMARY KEY UNIQUE NOT NULL,
                    [faction] STRING NOT NULL,
                    [discord_id] STRING NOT NULL,
                    [last_raid] INTEGER NOT NULL
                    );
                    """);

            PreparedStatement factionIslandTable = connection.prepareStatement("""
                     CREATE TABLE IF NOT EXISTS factionIslands (
                     [id] INTEGER PRIMARY KEY,
                     [faction_name] BLOB NOT NULL,
                     [runes] INTEGER NOT NULL,
                     [gems] INTEGER NOT NULL,
                     [last_raided] INTEGER NOT NULL
                     );
                    """);

            PreparedStatement factionTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS factions(
                    [name] STRING PRIMARY KEY UNIQUE NOT NULL,
                    [motd] STRING NOT NULL,
                    [level] INTEGER NOT NULL,
                    [last_raid] INTEGER NOT NULL
                    );
                    """);

            PreparedStatement factionMemberTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS factionMembers(
                    [faction_name] STRING PRIMARY KEY NOT NULL,
                    [uuid] BLOB NOT NULL,
                    [rank] STRING NOT NULL
                    );
                    """);

            PreparedStatement trustedPlayerTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS trustedPlayers (
                    [island_id] INTEGER PRIMARY KEY NOT NULL,
                    [uuid] BLOB NOT NULL
                    );
                    """);

            PreparedStatement auditLogTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS auditLogs (
                    [faction_name] STRING NOT NULL,
                    [type] STRING NOT NULL,
                    [uuid] BLOB NOT NULL,
                    [description] BLOB NOT NULL,
                    [timestamp] INTEGER NOT NULL
                    );
                    """);

            PreparedStatement factionBannedMembers = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS factionBans (
                    [faction_name] STRING NOT NULL,
                    [uuid] BLOB NOT NULL
                    );
                    """);

            PreparedStatement factionInvitesTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS factionInvites (
                    [faction_name] STRING NOT NULL,
                    [uuid] BLOB NOT NULL,
                    [inviter] BLOB NOT NULL,
                    [type] STRING NOT NULL,
                    [timestamp] INTEGER NOT NULL
                    );
                    """);

            PreparedStatement notificationTable = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS notifications (
                    [uuid] BLOB NOT NULL,
                    [type] BLOB NOT NULL,
                    [description] BLOB NOT NULL,
                    [timestamp] INTEGER NOT NULL
                    );
                    """)) {

            islandsTable.executeUpdate();
            islandsTable.close();

            playerDataTable.executeUpdate();
            playerDataTable.close();

            factionIslandTable.executeUpdate();
            factionIslandTable.close();

            factionTable.executeUpdate();
            factionTable.close();

            factionMemberTable.executeUpdate();
            factionMemberTable.close();

            trustedPlayerTable.executeUpdate();
            trustedPlayerTable.close();

            auditLogTable.executeUpdate();
            auditLogTable.close();

            factionBannedMembers.executeUpdate();
            factionBannedMembers.close();

            factionInvitesTable.executeUpdate();
            factionInvitesTable.close();

            notificationTable.executeUpdate();
            notificationTable.close();

            connection.close();
        }
    }

    public void closeConnection() throws SQLException {
        LOGGER.info("Disabling Database.");
        dataSource.close();
        LOGGER.info("Database closed.");
    }

    // ------------------ ISLAND ------------------ //

    public CompletableFuture<Boolean> hasIsland(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return true;
                }

                statement.close();
                connection.close();

                return false;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> createIsland(Player player, PlayerIsland island) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO islands (id, uuid, level, gems, runes, last_raided) VALUES (?, ?, ?,?, ?, ?)")) {

                statement.setInt(1, island.getId());
                statement.setString(2, player.getUniqueId().toString());
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.setInt(5, 0);
                statement.setInt(6, 0);

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<PlayerIsland> getPlayerIsland(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands where uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());
                ResultSet set = statement.executeQuery();

                if (set.next()) {
                    int id = set.getInt("id");
                    return new PlayerIsland(id);
                }

                statement.close();
                connection.close();

                return null;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> setIslandCachedNextID() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE id = (SELECT MAX(id) FROM islands);")) {

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    int id = set.getInt("id");
                    this.cachedPlayerIslandID = (id + 1);
                } else {
                    this.cachedPlayerIslandID = 1;
                }

                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> setFactionCachedNextID() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionIslands WHERE id = (SELECT MAX(id) FROM factionIslands);")) {

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    int id = set.getInt("id");
                    this.cachedFactionIslandID = (id + 1);
                } else {
                    this.cachedFactionIslandID = 1;
                }

                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Integer> getIslandLevel(PlayerIsland island) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE id = ?")) {

               statement.setInt(1, island.getId());
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   int level = set.getInt("level");

                   return level;
               }

               statement.close();
               connection.close();

               return 0;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> upgradeIslandLevel(PlayerIsland island) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE islands SET level = level + 1 WHERE id = ?")) {

               statement.setInt(1, island.getId());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> setIslandCooldown(PlayerIsland island, long time) {
        return CompletableFuture.runAsync(() -> {
            try {
                try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement("UPDATE islands set last_raided = ? WHERE id = ?")) {

                    statement.setLong(1, time);
                    statement.setInt(2, island.getId());

                    statement.executeUpdate();
                    statement.close();

                    connection.close();
                }
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> removeIsland(Player player) {
        return CompletableFuture.runAsync(() -> {
            try {
                try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM islands WHERE uuid = ?")) {
                    statement.setString(1, player.getUniqueId().toString());

                    statement.executeUpdate();
                    statement.close();

                    connection.close();
                }
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    // ------------------ PLAYER DATA ------------------ //

    public CompletableFuture<Boolean> playerIsRegistered(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerData WHERE uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return true;
                }

                statement.close();
                connection.close();

                return false;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> registerPlayer(Player player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO playerData (uuid, faction, discord_id, last_raid) VALUES (?, ?, ?, ?);")) {

                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, "none");
                statement.setString(3, "none");
                statement.setInt(4, 0);

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> registerDiscordLink(UUID uuid, String discordID) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE playerData set discord_id = ? WHERE uuid = ?")) {

                statement.setString(1, discordID);
                statement.setString(2, uuid.toString());

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<String> getDiscordLink(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerData WHERE uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());
                ResultSet set = statement.executeQuery();

                if (set.next()) {
                    String id = set.getString("discord_id");
                    if (id.equals("none")) {
                        return null;
                    }
                    return id;
                }

                statement.close();
                connection.close();
                return null;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Long> getLastRaid(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerData WHERE uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    long lastRaid = set.getLong("last_raid");

                    return lastRaid;
                }

                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
            return 0L;
        });
    }

    public CompletableFuture<Void> updateLastRaid(Player player, long time) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE playerData set last_raid = ? WHERE uuid = ?")) {

                statement.setLong(1, time);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<List<IslandRaidData>> getRaidablePlayers(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE last_raided <=?")) {

                statement.setLong(1, System.currentTimeMillis() - Settings.RAIDED_COOLDOWN.getInt());

                List<IslandRaidData> islands = new ArrayList<>();
                ResultSet set = statement.executeQuery();

                while (set.next()) {
                    int id = set.getInt("id");
                    String uuid = set.getString("uuid");
                    //if (player.getUniqueId().toString().equals(uuid)) continue;
                    int last_raided = set.getInt("last_raided");

                    islands.add(new IslandRaidData(id, uuid, last_raided));
                }

                statement.close();
                connection.close();

                return islands;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    // ------------------ CURRENCY ------------------ //

    public CompletableFuture<Integer> getGems(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE uuid = ?")) {

                statement.setString(1, player.getUniqueId().toString());

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    int gems = set.getInt("gems");
                    return gems;
                }

                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
            return 0;
        });
    }

    public CompletableFuture<Void> subtractGems(Player player, int current, int amount) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE islands set gems = ? WHERE uuid = ?")) {

                statement.setInt(1, (current - amount));
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> addGems(Player player, int amount) {
        int currentCount = getGems(player).join();
        int newCount = currentCount + amount;
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE islands set gems = ? WHERE uuid = ?")) {

                statement.setInt(1, newCount);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    // ------------------ TRUSTING ------------------ //

    public CompletableFuture<Boolean> isPlayerTrusted(Player player, int id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM trustedPlayers WHERE island_id = ? AND uuid = ?")) {

                statement.setInt(1, id);
                statement.setString(2, player.getUniqueId().toString());

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return true;
                }

                statement.close();
                connection.close();

                return false;
            } catch (SQLException error) {
                handleError(error);
            }

            return false;
        });
    }

    public CompletableFuture<Void> trustPlayer(Player player, int id) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO trustedPlayers (island_id, uuid) VALUES (?, ?)")) {

               statement.setInt(1, id);
               statement.setString(2, player.getUniqueId().toString());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
           }
        });
    }

    public CompletableFuture<Void> removeTrust(Player player, int id) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM trustedPlayers WHERE island_id = ? AND uuid = ?")) {

                statement.setInt(1, id);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();

                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
            }
        });
    }

    public CompletableFuture<Void> removeAllTrustedPlayers(int islandID) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE * FROM trustedPlayers WHERE island_id = ?")) {

                statement.setInt(1, islandID);

                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<List<OfflinePlayer>> getTrustedPlayers(int islandID) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM trustedPlayers WHERE island_id = ?")) {

               statement.setInt(1, islandID);
               ResultSet set = statement.executeQuery();

               List<OfflinePlayer> players = new ArrayList<>();
               while (set.next()) {
                   UUID uuid = UUID.fromString(set.getString("uuid"));
                   OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                   if (player.hasPlayedBefore()) {
                       players.add(player);
                   }
               }

               statement.close();
               connection.close();

               return players;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    // ------------------ FACTIONS ------------------ //

    // TODO - Make all players / factions unraidable for CONFIGURABLE AMOUNT OF TIME!

    public CompletableFuture<Void> registerFaction(Player owner, String name) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement factionRegistration = connection.prepareStatement("INSERT INTO factions (name, motd, level, last_raid) VALUES (?, ?, ?, ?)");
                PreparedStatement factionOwnerRegistration = connection.prepareStatement("INSERT INTO factionMembers (faction_name, uuid, rank) VALUES (?, ?, ?)")) {

               factionRegistration.setString(1, name);
               factionRegistration.setString(2, "&aNone");
               factionRegistration.setInt(3, 1);
               factionRegistration.setInt(4, 0);

               factionOwnerRegistration.setString(1, name);
               factionOwnerRegistration.setString(2, owner.getUniqueId().toString());

               factionOwnerRegistration.setString(3, "owner");
               factionOwnerRegistration.executeUpdate();
               factionOwnerRegistration.close();

               factionRegistration.executeUpdate();
               factionRegistration.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> addFactionMember(OfflinePlayer player, String factionName) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO factionMembers (faction_name, uuid, rank) VALUES (?, ?, ?)")) {

               statement.setString(1, factionName);
               statement.setString(2, player.getUniqueId().toString());
               statement.setString(3, "member");

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Boolean> isInFaction(Player player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement memberCheck = connection.prepareStatement("SELECT * FROM factionMembers WHERE uuid = ?")) {

               memberCheck.setString(1, player.getUniqueId().toString());
               ResultSet memberSet = memberCheck.executeQuery();
               if (memberSet.next()) {
                   return true;
               }

               memberCheck.close();
               connection.close();

               return false;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> createFactionIsland(String name, FactionIsland island) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO factionIslands (id, faction_name, runes, gems, last_raided) VALUES (?, ?, ?, ?, ?)")) {

               statement.setInt(1, island.getId());
               statement.setString(2, name);
               statement.setInt(3, 0);
               statement.setInt(4, 0);
               statement.setInt(5, island.getLast_raided());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<FactionIsland> getFactionIsland(String name) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionIslands WHERE faction_name = ?")) {

               statement.setString(1, name);
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   int id = set.getInt("id");
                   int last_raided = set.getInt("last_raided");

                   return new FactionIsland(id, last_raided);
               }

               statement.close();
               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }

           return null;
        });
    }

    public CompletableFuture<Faction> getFaction(String name) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factions WHERE name = ?")) {

               statement.setString(1, name);
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   int last_raid = set.getInt("last_raid");
                   int level = set.getInt("level");
                   statement.close();
                   connection.close();

                   FactionIsland island = getFactionIsland(name).get();

                   return new Faction(island, name, last_raid, level);
               }

               statement.close();
               connection.close();
           } catch (SQLException | ExecutionException | InterruptedException error) {
               if (error instanceof SQLException) {
                   handleError((SQLException) error);
               } else {
                   error.printStackTrace();
                   throw new RuntimeException(error);
               }
           }

           return null;
        });
    }

    public CompletableFuture<Faction> getFaction(Player player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement memberStatement = connection.prepareStatement("SELECT * FROM factionMembers WHERE uuid = ?")) {

               memberStatement.setString(1, player.getUniqueId().toString());

               ResultSet set = memberStatement.executeQuery();

               if (set.next()) {
                   String name = set.getString("faction_name");

                   memberStatement.close();
                   connection.close();

                   return getFaction(name).join();
               }

               memberStatement.close();
               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }

            return null;
        });
    }

    public CompletableFuture<Integer> getGems(String name) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionIslands WHERE faction_name = ?")) {

               statement.setString(1, name);
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   int gems = set.getInt("gems");

                   return gems;
               }
               statement.close();
               connection.close();

               return 0;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> addGems(String name, int addition) {
        return CompletableFuture.runAsync(() -> {
           int gemCount = getGems(name).join();
           int newCount = gemCount + addition;
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE factionIslands SET gems = ? WHERE faction_name = ?")) {

               statement.setInt(1, newCount);
               statement.setString(2, name);

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> updateFactionName(String name, String original_name) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE factions set name WHERE name = ?")) {

               statement.setString(1, name);
               statement.setString(2, original_name);
               statement.executeUpdate();

               statement.close();
               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> leaveFaction(String name, OfflinePlayer player) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM factionMembers WHERE faction_name = ? AND uuid = ?")) {

               statement.setString(1, name);
               statement.setString(2, player.getUniqueId().toString());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<OfflinePlayer> getFactionOwner(String name) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionMembers WHERE faction_name = ? AND rank = 'owner'")) {

               statement.setString(1, name);
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   UUID uuid = UUID.fromString(set.getString("uuid"));

                   return Bukkit.getOfflinePlayer(uuid);
               }

               statement.close();
               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }

           return null;
        });
    }

    public CompletableFuture<List<OfflinePlayer>> getMembersByRank(String name, String rank) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionMembers WHERE faction_name = ? AND rank = ?")) {

               statement.setString(1, name);
               statement.setString(2, rank);
               ResultSet set = statement.executeQuery();

               List<OfflinePlayer> players = new ArrayList<>();
               if (set.next()) {
                   UUID uuid = UUID.fromString(set.getString("uuid"));
                   OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                   if (player.hasPlayedBefore()) {
                       players.add(player);
                   }
               }

               statement.close();
               connection.close();

               return players;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> updateMemberRank(String factionName, Player player, String rank) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE factionMembers SET rank = ? WHERE faction_name = ? AND uuid = ?")) {

               statement.setString(1, rank);
               statement.setString(2, factionName);
               statement.setString(3, player.getUniqueId().toString());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<String> getMOTD(String name) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factions WHERE name = ?")) {

               statement.setString(1, name);;
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   String MOTD = set.getString("motd");

                   return MOTD;
               }

               statement.close();
               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }

           return "&aNone";
        });
    }

    public CompletableFuture<Void> setMOTD(String factionName, String MOTD) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE factions SET motd = ? WHERE name = ?")) {

                statement.setString(1, MOTD);
                statement.setString(2, factionName);

                statement.executeUpdate();
                statement.close();

                connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    // ------------------ RUNES  ------------------ //

    public CompletableFuture<Integer> getRunes(Player player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM islands WHERE uuid = ?")) {

               statement.setString(1, player.getUniqueId().toString());
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   return set.getInt("runes");
               }

               statement.close();
               connection.close();

               return 0;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Integer> getRunes(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionIslands WHERE faction_name = ?")) {

               statement.setString(1, factionName);
               ResultSet set = statement.executeQuery();

               if (set.next()) {
                   return set.getInt("runes");
               }

               statement.close();
               connection.close();

               return 0;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> addRunes(Player player, int addition) {
        return CompletableFuture.runAsync(() -> {
            int currentRunes = getRunes(player).join();
            int newCount = currentRunes + addition;
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE islands SET runes = ? WHERE uuid = ?")) {

               statement.setInt(1, newCount);
               statement.setString(2, player.getUniqueId().toString());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> addRunes(String factionName, int addition) {
        return CompletableFuture.runAsync(() -> {
            int currentRunes = getRunes(factionName).join();
            int newCount = currentRunes + addition;

            try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE factionIslands SET runes = ? WHERE faction_name = ?")) {

                statement.setInt(1, newCount);
                statement.setString(2, factionName);

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    // ------------------ FACTION ADMINISTRATION ------------------ //

    public CompletableFuture<Void> kickPlayer(OfflinePlayer player, String factionName) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE * FROM factionMembers WHERE uuid = ? AND faction_name = ?")) {

               statement.setString(1, player.getUniqueId().toString());
               statement.setString(2, factionName);

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> banPlayer(String factionName, OfflinePlayer player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement kickStatement = connection.prepareStatement("DELETE * FROM factionMembers WHERE uuid = ? AND faction_name = ?");
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO factionBans (faction_name, uuid) VALUES (?, ?);")) {

                kickStatement.setString(1, player.getUniqueId().toString());
                kickStatement.setString(2, factionName);

                statement.setString(1, factionName);
                statement.setString(2, player.getUniqueId().toString());

                kickStatement.executeUpdate();
                kickStatement.close();

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<List<OfflinePlayer>> getBannedPlayers(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionBans WHERE faction_name = ?")) {

                statement.setString(1, factionName);
                ResultSet set = statement.executeQuery();

                List<OfflinePlayer> players = new ArrayList<>();
                while (set.next()) {
                    UUID uuid = UUID.fromString(set.getString("uuid"));
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    if (player.hasPlayedBefore()) {
                        players.add(player);
                    }
                }

                statement.close();
                connection.close();

                return players;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Boolean> isPlayerBanned(String factionName, OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionBans WHERE faction_name = ? AND uuid = ?")) {

                statement.setString(1, factionName);
                statement.setString(2, player.getUniqueId().toString());

                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    return true;
                }

                statement.close();
                connection.close();

                return false;
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    public CompletableFuture<Void> unbanPlayer(String factionName, OfflinePlayer player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM factionBans WHERE faction_name = ? AND uuid = ?")) {

                statement.setString(1, factionName);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();

                connection.close();
            } catch (SQLException error) {
                handleError(error);
                throw new RuntimeException(error);
            }
        });
    }

    // ------------------ FACTION AUDIT LOGGING ------------------ //

    public CompletableFuture<Void> createAuditLog(OfflinePlayer player, String type, String description, String factionName) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO auditLogs (faction_name, type, uuid, description, timestamp) VALUES (?, ?, ?, ?, ?);")) {

               statement.setString(1, factionName);
               statement.setString(2, type);
               statement.setString(3, player.getUniqueId().toString());
               statement.setString(4, description);
               statement.setLong(5, System.currentTimeMillis());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<List<AuditLogData>> getAuditLogs(String factionName) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM auditLogs WHERE faction_name = ? ORDER BY timestamp DESC")) {

               statement.setString(1, factionName);
               ResultSet set = statement.executeQuery();

               List<AuditLogData> data = new ArrayList<>();
               while (set.next()) {
                   String faction_name = set.getString("faction_name");
                   UUID uuid = UUID.fromString(set.getString("uuid"));
                   String type = set.getString("type");
                   String description = set.getString("description");
                   long timestamp = set.getLong("timestamp");

                   data.add(new AuditLogData(Bukkit.getOfflinePlayer(uuid), faction_name, type, description, timestamp));
               }

               return data;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    // ------------------ FACTION INVITES ------------------ //

    public CompletableFuture<Void> createInvite(Player player, String factionName, String type, Player inviter) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO factionInvites (faction_name, uuid, inviter, type, timestamp) VALUES (?, ?, ?, ?, ?);")) {

               statement.setString(1, factionName);
               statement.setString(2, player.getUniqueId().toString());
               if (inviter != null) {
                   statement.setString(3, inviter.getUniqueId().toString());
               } else {
                   statement.setString(3, "");
               }
               statement.setString(4, type);
               statement.setLong(5, System.currentTimeMillis());

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Boolean> joinRequestExists(String factionName, Player player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionInvites WHERE faction_name = ? AND uuid = ? AND type = 'incoming'")) {

               statement.setString(1, factionName);
               statement.setString(2, player.getUniqueId().toString());

               ResultSet set = statement.executeQuery();
               if (set.next()) {
                   return true;
               }

               statement.close();
               connection.close();

               return false;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<List<InviteData>> getInvitesOfType(String factionName, String type) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionInvites WHERE faction_name = ? AND type = ? ORDER BY timestamp DESC")) {

               statement.setString(1, factionName);
               statement.setString(2, type);

               ResultSet set = statement.executeQuery();
               List<InviteData> data = new ArrayList<>();
               while (set.next()) {

                   OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(set.getString("uuid")));
                   long timestamp = set.getLong("timestamp");
                   String uuid = set.getString("inviter");
                   OfflinePlayer inviter = !uuid.isEmpty() ? Bukkit.getOfflinePlayer(UUID.fromString(uuid)) : null;

                   data.add(new InviteData(offlinePlayer, inviter, factionName, type, timestamp));
               }

               statement.close();
               connection.close();

               return data;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<List<InviteData>> getInvitesOfPlayer(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM factionInvites WHERE uuid = ? AND type = 'outgoing' ORDER BY timestamp DESC")) {

               statement.setString(1, player.getUniqueId().toString());

               ResultSet set = statement.executeQuery();
               List<InviteData> data = new ArrayList<>();
               while (set.next()) {
                   String factionName = set.getString("faction_name");
                   long timestamp = set.getLong("timestamp");
                   String uuid = set.getString("inviter");
                   OfflinePlayer inviter = !uuid.isEmpty() ? Bukkit.getOfflinePlayer(UUID.fromString(uuid)) : null;

                   data.add(new InviteData(player, inviter, factionName, "outgoing", timestamp));
               }

               statement.close();
               connection.close();

               return data;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<Void> revokeInvite(String factionName, OfflinePlayer player, String type) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM factionInvites WHERE faction_name = ? AND uuid = ? AND type = ?")) {

               statement.setString(1, factionName);
               statement.setString(2, player.getUniqueId().toString());
               statement.setString(3, type);

               statement.executeUpdate();
               statement.close();

               connection.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    // ------------------ NOTIFICATIONS ------------------ //

    public CompletableFuture<Void> createNotification(OfflinePlayer player, String type, String description) {
        return CompletableFuture.runAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO notifications (uuid, type, description, timestamp) VALUES (?, ?, ?, ?)")) {

               statement.setString(1, player.getUniqueId().toString());
               statement.setString(2, type);
               statement.setString(3, description);
               statement.setLong(4, System.currentTimeMillis());

               statement.executeUpdate();
               statement.close();
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }

    public CompletableFuture<List<NotificationData>> getNotifications(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
           try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM notifications WHERE uuid = ? ORDER BY timestamp DESC")) {

               statement.setString(1, player.getUniqueId().toString());
               ResultSet set = statement.executeQuery();

               List<NotificationData> data = new ArrayList<>();
               while (set.next()) {
                   String type = set.getString("type");
                   String desc = set.getString("desc");
                   long timestamp = set.getLong("timestamp");

                   data.add(new NotificationData(player.getUniqueId(), type, desc, timestamp));
               }

               statement.close();
               connection.close();

               return data;
           } catch (SQLException error) {
               handleError(error);
               throw new RuntimeException(error);
           }
        });
    }


    // ------------------ MISC ------------------ //

    public void handleError(SQLException error) {
        Bukkit.getScheduler().runTask(SkyFactionsReborn.getInstance(), () -> {
            LOGGER.fatal("----------------------- DATABASE EXCEPTION -----------------------");
            LOGGER.fatal("There was an error while performing database actions:");
            LOGGER.fatal(error.getMessage());
            LOGGER.fatal("Please see https://docs.terrabytedev.com/skyfactions/errors-and-debugging for more information.");
            LOGGER.fatal("Please contact the devs.");
            LOGGER.fatal("----------------------- DATABASE EXCEPTION -----------------------");
        });
    }
}
