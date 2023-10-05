package dev.diona.shadowban;

public abstract class AbstractManager {

    protected final ShadowBan shadowBan;

    public AbstractManager(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
