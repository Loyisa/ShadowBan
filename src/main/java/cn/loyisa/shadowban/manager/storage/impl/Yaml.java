package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Yaml extends StorageEngine {

    private File playerDataDir;

    public Yaml(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void init() {
        playerDataDir = new File(shadowBan.getDataFolder(), "PlayerData");
        if (!playerDataDir.exists()) {
            playerDataDir.mkdir();
        }

    }

    @Override
    public void close() {

    }


    // 如果true就说明这人在ShadowBan列表里
    @Override
    public boolean load(Player player) {
        File playerDataFile = new File(playerDataDir, player.getUniqueId() + ".yml");
        // 检查是否有数据
        if (!playerDataFile.exists()) {
            return false;
        } else {
            // 如果有数据 读取封禁时间
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);
            // is player's data file empty?
            if (config.getKeys(true).size() == 0) {
                playerDataFile.delete();
                return false;
            }
            // 放Map里
            shadowBan.shadowBanMap.put(player.getUniqueId(), config.getLong("BanTime"));
            return true;
        }
    }

    @Override
    public void save(Player player) {
        File playerDataFile = new File(playerDataDir, player.getUniqueId() + ".yml");
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);
        config.set("PlayerName", player.getName());
        config.set("BanTime", shadowBan.shadowBanMap.get(player.getUniqueId()));
        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Player player) {
        File playerDataFile = new File(playerDataDir, player.getUniqueId() + ".yml");
        if (playerDataFile.exists()) {
            playerDataFile.delete();
        }
    }


}
