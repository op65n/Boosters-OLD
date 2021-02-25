package com.splicegames.sgboosters.listener.registerable;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.listener.booster.BlockBreakListener;
import com.splicegames.sgboosters.listener.booster.ExperienceGainListener;
import com.splicegames.sgboosters.listener.booster.griefprevention.CropGrowthListener;
import com.splicegames.sgboosters.listener.booster.griefprevention.EntityDeathListener;
import com.splicegames.sgboosters.listener.booster.griefprevention.FactionCropGrowthListener;
import com.splicegames.sgboosters.listener.booster.mcmmo.McMMOGainListener;
import com.splicegames.sgboosters.listener.booster.shopguiplus.ShopGUIPlusListener;
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
                new FactionCropGrowthListener(boosterStorage),
                new CropGrowthListener(boosterStorage),
                new EntityDeathListener(boosterStorage),
                new McMMOGainListener(plugin),
                new ShopGUIPlusListener(plugin),
                new ExperienceGainListener(plugin),
                new BlockBreakListener(plugin),

                new VoucherUseListener(plugin),
                new PlayerJoinListener(plugin)
        ).forEach(it -> {
            if (!it.isPluginInstalled()) {
                logger.log(Level.INFO, String.format(
                        "Skipping \"%s\" registration, as plugin %s is not installed.",
                        it.getClassIdentifier(), it.getRequiredPluginName()
                ));
                return;
            }
            it.initializeRequirements();
            pluginManager.registerEvents(it, plugin);
        });
    }

}
