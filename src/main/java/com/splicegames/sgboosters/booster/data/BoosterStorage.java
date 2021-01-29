package com.splicegames.sgboosters.booster.data;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class BoosterStorage {

    private final Set<BoosterHolder> boosterHolders = new HashSet<>();

    public void addBooster(final BoosterHolder holder) {
        this.boosterHolders.add(holder);
    }

    public void removeBooster(final BoosterHolder holder) {
        this.boosterHolders.remove(holder);
    }

    public boolean isTypeApplicable(final UUID identifier, final BoosterType type, final boolean scope) {
        final long activeBoostersOfGivenType = getBoostersApplicableToUser(identifier).stream()
                .filter(holder -> (holder.getType() == type))
                .filter(holder -> (scope != holder.isPersonal()))
                .count();

        return activeBoostersOfGivenType == 0;
    }


    public Set<BoosterHolder> getHolderOfTypeForUser(final BoosterType type, final UUID identifier) {
        return this.boosterHolders.stream()
                .filter(holder -> (holder.getType() == type))
                .filter(holder -> holder.getTarget().containsRecipient(identifier))
                .collect(Collectors.toSet());
    }

    public void decrementBoosterTimers() {
        final Set<BoosterHolder> updatedHolders = this.boosterHolders.stream()
                .map(BoosterHolder::getContent)
                .map(BoosterContent::decrementDuration)
                .filter(BoosterContent::isValid)
                .map(BoosterContent::getHolder)
                .collect(Collectors.toSet());

        this.boosterHolders.clear();
        this.boosterHolders.addAll(updatedHolders);
    }

    public Set<BoosterHolder> getBoostersApplicableToUser(final UUID identifier) {
        return this.boosterHolders.stream()
                .filter(holder -> holder.isApplicableToUser(identifier))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load(final BoostersPlugin plugin) {
        final File file = new File(plugin.getDataFolder(), "booster-storage.yml");
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        for (final String key : yamlConfiguration.getKeys(false)) {
            final ConfigurationSection section = yamlConfiguration.getConfigurationSection(key);
            if (section == null) continue;

            this.boosterHolders.add(
                    BoosterHolder.deserialize(section.getValues(false))
            );
        }

        file.delete();
    }

    public void save(final BoostersPlugin plugin) {
        try {
            final File file = new File(plugin.getDataFolder(), "booster-storage.yml");
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

            for (final BoosterHolder holder : this.boosterHolders)
                yamlConfiguration.set(UUID.randomUUID().toString(), holder.serialize());

            yamlConfiguration.save(file);
        } catch (final IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save Active Boosters to a file!");
        }
    }

}
