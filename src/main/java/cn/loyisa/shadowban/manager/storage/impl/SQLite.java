package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.OptionalLong;

public class SQLite extends StorageEngine {

    private HikariDataSource hikari;
    private FileConfiguration config;

    public SQLite(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void init() {
        config = shadowBan.getConfigManager().getConfig(); // Read config here
        File file = new File(shadowBan.getDataFolder() + "/PlayerData.db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hikari = new HikariDataSource();
        hikari.setPoolName("ShadowBan-Hikari");
        hikari.setDriverClassName("org.sqlite.JDBC");
        hikari.setJdbcUrl("jdbc:sqlite:" + file);
        hikari.setConnectionTestQuery("SELECT 1");
        shadowBan.getLogger().info("SQLite connected!");
        createTable();
    }

    private void createTable() {
        try (Connection con = hikari.getConnection();
             Statement stmt = con.createStatement()) {
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
        if (hikari != null) {
            hikari.close();
        }
    }

    @Override
    public OptionalLong load(OfflinePlayer player) {
        String prefix = config.getString("database.tableprefix", "");
        try (Connection con = hikari.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + prefix + "shadowban WHERE uuid=?")) {
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
                "playername='" + name + "', bantime='" + time + "' WHERE uuid = '" + uuid + "'";
        String insertSql = "INSERT INTO " + table + "(uuid, playername, bantime) VALUES ('" + uuid + "', '" + name + "', '" + time + "')";
        try (Connection con = hikari.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     con.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "'").executeQuery().next()
                             ? updateSql : insertSql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(OfflinePlayer player) {
        String prefix = config.getString("database.tableprefix", "");
        try (Connection con = hikari.getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM " + prefix + "shadowban WHERE uuid=?")) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.execute();
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
        }
    }

}
