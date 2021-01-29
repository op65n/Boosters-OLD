package com.splicegames.sgboosters.util.builder;

import com.github.frcsty.frozenactions.util.Color;
import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterTarget;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mattstudios.mfgui.gui.components.ItemNBT;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class BoosterVoucherBuilder {

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


    public ItemStack build() {
        final ConfigurationSection section = this.configuration.getConfigurationSection("booster-voucher");
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

            meta.setLore(Arrays.asList(builder.toString().split("\n")));
        }

        itemStack.setItemMeta(meta);
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-type", this.type.name());
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-target", this.target);
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-magnitude", String.valueOf(this.magnitude));
        itemStack = ItemNBT.setNBTTag(itemStack, "booster-duration", String.valueOf(this.duration));
        return itemStack;
    }

}
