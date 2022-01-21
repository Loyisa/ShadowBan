package cn.loyisa.shadowban.listeners.player;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.utils.RandomUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private ShadowBan shadowBan;

    public PlayerListener(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        shadowBan.shadowBanList.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if (!shadowBan.getConfigManager().getConfig().getString("method").equalsIgnoreCase("damagerekt")) {
            return;
        }
        if (shadowBan.shadowBanList.contains(damager.getUniqueId())) {
            event.setDamage(0);
            if (shadowBan.getConfigManager().getConfig().getBoolean("damagerekt.cancelkb")) {
                shadowBan.getServer().getScheduler().runTaskLater(shadowBan, () -> entity.setVelocity(new Vector()), 1L);
            }
        }
        if (shadowBan.shadowBanList.contains(entity.getUniqueId())) {
            event.setDamage(event.getDamage() * shadowBan.getConfigManager().getConfig().getDouble("damagerekt.multiple"));
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!shadowBan.shadowBanList.contains(player.getUniqueId())) {
            return;
        }

        if (shadowBan.getConfigManager().getConfig().getBoolean("modules.randomplace")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!shadowBan.shadowBanList.contains(player.getUniqueId())) {
            return;
        }
        if (shadowBan.getConfigManager().getConfig().getBoolean("modules.randombreak")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

}
