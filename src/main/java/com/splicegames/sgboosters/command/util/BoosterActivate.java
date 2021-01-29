package com.splicegames.sgboosters.command.util;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class BoosterActivate {

    private final FileConfiguration configuration;
    private final BoosterStorage storage;

    public BoosterActivate(final BoostersPlugin plugin) {
        this.configuration = plugin.getConfig();
        this.storage = plugin.getBoosterStorage();
    }

    public boolean activateBooster(final Player player, final BoosterHolder holder) {
        if (!this.storage.isTypeApplicable(player.getUniqueId(), holder.getType(), !holder.isPersonal())) {
            Message.send(player, this.configuration.getStringList("message.un-applicable-booster-type"));
            return false;
        }

        this.storage.addBooster(holder);
        Message.send(player, Replace.replaceList(
                this.configuration.getStringList("message.activated-booster"),
                "{formatted-type}", WordUtils.capitalize(holder.getType().name().replace("_", " ").toLowerCase()),
                "{type}", holder.getType().name(),
                "{magnitude}", holder.getContent().getMagnitude(),
                "{duration}", TimeDisplay.getFormattedTime(holder.getContent().getDuration()),
                "{scope}", holder.isPersonal() ? "Personal" : "Global")
        );

        return true;
    }


}
