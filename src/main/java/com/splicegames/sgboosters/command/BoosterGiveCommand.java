package com.splicegames.sgboosters.command;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.builder.BoosterVoucherBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

@Command("booster")
@Alias("boost")
public final class BoosterGiveCommand extends CommandBase {
    private final FileConfiguration configuration;

    public BoosterGiveCommand(final BoostersPlugin plugin) {
        this.configuration = plugin.getConfig();
    }

    @SubCommand("give")
    @Permission("sgboosters.command.booster.give")
    public void onGiveCommand(final CommandSender sender, final Player target, @Completion("#boosters") final String typeString, @Completion("#targets") final String targetString, final Double magnitude, final Long duration) {
        final BoosterType type = BoosterType.getNullable(typeString);

        if (type == null) {
            Message.send(sender,
                    this.configuration.getStringList("message.invalid-booster-type").stream()
                            .map(line -> PlaceholderAPI.setPlaceholders(null, line))
                            .collect(Collectors.toList()));
            return;
        }

        final ItemStack item = new BoosterVoucherBuilder(this.configuration)
                .ofMaterial(Material.NAME_TAG)
                .ofType(type)
                .ofTarget(targetString)
                .ofContents(magnitude, duration)
                .build();

        target.getInventory().addItem(item);
        Message.send(target, Replace.replaceList(
                this.configuration.getStringList("message.received-booster-voucher"),
                "{formatted-type}", WordUtils.capitalize(type.name().replace("_", " ").toLowerCase()),
                "{type}", type.name())
        );
    }
}
