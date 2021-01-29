package com.splicegames.sgboosters.command.registerable;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.command.BoosterGiveCommand;
import com.splicegames.sgboosters.command.BoosterListCommand;
import com.splicegames.sgboosters.registry.Registerable;
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
                new BoosterListCommand(plugin)
        );
    }

    private void assignCompletions(final CompletionHandler completionHandler) {
        completionHandler.register("#boosters", resolver -> Arrays.stream(BoosterType.values()).map(Enum::name).collect(Collectors.toList()));
        completionHandler.register("#targets", resolver -> {
            final List<String> result = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
            result.add("ALL");

            return result;
        });
    }

}