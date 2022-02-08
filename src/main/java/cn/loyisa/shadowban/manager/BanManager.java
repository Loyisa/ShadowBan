package cn.loyisa.shadowban.manager;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.utils.RandomUtils;
import cn.loyisa.shadowban.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 封禁管理模块
 *
 * 管理玩家是否被shadow ban, 以及处理玩家ban/unban
 *
 * @author RE
 */
public class BanManager{
    // 主类依赖
    private final ShadowBan shadowBan;

    // 储存踢出玩家的列表
    private final Map<UUID, Long> shadowBanMap = new ConcurrentHashMap<>();

    // 依赖注入
    public BanManager(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;

        // PlugMan支持
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.load(onlinePlayer);
        }
    }

    /**
     * 玩家是否被shadowban
     *
     * @param player 检查的玩家
     * @return 是否被shadow ban
     */
    public boolean isBanned(Player player) {
        return this.isBanned(player.getUniqueId());
    }

    /**
     * 玩家是否被shadowban
     *
     * @param uuid 检查的玩家UUID
     * @return 是否被shadow ban
     */
    public boolean isBanned(UUID uuid){
        return shadowBanMap.containsKey(uuid);
    }

    /**
     * Shadow Ban该玩家
     *
     * @param player 要封禁的玩家
     */
    public void ban(OfflinePlayer player) {
        FileConfiguration config = shadowBan.getConfigManager().getConfig();
        Long banTime = System.currentTimeMillis()
                + RandomUtils.nextLong(config.getLong("banwave.minbantime"), config.getLong("banwave.maxbantime")) * 1000;

        // 只在该玩家在线的时候丢进缓存
        if(player.isOnline()) {
            this.add(player.getUniqueId(), banTime);
        }

        TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().save(player, banTime));
    }

    /**
     * 解封该玩家
     *
     * @param player 玩家对象
     */
    public void unban(OfflinePlayer player) {
        shadowBanMap.remove(player.getUniqueId());
        TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().remove(player));
    }

    /**
     * 为该玩家加载数据
     *
     * @param player 玩家对象
     */
    public void load(Player player){
        TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().load(player));
    }

    /**
     * 将玩家添加到 shadow ban 缓存
     */
    public void add(UUID uuid, Long time){
        if(Bukkit.getPlayer(uuid) == null) return;

        shadowBanMap.put(uuid, time);
    }

    /**
     * 将玩家移除出 shadow ban缓存
     *
     * @param player 玩家名字
     */
    public void remove(Player player) {
        shadowBanMap.remove(player.getUniqueId());
    }

    /**
     * 获取封禁ready的玩家
     *
     * (TODO: Java Stream性能似乎不是很好，是不是应该改成普通forEach?)
     *
     * @return 封禁ready的玩家
     */
    public Set<UUID> getBanReadyPlayers() {
        return shadowBanMap.entrySet().stream()
                .filter(entry -> System.currentTimeMillis() >= entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Map<UUID, Long> getShadowBanMap() {
        return shadowBanMap;
    }
}
