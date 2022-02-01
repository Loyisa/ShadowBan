package cn.loyisa.shadowban.manager.storage;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class StorageEngine {

    protected final ShadowBan shadowBan;

    public StorageEngine(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void init();

    public abstract void close();

    public abstract boolean load(OfflinePlayer player);

    public abstract void save(OfflinePlayer player);

    public abstract void remove(OfflinePlayer player);
}