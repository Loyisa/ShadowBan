package cn.loyisa.shadowban.listeners.player;

import cn.loyisa.shadowban.ShadowBan;
import cn.loyisa.shadowban.utils.RandomUtils;
import cn.loyisa.shadowban.utils.TaskUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private final ShadowBan shadowBan;
    private final FileConfiguration config;


    public PlayerListener(ShadowBan shadowBan) {
        this.shadowBan = shadowBan;
        this.config = this.shadowBan.getConfigManager().getConfig();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TaskUtils.taskAsync(() -> shadowBan.getStorageManager().getStorageEngine().load(event.getPlayer()));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        shadowBan.shadowBanMap.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !config.getString("method").equalsIgnoreCase("damagerekt")) {
            return;
        }
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (shadowBan.shadowBanMap.containsKey(damager.getUniqueId())) {
            if (config.getBoolean("damagerekt.randomhit")) {
                event.setCancelled(RandomUtils.nextBoolean());
            }
            event.setDamage(0);
            if (config.getBoolean("damagerekt.cancelkb")) {
                shadowBan.getServer().getScheduler().runTaskLater(shadowBan, () -> entity.setVelocity(new Vector()), 1L);
            }
        }
        if (shadowBan.shadowBanMap.containsKey(entity.getUniqueId())) {
            event.setDamage(event.getDamage() * config.getDouble("damagerekt.multiple"));
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() || !shadowBan.shadowBanMap.containsKey(player.getUniqueId())) {
            return;
        }

        if (config.getBoolean("damagerekt.randomplace")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() || !shadowBan.shadowBanMap.containsKey(player.getUniqueId())) {
            return;
        }
        if (config.getBoolean("damagerekt.randombreak")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

}
