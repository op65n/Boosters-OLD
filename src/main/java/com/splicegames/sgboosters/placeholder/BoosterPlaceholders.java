package com.splicegames.sgboosters.placeholder;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class BoosterPlaceholders extends PlaceholderExpansion {
    private static final Set<BoosterType> BOOSTER_TYPES = Arrays.stream(BoosterType.values()).collect(Collectors.toSet());

    private final Map<BoosterType, String> descriptions = new HashMap<>();

    public BoosterPlaceholders(final BoostersPlugin plugin) {
        final FileConfiguration configuration = plugin.getConfig();
        final ConfigurationSection section = configuration.getConfigurationSection("booster-descriptions");
        if (section == null) return;

        section.getKeys(false).forEach(it -> {
            final BoosterType type = BoosterType.getNullable(it);
            if (type == null) return;

            this.descriptions.put(type, section.getString(it));
        });
    }

    public String getVersion() {
        return "1.0.2-Alpha";
    }

    public String getAuthor() {
        return "Frcsty";
    }

    public String getIdentifier() {
        return "boosters";
    }

    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        if (params.contains("booster-types")) {
            final String[] args = params.split("_", 2);
            final String formatter = args[1];
            return getStringTypes(formatter);
        }
        if (params.contains("booster-description")) {
            final String[] args = params.split(";");
            final BoosterType type = BoosterType.getNullable(args[1]);
            final String[] parts = this.descriptions.getOrDefault(type, "").split(";");
            return getSplitLore(parts, args[2]);
        }
        return null;
    }

    private String getSplitLore(final String[] parts, final String formatter) {
        return Arrays.stream(parts)
                .map(formatter::concat)
                .collect(Collectors.joining("\n"));
    }

    private String getStringTypes(final String formatter) {
        final AtomicInteger index = new AtomicInteger(BOOSTER_TYPES.size() - 1);
        final StringBuilder builder = new StringBuilder();

        BOOSTER_TYPES.forEach(type -> {
            final String name = type.name().replace("_", "\\\\_");
            builder.append(((index.getAndDecrement() == 0) ? formatter.replace(",", "") : formatter).replace("{type}", name));
        });

        return builder.toString();
    }
}
