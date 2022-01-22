package cn.loyisa.shadowban.manager;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private ShadowBan shadowBan;

    private File file;
    private FileConfiguration config;

    public ConfigManager(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public void init() {
        if (!shadowBan.getDataFolder().exists()) {
            shadowBan.getDataFolder().mkdir();
        }

        shadowBan.saveDefaultConfig();
        file = new File(shadowBan.getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
