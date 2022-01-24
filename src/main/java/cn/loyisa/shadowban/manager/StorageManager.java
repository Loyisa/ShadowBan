package cn.loyisa.shadowban.manager;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.manager.storage.StorageEngine;
import cn.loyisa.shadowban.manager.storage.impl.MySQL;
import cn.loyisa.shadowban.manager.storage.impl.Yaml;

public class StorageManager {

    private StorageEngine storageEngine;

    private final ShadowBan shadowBan;

    public StorageManager(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public void init() {
        // Load the storage-method in cfg
        switch (shadowBan.getConfigManager().getConfig().getString("storage-method").toLowerCase()) {
            case "mysql":
                this.storageEngine = new MySQL(shadowBan);
                shadowBan.getLogger().info("Storage method: MySQL");
                break;
            case "yaml":
                this.storageEngine = new Yaml(shadowBan);
                shadowBan.getLogger().info("Storage method: Yaml");
                break;
            default:
                shadowBan.getLogger().info("No storage method! Shutting down server... ");
                shadowBan.getServer().shutdown();
        }
        this.storageEngine.init();
    }

    public void close() {
        this.storageEngine.close();
    }

    public StorageEngine getStorageEngine() {
        return storageEngine;
    }
}
