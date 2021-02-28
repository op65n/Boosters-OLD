package com.splicegames.sgboosters.listener.booster.mcmmo;

import com.github.frcsty.frozenactions.util.Replace;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.util.player.UserManager;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Set;

public final class McMMOGainListener extends ListenerRequirement {

    private final FileConfiguration configuration;
    private final BoosterStorage storage;

    public McMMOGainListener(final BoostersPlugin plugin) {
        this.storage = plugin.getBoosterStorage();
        this.configuration = plugin.getConfig();
    }

    @EventHandler
    public void onMcMMOGain(final McMMOPlayerXpGainEvent event) {
        final Player player = event.getPlayer();
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.MCMMO_GAIN, player.getUniqueId());

        holders.forEach(holder -> {
            final BoosterContent content = holder.getContent();

            final float level = event.getRawXpGained();
            final float boosterAddition = (float) (level * content.getMagnitude() - level);

            UserManager.getPlayer(player).setSkillXpLevel(
                    event.getSkill(),
                    boosterAddition
            );

            Message.send(player, Replace.replaceList(
                    this.configuration.getStringList("booster-message.mcmmo-experience-gain-message"),
                    "{magnitude}", content.getMagnitude(),
                    "{amount}", boosterAddition,
                    "{owner}", holder.getOwner().getName(),
                    "{duration}", TimeDisplay.getFormattedTime(holder.getContent().getDuration())
            ));
        });
    }

    @Override
    public String getRequiredPluginName() {
        return "mcMMO";
    }

    @Override
    public boolean isPluginInstalled() {
        return Bukkit.getServer().getPluginManager().getPlugin("mcMMO") != null;
    }

    @Override
    public String getClassIdentifier() {
        return "McMMO Gain Listener";
    }

    @Override
    public BoosterType getType() {
        return BoosterType.MCMMO_GAIN;
    }
}
