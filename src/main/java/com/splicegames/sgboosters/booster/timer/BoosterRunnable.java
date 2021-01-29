package com.splicegames.sgboosters.booster.timer;

import com.splicegames.sgboosters.BoostersPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class BoosterRunnable {

    public void initialize(final BoostersPlugin plugin) {
        new BukkitRunnable() {
            public void run() {
                plugin.getBoosterStorage().decrementBoosterTimers();
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

}
