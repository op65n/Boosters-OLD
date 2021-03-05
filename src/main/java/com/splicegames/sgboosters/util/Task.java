package com.splicegames.sgboosters.util;

import com.splicegames.sgboosters.BoostersPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class Task {

    private static final BoostersPlugin PLUGIN = JavaPlugin.getPlugin(BoostersPlugin.class);
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    public static void async(final Runnable runnable) {
        SCHEDULER.scheduleAsyncDelayedTask(PLUGIN, runnable);
    }

    public static void queue(final Runnable runnable) {
        SCHEDULER.scheduleSyncDelayedTask(PLUGIN, runnable);
    }

}
