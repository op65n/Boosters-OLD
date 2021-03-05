package com.splicegames.sgboosters.command.impl;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.Task;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

@Command("boosters")
@Alias({"boost", "booster"})
public final class BoosterReloadCommand extends CommandBase {

    private final BoostersPlugin plugin;

    public BoosterReloadCommand(final BoostersPlugin plugin) {
        this.plugin = plugin;
    }

    @SubCommand("reload")
    @Permission("sgboosters.command.admin.reload")
    public void onReloadCommand(final CommandSender sender) {
        Task.async(() -> {
            final long start = System.currentTimeMillis();

            this.plugin.reloadConfig();

            Message.send(sender, Replace.replaceList(
                    this.plugin.getConfig().getStringList("message.reloaded-plugin"),
                    "{time}", System.currentTimeMillis() - start
            ));
        });
    }

}
