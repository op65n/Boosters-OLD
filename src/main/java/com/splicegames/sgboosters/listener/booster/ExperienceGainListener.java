package com.splicegames.sgboosters.listener.booster;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.text.DecimalFormat;
import java.util.Set;

public final class ExperienceGainListener extends ListenerRequirement {

    private final DecimalFormat format = new DecimalFormat("###.##");

    private final FileConfiguration configuration;
    private final BoosterStorage storage;

    public ExperienceGainListener(final BoostersPlugin plugin) {
        this.storage = plugin.getBoosterStorage();
        this.configuration = plugin.getConfig();
    }

    @EventHandler
    public void onExperienceGain(final PlayerExpChangeEvent event) {
        final Player player = event.getPlayer();
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.EXPERIENCE_GAIN, player.getUniqueId());

        holders.forEach(holder -> {
            final BoosterContent content = holder.getContent();

            final int gained = event.getAmount();
            final double boosterAddition = (gained * content.getMagnitude()) - gained;

            player.giveExp((int) boosterAddition);
            Message.send(player, Replace.replaceList(
                    this.configuration.getStringList("booster-message.experience-gain-message"),
                    "{magnitude}", content.getMagnitude(),
                    "{amount}", this.format.format(boosterAddition),
                    "{owner}", holder.getOwner().getName(),
                    "{duration}", TimeDisplay.getFormattedTime(holder.getContent().getDuration())
            ));
        });
    }

    @Override
    public boolean isPluginInstalled() {
        return true;
    }

    @Override
    public String getRequiredPluginName() {
        return "None";
    }

    @Override
    public String getClassIdentifier() {
        return "Experience Gain Listener";
    }
}
