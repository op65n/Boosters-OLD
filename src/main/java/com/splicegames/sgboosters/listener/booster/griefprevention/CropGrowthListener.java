package com.splicegames.sgboosters.listener.booster.griefprevention;

import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Set;
import java.util.UUID;

public final class CropGrowthListener extends ListenerRequirement {

    private final BoosterStorage storage;
    private GriefPrevention griefPrevention;

    public CropGrowthListener(final BoosterStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onCropGrowth(final BlockGrowEvent event) {
        final Block block = event.getBlock();
        final Ageable state = (Ageable) event.getNewState().getBlockData();
        final Location location = block.getLocation();
        final Claim claim = this.griefPrevention.dataStore.getClaimAt(location, false, null);
        if (claim == null) return;

        final UUID identifier = claim.getOwnerID();
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.CROP_GROWTH, identifier);
        holders.forEach(holder -> {
            event.setCancelled(true);

            final int newAge = state.getAge() + 1;
            state.setAge(newAge >= state.getMaximumAge() ? state.getMaximumAge() : newAge);
            block.setBlockData(state);
            block.getState().update();
            spawnParticles(block.getLocation());
        });
    }

    private void spawnParticles(final Location location) {
        location.setY(location.getBlockY() + 0.5D);
        location.getWorld().spawnParticle(Particle.HEART, location, 3);
    }

    public void initializeRequirements() {
        this.griefPrevention = GriefPrevention.instance;

        if (this.griefPrevention == null)
            throw new RuntimeException("Failed to retrieve GriefPrevention instance!");
    }

    public boolean isPluginInstalled() {
        return Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention") != null;
    }

    public String getRequiredPluginName() {
        return "GriefPrevention";
    }

    @Override
    public String getClassIdentifier() {
        return "Crop Growth Listener";
    }

    @Override
    public BoosterType getType() {
        return BoosterType.CROP_GROWTH;
    }
}
