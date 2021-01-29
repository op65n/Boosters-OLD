package com.splicegames.sgboosters.booster.component;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public final class BoosterTarget implements ConfigurationSerializable {

    static {
        ConfigurationSerialization.registerClass(BoosterTarget.class);
    }

    private final Set<UUID> recipients = new HashSet<>();

    public BoosterTarget of(final Set<UUID> recipientSet) {
        this.recipients.addAll(recipientSet);
        return this;
    }

    public void addRecipients(final UUID... recipients) {
        this.recipients.addAll(Stream.of(recipients).collect(Collectors.toSet()));
    }

    public void addRecipient(final UUID recipient) {
        if (this.recipients.contains(recipient)) return;

        this.recipients.add(recipient);
    }

    public boolean containsRecipient(final UUID recipient) {
        return this.recipients.contains(recipient);
    }

    public long getRecipientSetSize() {
        return this.recipients.size();
    }

    public static Set<UUID> getRecipientsFromString(final String target) {
        final Set<UUID> result = new HashSet<>();

        for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (target.equalsIgnoreCase("all")) {
                result.add(player.getUniqueId());
            } else if (target.equalsIgnoreCase(player.getName())) {
                result.add(player.getUniqueId());
                break;
            }
        }
        return result;
    }

    public ImmutableSet<UUID> getRecipients() {
        return this.recipients.stream().collect(ImmutableSet.toImmutableSet());
    }


    @NotNull
    public Map<String, Object> serialize() {
        final Map<String, Object> data = new HashMap<>();
        final AtomicInteger index = new AtomicInteger(0);
        this.recipients.stream().map(UUID::toString)
                .collect(Collectors.toSet())
                .forEach(it -> data.put("uuid-" + index.getAndIncrement(), it));

        return data;
    }

    public static BoosterTarget deserialize(final ConfigurationSection serialization) {
        return new BoosterTarget().of(serialization.getKeys(false).stream()
                .map(serialization::getString)
                .filter(Objects::nonNull)
                .map(UUID::fromString)
                .collect(Collectors.toSet()));
    }

}
