package cn.loyisa.shadowban;

import cn.loyisa.shadowban.commands.CommandManager;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.listeners.packet.PacketListener;
import cn.loyisa.shadowban.listeners.player.PlayerListener;
import cn.loyisa.shadowban.manager.ConfigManager;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public final class ShadowBan extends JavaPlugin {
    // 储存踢出玩家的列表
    public Set<UUID> shadowBanList = new HashSet<>();
    private static ShadowBan instance;
    public static Logger logger;
    private PacketListener packetListener;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        logger.info(Messages.LOADUP.getMessage() + getDescription().getVersion());

        // 注册配置管理器
        this.configManager = new ConfigManager(this);
        // 注册指令
        getCommand("shadowban").setExecutor(new CommandManager(this));
        // 注册Bukkit listener
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        // 注册ProtocolLib listener
        packetListener = new PacketListener(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
    }

    @Override
    public void onDisable() {
        logger.info(Messages.DISABLE.getMessage());
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
        instance = null;
    }

    public static ShadowBan getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}