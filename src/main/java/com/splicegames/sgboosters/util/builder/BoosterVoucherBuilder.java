package com.splicegames.sgboosters.util.builder;

import com.github.frcsty.frozenactions.util.Color;
import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterTarget;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mattstudios.mfgui.gui.components.ItemNBT;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class BoosterVoucherBuilder {

    private static final BoostersPlugin PLUGIN = JavaPlugin.getPlugin(BoostersPlugin.class);

    private final FileConfiguration configuration;
    private Material material;
    private BoosterType type;
    private String target;
    private double magnitude;
    private long duration;

    public BoosterVoucherBuilder(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public BoosterVoucherBuilder ofMaterial(final Material material) {
        this.material = material;
        return this;
    }

    public BoosterVoucherBuilder ofType(final BoosterType type) {
        this.type = type;
        return this;
    }

    public BoosterVoucherBuilder ofTarget(final String target) {
        this.target = target;
        return this;
    }

    public BoosterVoucherBuilder ofContents(final double magnitude, final long duration) {
        this.magnitude = magnitude;
        this.duration = duration;
        return this;
    }

    public ItemStack build(String preset) {
        if (preset == null) preset = "common";
        final ConfigurationSection section;

        if (this.configuration.getConfigurationSection(String.format("booster-voucher-preset.%s", preset)) != null) {
            section = this.configuration.getConfigurationSection(String.format("booster-voucher-preset.%s", preset));
        } else {
            section = this.configuration.getConfigurationSection("booster-voucher-preset.common");
        }

        ItemStack itemStack = new ItemStack(this.material);
        final ItemMeta meta = itemStack.getItemMeta();

        if (section != null) {
            meta.setDisplayName(Color.translate(Replace.replaceString(
                    section.getString("display"),
                    "{formatted-type}", WordUtils.capitalize(this.type.name().replace("_", " ").toLowerCase()),
                    "{type}", this.type.name()
            )));

            final StringBuilder builder = new StringBuilder();
            for (final String line : section.getStringList("lore")) {
                builder.append(PlaceholderAPI.setPlaceholders(null, Replace.replaceString(
                        line,
                        "{type}", this.type.name(),
                        "{formatted-type}", WordUtils.capitalize(this.type.name().replace("_", " ").toLowerCase()),
                        "{magnitude}", this.magnitude,
                        "{duration}", TimeDisplay.getFormattedTime(this.duration),
                        "{scope}", BoosterTarget.getRecipientsFromString(this.target).size() == 1 ? "Personal" : "Global")
                )).append("\n");
            }

            meta.setLore(Arrays.stream(builder.toString().split("\n")).collect(Collectors.toList()));

            final ConfigurationSection enchantmentsSection = section.getConfigurationSection("enchantments");
            if (enchantmentsSection != null) {
                for (final String key : enchantmentsSection.getKeys(false)) {
                    final Enchantment enchantment = Enchantment.getByKey(new NamespacedKey(PLUGIN, key));

                    if (enchantment == null) {
                        PLUGIN.getLogger().log(Level.WARNING, String.format(
                                "Enchantment for input '%s' is not valid!", key
                        ));
                        continue;
                    }

                    final int level = enchantmentsSection.getInt(key);
                    meta.addEnchant(enchantment, level, false);
                }
            }

            final ConfigurationSection flagsSection = section.getConfigurationSection("flags");
            if (flagsSection != null) {
                for (final String flagString : section.getStringList("flags")) {
                    final Optional<ItemFlag> flag = Arrays.stream(ItemFlag.values()).filter(itemFlag -> itemFlag.name().equalsIgnoreCase(flagString)).findFirst();

                    if (flag.isEmpty()) {
                        PLUGIN.getLogger().log(Level.WARNING, String.format(
                                "ItemFlag for input '%s' is not valid!", flagString
                        ));
                        continue;
                    }

                    meta.addItemFlags(flag.get());
                }
            }
        }

        itemStack.setItemMeta(meta);
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-type", this.type.name());
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-target", this.target);
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-magnitude", String.valueOf(this.magnitude));
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-duration", String.valueOf(this.duration));

        return itemStack;
    }

}
