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
        shadowBan.getBanManager().remove(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !config.getString("method").equalsIgnoreCase("damagerekt")) {
            return;
        }
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        // 如果攻击者和被攻击者都在ShadowBan列表中
        if (shadowBan.getBanManager().isBanned(damager.getUniqueId())
                && shadowBan.getBanManager().isBanned(entity.getUniqueId())) {
            return;
        }
        // 如果攻击者在ShadowBan列表中
        if (shadowBan.getBanManager().isBanned(damager.getUniqueId())) {
            // 随机取消攻击者伤害
            if (config.getBoolean("damagerekt.randomhit") && RandomUtils.nextBoolean()) {
                event.setCancelled(true);
            } else {
                // 设置攻击者伤害
                event.setDamage(event.getFinalDamage() * config.getDouble("damagerekt.multiple1"));
                // 取消攻击击退
                if (config.getBoolean("damagerekt.cancelkb")) {
                    shadowBan.getServer().getScheduler().runTaskLater(shadowBan, () -> entity.setVelocity(new Vector()), 1L);
                }
            }
        }
        // 如果被攻击者在ShadowBan列表中
        if (shadowBan.getBanManager().isBanned(entity.getUniqueId())) {
            // 设置攻击伤害
            event.setDamage(event.getFinalDamage() * config.getDouble("damagerekt.multiple2"));
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() || shadowBan.getBanManager().isBanned(player)) {
            return;
        }
        if (config.getBoolean("damagerekt.randomplace")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() || !shadowBan.getBanManager().isBanned(player.getUniqueId())) {
            return;
        }
        if (config.getBoolean("damagerekt.randombreak")) {
            event.setCancelled(RandomUtils.nextBoolean());
        }
    }

}
