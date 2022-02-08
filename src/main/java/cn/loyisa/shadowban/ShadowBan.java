package cn.loyisa.shadowban;

import cn.loyisa.shadowban.commands.CommandManager;
import cn.loyisa.shadowban.enums.Messages;
import cn.loyisa.shadowban.listeners.packet.PacketListener;
import cn.loyisa.shadowban.listeners.player.PlayerListener;
import cn.loyisa.shadowban.manager.BanManager;
import cn.loyisa.shadowban.manager.ConfigManager;
import cn.loyisa.shadowban.manager.StorageManager;
import cn.loyisa.shadowban.tasks.BanTask;
import cn.loyisa.shadowban.utils.TaskUtils;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ShadowBan extends JavaPlugin {
    private static ShadowBan instance;
    public static Logger logger;
    private PacketListener packetListener;
    private ConfigManager configManager;
    private StorageManager storageManager;
    private BanManager banManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = this.getLogger();
        logger.info(Messages.LOAD_UP.getMessage() + getDescription().getVersion());

        // 注册配置管理器
        (configManager = new ConfigManager(this)).init();
        (storageManager = new StorageManager(this)).init();
        // 注册指令
        getCommand("shadowban").setExecutor(new CommandManager(this));
        // 注册Bukkit listener
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        // 注册ProtocolLib listener
        packetListener = new PacketListener(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
        // 注册ban manager
        banManager = new BanManager(this);
        // Ban task
        TaskUtils.taskTimerAsync(new BanTask(this), 0, 20);
    }

    @Override
    public void onDisable() {
        logger.info(Messages.DISABLE.getMessage());
        this.storageManager.close();
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
        // unregister all event
        HandlerList.unregisterAll(this);
        // cancel all tasks
        getServer().getScheduler().cancelTasks(this);
        instance = null;
    }

    public static ShadowBan getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public BanManager getBanManager() {
        return banManager;
    }
}
