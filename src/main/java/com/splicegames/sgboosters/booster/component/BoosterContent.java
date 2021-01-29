package com.splicegames.sgboosters.booster.component;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.message.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class BoosterContent {

    private final BoosterHolder holder;
    private final BoostersPlugin plugin;
    private double magnitude;
    private long duration;

    public BoosterContent(final BoosterHolder holder, final BoostersPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
    }

    public void of(final double magnitude, final long duration) {
        this.magnitude = magnitude;
        this.duration = duration;
    }

    public BoosterContent decrementDuration() {
        this.duration--;
        return this;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public long getDuration() {
        return this.duration;
    }

    public boolean isValid() {
        final boolean valid = this.duration > 0L;

        if (!valid) notifyOwner();

        return valid;
    }

    private void notifyOwner() {
        final OfflinePlayer offlinePlayer = this.holder.getOwner();
        if (!offlinePlayer.isOnline()) return;

        final Player player = offlinePlayer.getPlayer();
        if (player == null) return;

        Message.send(player, Replace.replaceList(
                this.plugin.getConfig().getStringList("message.booster-expired"),
                "{formatted-type}", WordUtils.capitalize(this.holder.getType().name().replace("_", " ").toLowerCase())
        ));
    }

    public BoosterHolder getHolder() {
        return this.holder;
    }

}
