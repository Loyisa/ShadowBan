package cn.loyisa.shadowban.manager.storage;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.entity.Player;

public abstract class StorageEngine {

    protected final ShadowBan shadowBan;

    public StorageEngine(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void init();

    public abstract void close();

    public abstract boolean load(Player player);

    public abstract void save(Player player);

    public abstract void remove(Player player);
}