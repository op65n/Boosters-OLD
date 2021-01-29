package com.splicegames.sgboosters.booster.holder;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.component.BoosterTarget;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BoosterHolder implements ConfigurationSerializable {

    private final OfflinePlayer owner;
    private final BoosterType type;
    private final BoosterTarget target;
    private final BoosterContent content;

    static {
        ConfigurationSerialization.registerClass(BoosterHolder.class);
    }

    public BoosterHolder(final OfflinePlayer player, final BoosterType type, final BoosterTarget target) {
        this.owner = player;
        this.type = type;
        this.target = target;

        final BoostersPlugin plugin = JavaPlugin.getPlugin(BoostersPlugin.class);
        this.content = new BoosterContent(this, plugin);
    }

    public void assignContents(final double magnitude, final long duration) {
        this.content.of(magnitude, duration);
    }

    public boolean isPersonal() {
        return this.target.getRecipientSetSize() == 1;
    }

    public boolean isApplicableToUser(final UUID identifier) {
        return this.target.containsRecipient(identifier);
    }

    public BoosterType getType() {
        return this.type;
    }

    public BoosterTarget getTarget() {
        return this.target;
    }

    public BoosterContent getContent() {
        return this.content;
    }

    public OfflinePlayer getOwner() {
        return this.owner;
    }

    @NotNull
    public Map<String, Object> serialize() {
        final Map<String, Object> data = new HashMap<>();

        data.put("owner", this.owner.getUniqueId().toString());
        data.put("type", this.type.name());
        data.put("target", this.target.serialize());
        data.put("duration", this.content.getDuration());
        data.put("magnitude", this.content.getMagnitude());

        return data;
    }

    public static BoosterHolder deserialize(final Map<String, Object> serialization) {
        final BoosterHolder holder = new BoosterHolder(
                Bukkit.getOfflinePlayer(UUID.fromString((String) serialization.get("owner"))),
                BoosterType.valueOf((String) serialization.get("type")),
                BoosterTarget.deserialize((ConfigurationSection) serialization.get("target"))
        );

        holder.assignContents((double) serialization.get("magnitude"), (long) ((int) serialization.get("duration")));
        return holder;
    }

}
