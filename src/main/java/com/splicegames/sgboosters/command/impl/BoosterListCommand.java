package com.splicegames.sgboosters.command.impl;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.command.menu.BoosterListMenu;
import com.splicegames.sgboosters.util.Task;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;

import java.util.logging.Level;

@Command("boosters")
public final class BoosterListCommand extends CommandBase {
    private final BoostersPlugin plugin;

    public BoosterListCommand(final BoostersPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("sgboosters.command.list")
    public void onListCommand(final Player player) {
        Task.async(() -> {
            final BoosterListMenu listMenu = new BoosterListMenu(this.plugin, player);
            final PaginatedGui menu = listMenu.getMenu();

            if (menu == null) {
                this.plugin.getLogger().log(Level.WARNING, String.format(
                        "Failed to Construct & Open Booster ListBuilder Menu for Player '%s'", player.getName()
                ));
                return;
            }

            Task.queue(() -> menu.open(player));
        });
    }
}
