package cn.loyisa.shadowban.listeners.packet;

import cn.loyisa.shadowban.ShadowBan;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import java.util.ArrayList;
import java.util.List;

public class PacketListener extends PacketAdapter {

    private final ShadowBan shadowBan;

    private static final List<PacketType> LISTENING_PACKETS = new ArrayList<>();

    static {
        //Listen for every single client packet
        for (PacketType packetType : PacketType.Play.Client.getInstance().values()) {
            if (packetType.isSupported()) {
                LISTENING_PACKETS.add(packetType);
            }
        }
        //Listen for every single server packet
        for (PacketType packetType : PacketType.Play.Server.getInstance().values()) {
            if (packetType.isSupported()) {
                LISTENING_PACKETS.add(packetType);
            }
        }
    }

    public PacketListener(ShadowBan shadowBan) {
        super(shadowBan, ListenerPriority.MONITOR, LISTENING_PACKETS);
        this.shadowBan = shadowBan;
    }

    public void onPacketSending(PacketEvent event) {
        if (shadowBan.shadowBanList.contains(event.getPlayer().getUniqueId())
                && shadowBan.getConfigManager().getConfig().getString("method").equalsIgnoreCase("fakelag")) {
            event.setReadOnly(false);
            event.setCancelled(true);
        }

    }

    public void onPacketReceiving(PacketEvent event) {
        if (shadowBan.shadowBanList.contains(event.getPlayer().getUniqueId())
                && shadowBan.getConfigManager().getConfig().getString("method").equalsIgnoreCase("fakelag")) {
            event.setReadOnly(false);
            event.setCancelled(true);
        }
    }

}
