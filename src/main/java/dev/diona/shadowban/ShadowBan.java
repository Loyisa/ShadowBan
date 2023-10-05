package dev.diona.shadowban;

import dev.diona.shadowban.config.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShadowBan extends JavaPlugin {

    @Getter
    private static ShadowBan instance;
    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        // load config first
        (configManager = new ConfigManager(this)).onEnable();
    }

    @Override
    public void onDisable() {
    }
}
