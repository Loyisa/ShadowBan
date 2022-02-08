package cn.loyisa.shadowban.manager.storage;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.OptionalLong;

public abstract class StorageEngine {

    protected final ShadowBan shadowBan;

    public StorageEngine(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void init();

    public abstract void close();

    /**
     * 加载玩家封禁时间, 如果没有被封禁返回 OptionalLong.empty()
     *
     * @param player 要加载的玩家
     * @return 封禁时间
     */
    public abstract OptionalLong load(OfflinePlayer player);

    /**
     * 保存玩家封禁时间
     *
     * @param player 玩家对象
     * @param time 封禁时间
     */
    public abstract void save(OfflinePlayer player, Long time);

    /**
     * 移除玩家数据
     *
     * @param player 玩家对象
     */
    public abstract void remove(OfflinePlayer player);
}