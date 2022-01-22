package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;

public class MySQL extends StorageEngine {

    private static HikariDataSource hikari;
    private static FileConfiguration config;

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
        hikari.addDataSourceProperty("password", config.get("database.password"));
        hikari.addDataSourceProperty("useUnicode", true);
        hikari.addDataSourceProperty("characterEncoding", "utf8");
        shadowBan.getLogger().info("MySQL/MariaDB connecting...");
        if (isConnected()) {
            shadowBan.getLogger().info("MySQL/MariaDB connected!");
            createTable();
        } else {
            shadowBan.getLogger().info("MySQL/MariaDB connection failed! 润了");
            shadowBan.getServer().getPluginManager().disablePlugin(shadowBan);
        }
    }

    private void createTable() {
        try (Connection con = hikari.getConnection();
             Statement statement = con.createStatement()) {
            statement.executeUpdate(
                    "create table if not exists `" + config.getString("database.tableprefix") + "shadowban`(" +
                            "`uuid`             VARCHAR(36)        NOT NULL," +
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
    public boolean load(Player player) {
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM " + config.getString("database.tableprefix") + "shadowban WHERE uuid=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = statement.executeQuery()) {
                // Query player record
                if (rs.next()) {
                    shadowBan.shadowBanMap.put(player.getUniqueId(), rs.getLong("bantime"));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void save(Player player) {
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "INSERT INTO " + config.getString("database.tableprefix") + "shadowban (`uuid`,`bantime`) VALUES (?,?)" +
                             " on duplicate key update bantime=values(bantime)")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setLong(2, shadowBan.shadowBanMap.get(player.getUniqueId()));
            statement.execute();
            shadowBan.shadowBanMap.remove(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Player player) {
        try (Connection con = hikari.getConnection();
             PreparedStatement statement = con.prepareStatement("DELETE FROM " + config.getString("database.tableprefix") + "shadowban WHERE uuid=?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.execute();
        } catch (SQLException e) {
            // if we can't get player data
            e.printStackTrace();
        }
    }
}
