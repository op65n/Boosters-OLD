package com.splicegames.sgboosters.listener.registerable;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.listener.booster.CropGrowthListener;
import com.splicegames.sgboosters.listener.booster.EntityDeathListener;
import com.splicegames.sgboosters.listener.booster.McMMOGainListener;
import com.splicegames.sgboosters.listener.booster.ShopSellListener;
import com.splicegames.sgboosters.listener.notification.PlayerJoinListener;
import com.splicegames.sgboosters.listener.voucher.VoucherUseListener;
import com.splicegames.sgboosters.registry.Registerable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class ListenerRegisterable implements Registerable {

    public void register(final BoostersPlugin plugin) {
        final BoosterStorage boosterStorage = plugin.getBoosterStorage();
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final Logger logger = plugin.getLogger();

        Stream.of(
                new CropGrowthListener(boosterStorage),
                new EntityDeathListener(boosterStorage),
                new McMMOGainListener(plugin),
                new ShopSellListener(plugin),
                new VoucherUseListener(plugin),
                new PlayerJoinListener(plugin)
        ).forEach(it -> {
            if (!it.isPluginInstalled()) {
                logger.log(Level.INFO, String.format(
                        "Skipping %s registration, as plugin %s is not installed.",
                        it.getClass().getName().replace(".", " ").split(" ")[4], it.getRequiredPluginName()
                ));
                return;
            }
            it.initializeRequirements();
            pluginManager.registerEvents(it, plugin);
        });
    }

}
