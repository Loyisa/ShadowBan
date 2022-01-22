package cn.loyisa.shadowban.manager.storage;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class StorageEngine {

    protected final ShadowBan shadowBan;

    public StorageEngine(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void init();

    public abstract void close();

    public abstract Long load(Player player);

    public abstract void save(Player player, Long timestamp);
}