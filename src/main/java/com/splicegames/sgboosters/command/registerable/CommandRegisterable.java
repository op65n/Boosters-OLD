package com.splicegames.sgboosters.command.registerable;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.command.impl.BoosterGiveCommand;
import com.splicegames.sgboosters.command.impl.BoosterListCommand;
import com.splicegames.sgboosters.command.impl.BoosterReloadCommand;
import com.splicegames.sgboosters.registry.Registerable;
import com.splicegames.sgboosters.util.booster.TypeRegistry;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.CompletionHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandRegisterable implements Registerable {

    public void register(final BoostersPlugin plugin) {
        final CommandManager commandManager = new CommandManager(plugin);
        final CompletionHandler completionHandler = commandManager.getCompletionHandler();
        assignCompletions(completionHandler);
        commandManager.register(
                new BoosterGiveCommand(plugin),
                new BoosterListCommand(plugin),
                new BoosterReloadCommand(plugin)
        );
    }

    private void assignCompletions(final CompletionHandler completionHandler) {
        completionHandler.register("#boosters", resolver -> Arrays.stream(BoosterType.values())
                .filter(TypeRegistry::isEnabled)
                .map(Enum::name)
                .collect(Collectors.toList()));

        completionHandler.register("#targets", resolver -> {
            final List<String> result = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
            result.add("ALL");

            return result;
        });
    }

}
