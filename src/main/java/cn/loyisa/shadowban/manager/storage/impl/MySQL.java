package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.OptionalLong;

public class MySQL extends StorageEngine {

    private HikariDataSource hikari;
    private FileConfiguration config;

    public MySQL(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void init() {
        // Read mysql config
        config = shadowBan.getConfigManager().getConfig();
        hikari = new HikariDataSource();
        hikari.setPoolName("ShadowBan-Hikari");
        hikari.setJdbcUrl("jdbc:mysql://" + config.getString("database.address") + "/" + config.getString("database.database"));
        hikari.addDataSourceProperty("user", config.getString("database.username"));
        hikari.addDataSourceProperty("password", config.getString("database.password"));
        hikari.addDataSourceProperty("useUnicode", config.getBoolean("database.useunicode"));
        hikari.addDataSourceProperty("characterEncoding", config.getString("database.characterencoding"));
        hikari.addDataSourceProperty("useSSL", config.getBoolean("database.usessl"));
        shadowBan.getLogger().info("MySQL/MariaDB connecting...");
        if (isConnected()) {
            shadowBan.getLogger().info("MySQL/MariaDB connected!");
            createTable();
        } else {
            shadowBan.getLogger().info("MySQL/MariaDB connection failed! 寄了!啊哈哈哈");
            shadowBan.getServer().getPluginManager().disablePlugin(shadowBan);
        }
    }

    private void createTable() {
        try (Connection con = hikari.getConnection();
             Statement statement = con.createStatement()) {
            String tableprefix = config.getString("database.tableprefix");
            statement.executeUpdate(
                    "create table if not exists `" + (tableprefix != null ? tableprefix : "") + "shadowban`(" +
                            "`uuid`             VARCHAR(36)        NOT NULL," +
                            "`playername`       VARCHAR(16)        NOT NULL," +
                            "`bantime`          BIGINT             NOT NULL," +
                            " PRIMARY KEY (`uuid`)" +
                            ") DEFAULT CHARSET = utf8mb4;");
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

    private boolean isConnected() {
        try {
            return hikari.getConnection() != null;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public OptionalLong load(OfflinePlayer player) {
        String tableprefix = config.getString("database.tableprefix");
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM " + (tableprefix != null ? tableprefix : "") + "shadowban WHERE uuid=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = statement.executeQuery()) {
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
        String tableprefix = config.getString("database.tableprefix");
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "INSERT INTO " + (tableprefix != null ? tableprefix : "") + "shadowban (`uuid`, `playername`, `bantime`) VALUES (?,?,?)" +
                             " on duplicate key update playername=values(playername), bantime=values(bantime)")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setLong(3, time);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(OfflinePlayer player) {
        String tableprefix = config.getString("database.tableprefix");
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement("DELETE FROM " + (tableprefix != null ? tableprefix : "") + "shadowban WHERE uuid=?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
        }
    }
}
