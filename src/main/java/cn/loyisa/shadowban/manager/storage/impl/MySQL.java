package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;

import java.util.Map;

public class MySQL extends StorageEngine {

    public MySQL(ShadowBan shadowBan) {
        super(shadowBan);
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }

    @Override
    public Map<String, Object> load() {
        return null;
    }

    @Override
    public Long save() {
        return null;
    }
}
