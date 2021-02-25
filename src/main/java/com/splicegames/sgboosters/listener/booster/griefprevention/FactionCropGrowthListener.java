package com.splicegames.sgboosters.listener.booster.griefprevention;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class FactionCropGrowthListener extends ListenerRequirement {

    private final BoosterStorage storage;

    public FactionCropGrowthListener(final BoosterStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onCropGrowth(final BlockGrowEvent event) {
        final Block block = event.getBlock();
        final Ageable state = (Ageable) event.getNewState().getBlockData();
        final Location location = block.getLocation();
        final Set<UUID> identifiers = getFactionPlayersByLocation(location);

        identifiers.forEach(identifier -> {
            final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.CROP_GROWTH, identifier);
            holders.forEach(holder -> {
                event.setCancelled(true);

                final int newAge = state.getAge() + 1;
                state.setAge(newAge >= state.getMaximumAge() ? state.getMaximumAge() : newAge);
                block.setBlockData(state);
                block.getState().update();
                spawnParticles(block.getLocation());
            });
        });
    }

    private Set<UUID> getFactionPlayersByLocation(final Location location) {
        final Set<UUID> result = new HashSet<>();

        for (final Faction faction : Factions.getInstance().getAllFactions()) {
            if (!isLocationWithinClaim(faction.getAllClaims(), location))
                continue;

            result.addAll(faction.getFPlayers().stream()
                    .map(FPlayer::getPlayer)
                    .map(Player::getUniqueId)
                    .collect(Collectors.toSet())
            );
        }

        return result;
    }

    private boolean isLocationWithinClaim(final Set<FLocation> claims, final Location location) {
        final Optional<Chunk> result = claims.stream().map(FLocation::getChunk)
                .filter(chunk -> chunk.getX() == location.getChunk().getX())
                .filter(chunk -> chunk.getZ() == location.getChunk().getZ())
                .findFirst();

        return result.isPresent();
    }

    private void spawnParticles(final Location location) {
        location.setY(location.getBlockY() + 0.5D);
        location.getWorld().spawnParticle(Particle.HEART, location, 3);
    }

    public boolean isPluginInstalled() {
        return Bukkit.getServer().getPluginManager().getPlugin("Factions") != null;
    }

    public String getRequiredPluginName() {
        return "Factions";
    }

    @Override
    public String getClassIdentifier() {
        return "Faction Crop Growth Listener";
    }
}
