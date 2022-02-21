package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.OptionalLong;

public class SQLite extends StorageEngine {

    private FileConfiguration config;
    private Connection conn;

    public SQLite(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void init() {
        config = shadowBan.getConfigManager().getConfig(); // Read config here
        File file = new File(shadowBan.getDataFolder() + "/PlayerData.db");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            DriverManager.registerDriver((Driver) Bukkit.getServer().getClass().getClassLoader().loadClass("org.sqlite.JDBC").newInstance());
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            if (ex instanceof ClassNotFoundException) {
                shadowBan.getLogger().info("SQLite connection failed! 润了");
                shadowBan.getServer().getPluginManager().disablePlugin(shadowBan);
            }
            ex.printStackTrace();
        }
        createTable();
        shadowBan.getLogger().info("SQLite connected!");
    }

    private void createTable() {
        try (Statement stmt = conn.createStatement()) {
            String prefix = config.getString("database.tableprefix", "");
            stmt.executeUpdate(
                    "create table if not exists `" + prefix + "shadowban`(" +
                            "`uuid`             VARCHAR(36)        NOT NULL PRIMARY KEY," +
                            "`playername`       VARCHAR(16)        NOT NULL," +
                            "`bantime`          BIGINT             NOT NULL" +
                            ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        // Close database connection
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public OptionalLong load(OfflinePlayer player) {
        String prefix = config.getString("database.tableprefix", "");
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + prefix + "shadowban WHERE uuid=?")) {
            stmt.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                // Query player record
                if (rs.next()) {
                    return OptionalLong.of(rs.getLong("bantime"));
                } else {
                    return OptionalLong.empty();
                }
            }
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
            return OptionalLong.empty();
        }
    }

    @Override
    public void save(OfflinePlayer player, Long time) {
        String table = config.getString("database.tableprefix", "") + "shadowban";
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        String updateSql = "UPDATE " + table + " SET " +
                "playername='" + name + "', bantime='" + time + "' WHERE `uuid` = '" + uuid + "'";
        String insertSql = "INSERT INTO " + table + "(`uuid`, `playername`, `bantime`) VALUES (" + uuid + ", " + name + ", " + time + ")";
        try (PreparedStatement stmt = conn.prepareStatement(
                conn.prepareStatement("SELECT * FROM " + table + " WHERE `uuid` = '" + player.getUniqueId().toString() + "'").executeQuery().next()
                        ? updateSql : insertSql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(OfflinePlayer player) {
        String prefix = config.getString("database.tableprefix", "");
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + prefix + "shadowban WHERE uuid=?")) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.execute();
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
        }
    }

}
