package cn.loyisa.shadowban.tasks;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.utils.TaskUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanTask implements Runnable {

    private final ShadowBan shadowBan;

    public BanTask(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    @Override
    public void run() {
        for (UUID uuid : shadowBan.getBanManager().getBanReadyPlayers()) {
            OfflinePlayer offp = Bukkit.getOfflinePlayer(uuid);
            if (offp == null || !offp.isOnline()){
                return;
            }
            Player player = offp.getPlayer();
            FileConfiguration config = shadowBan.getConfigManager().getConfig();
            // 判断指令配置
            if (config.isString("banCommands")) {
                // 运行指令 并处理Placeholder
                TaskUtils.task(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, config.getString("banCommands"))));
            } else if (config.isList("banCommands")) {
                TaskUtils.task(() -> {
                    for (String command : config.getStringList("banCommands")) {
                        // 运行指令 并处理Placeholder
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
                    }
                });
            }
            // 从数据库删除该名玩家
            shadowBan.getStorageManager().getStorageEngine().remove(player);
        }
    }
}
