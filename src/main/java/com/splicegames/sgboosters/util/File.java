package com.splicegames.sgboosters.util;

import com.splicegames.sgboosters.BoostersPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class File {

    private static final BoostersPlugin PLUGIN = JavaPlugin.getPlugin(BoostersPlugin.class);

    public static void saveResources(final String... paths) {
        Arrays.stream(paths).forEach(it -> {
            if (!(new java.io.File(PLUGIN.getDataFolder(), it)).exists())
                PLUGIN.saveResource(it, false);
        });
    }
}
