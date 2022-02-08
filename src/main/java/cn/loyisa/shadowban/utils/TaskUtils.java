package cn.loyisa.shadowban.utils;

import cn.loyisa.shadowban.ShadowBan;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtils {
    private static final ShadowBan shadowBan = ShadowBan.getInstance();

    public static BukkitTask taskTimer(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(shadowBan, runnable, delay, interval);
    }

    public static BukkitTask taskTimerAsync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(shadowBan, runnable, delay, interval);
    }

    public static BukkitTask task(Runnable runnable) {
        return Bukkit.getScheduler().runTask(shadowBan, runnable);
    }

    public static BukkitTask taskAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(shadowBan, runnable);
    }

    public static BukkitTask taskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(shadowBan, runnable, delay);
    }

    public static BukkitTask taskLaterAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(shadowBan, runnable, delay);
    }
}