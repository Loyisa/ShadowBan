package dev.diona.shadowban.config;

import dev.diona.shadowban.AbstractManager;
import dev.diona.shadowban.ShadowBan;
import dev.diona.shadowban.enums.BanMethod;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager extends AbstractManager {

    public static BanMethod BAN_METHOD;

    public ConfigManager(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void onEnable() {
        if (!shadowBan.getDataFolder().exists()) {
            shadowBan.getDataFolder().mkdir();
        }
        shadowBan.saveDefaultConfig();
        this.loadConfig();
    }

    @Override
    public void onDisable() {

    }

    public void loadConfig() {
        FileConfiguration config = shadowBan.getConfig();
        BanMethod banMethod = BanMethod.getByName(config.getString("ban-method"));
        if (banMethod == null) {
            banMethod = BanMethod.FAKELAG;
            shadowBan.getLogger().severe("Invalid ban method: " + banMethod.name() + ", Using FAKELAG as default.");
        }
        BAN_METHOD = banMethod;
    }
}
