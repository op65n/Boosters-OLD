package com.splicegames.sgboosters.listener.booster.griefprevention;

import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.UUID;

public final class EntityDeathListener extends ListenerRequirement {

    private static final SplittableRandom RANDOM = new SplittableRandom();
    private final BoosterStorage storage;
    private GriefPrevention griefPrevention;

    public EntityDeathListener(final BoosterStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        Claim claim = this.griefPrevention.dataStore.getClaimAt(location, false, null);
        if (claim == null) return;

        final UUID identifier = claim.getOwnerID();
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.MOB_DROPS, identifier);
        holders.forEach(holder -> {
            final BoosterContent content = holder.getContent();
            final double magnitude = content.getMagnitude();

            final List<ItemStack> drops = event.getDrops();
            if (drops.size() == 0) return;

            manageDrops(magnitude, drops);
        });
    }

    private void manageDrops(final double magnitude, final List<ItemStack> drops) {
        final int randomDropIndex = drops.size() == 1 ? 0 : RANDOM.nextInt(0, drops.size() - 1);
        final ItemStack item = drops.remove(randomDropIndex);

        item.setAmount(item.getAmount() + getAmountAddition(magnitude));
        drops.add(item);
    }

    private int getAmountAddition(final double magnitude) {
        return magnitude <= 1.5D ? 1 : 2;
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
        return "Entity Death Listener";
    }
}
