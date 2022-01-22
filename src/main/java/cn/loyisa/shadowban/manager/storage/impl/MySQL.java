package cn.loyisa.shadowban.manager.storage.impl;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import org.bukkit.entity.Player;

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
    public Long load(Player player) {
        return null;
    }

    @Override
    public void save(Player player, Long timestamp) {

    }
}
