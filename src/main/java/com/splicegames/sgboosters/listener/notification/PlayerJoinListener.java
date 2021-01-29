package com.splicegames.sgboosters.listener.notification;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import com.splicegames.sgboosters.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener extends ListenerRequirement {

    private final BoostersPlugin plugin;

    public PlayerJoinListener(final BoostersPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final BoosterStorage storage = this.plugin.getBoosterStorage();

        if (storage.getBoostersApplicableToUser(player.getUniqueId()).isEmpty()) return;

        Message.send(player, this.plugin.getConfig().getStringList("message.active-boosters"));
    }

    public String getRequiredPluginName() {
        return "None";
    }

    public boolean isPluginInstalled() {
        return true;
    }

}
