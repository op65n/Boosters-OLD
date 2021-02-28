package com.splicegames.sgboosters.util.booster;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class TypeRegistry {

    private static final Set<BoosterType> TYPE_REGISTRY = new HashSet<>();

    static {
        final BoostersPlugin plugin = JavaPlugin.getPlugin(BoostersPlugin.class);
        final FileConfiguration configuration = plugin.getConfig();
        final ConfigurationSection enabledBoosters = configuration.getConfigurationSection("enabled-types");

        if (enabledBoosters == null) {
            throw new RuntimeException("Configuration section 'enabled-types' could not be found!");
        }

        for (final String key : enabledBoosters.getKeys(false)) {
            final BoosterType type = BoosterType.getNullable(key);

            if (type == null) {
                plugin.getLogger().log(Level.WARNING, String.format(
                        "BoosterType for input '%s' is invalid", key
                ));
                continue;
            }

            TYPE_REGISTRY.add(type);
        }
    }

    public static boolean isEnabled(final BoosterType type) {
        if (type == null) return true;

        return TYPE_REGISTRY.contains(type);
    }

}
