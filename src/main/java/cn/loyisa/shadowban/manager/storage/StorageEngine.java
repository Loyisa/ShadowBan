package cn.loyisa.shadowban.manager.storage;

import cn.loyisa.shadowban.ShadowBan;

import java.util.Map;

public abstract class StorageEngine {

    protected final ShadowBan shadowBan;

    public StorageEngine(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void init();

    public abstract void close();

    public abstract Map<String, Object> load();

    public abstract Long save();
}