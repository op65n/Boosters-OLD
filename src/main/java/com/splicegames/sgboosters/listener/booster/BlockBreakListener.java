package com.splicegames.sgboosters.listener.booster;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import com.splicegames.sgboosters.util.Task;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Set;
import java.util.SplittableRandom;

public final class BlockBreakListener extends ListenerRequirement {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    private final BoosterStorage storage;

    public BlockBreakListener(final BoostersPlugin plugin) {
        this.storage = plugin.getBoosterStorage();
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        Task.async(() -> {
            final Player player = event.getPlayer();
            final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.BLOCK_BREAK, player.getUniqueId());

            final Block block = event.getBlock();
            holders.forEach(holder -> {
                final BoosterContent content = holder.getContent();

                final double chance = RANDOM.nextDouble(content.getMagnitude() * 1000);
                if (chance <= 75) {
                    Task.queue(() -> block.breakNaturally(player.getInventory().getItemInMainHand(), true));
                }
            });
        });
    }

    @Override
    public String getRequiredPluginName() {
        return "None";
    }

    @Override
    public boolean isPluginInstalled() {
        return true;
    }

    @Override
    public String getClassIdentifier() {
        return "Block Break Listener";
    }

    @Override
    public BoosterType getType() {
        return BoosterType.BLOCK_BREAK;
    }
}
