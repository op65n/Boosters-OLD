package com.splicegames.sgboosters;

import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.timer.BoosterRunnable;
import com.splicegames.sgboosters.command.registerable.CommandRegisterable;
import com.splicegames.sgboosters.listener.registerable.ListenerRegisterable;
import com.splicegames.sgboosters.placeholder.BoosterPlaceholders;
import com.splicegames.sgboosters.registry.Registerable;
import com.splicegames.sgboosters.util.File;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class BoostersPlugin extends JavaPlugin {

    private final BoosterStorage boosterStorage = new BoosterStorage();
    private final BoosterRunnable boosterRunnable = new BoosterRunnable();

    private final Set<Registerable> registerableSet = new HashSet<>(Arrays.asList(
            new ListenerRegisterable(), new CommandRegisterable()
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        File.saveResources(
                "menu/booster-list-menu.yml"
        );

        this.registerableSet.forEach(it -> it.register(this));

        new BoosterPlaceholders(this).register();

        this.boosterRunnable.initialize(this);
        this.boosterStorage.load(this);
    }

    @Override
    public void onDisable() {
        reloadConfig();

        this.boosterStorage.save(this);
    }

    public BoosterStorage getBoosterStorage() {
        return this.boosterStorage;
    }

}
