package com.splicegames.sgboosters.command.menu;

import com.github.frcsty.frozenactions.util.Color;
import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.builder.BoosterVoucherBuilder;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public final class BoosterListMenu {

    private final BoostersPlugin plugin;
    private final FileConfiguration configuration;
    private final PaginatedGui menu;
    private final Player player;

    public BoosterListMenu(final BoostersPlugin plugin, final Player player) {
        final File file = new File(plugin.getDataFolder(), "menu/booster-list-menu.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.plugin = plugin;
        this.player = player;

        this.menu = constructMenu();
    }

    private PaginatedGui constructMenu() {
        final Set<BoosterHolder> boosters = plugin.getBoosterStorage().getBoostersApplicableToUser(player.getUniqueId());
        final PaginatedGui gui = new PaginatedGui(
                configuration.getInt("menu-size") / 9,
                configuration.getInt("page-size"),
                Color.translate(Replace.replaceString(
                        configuration.getString("menu-title"),
                        "{active-boosters}", boosters.size()))
        );

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        final ConfigurationSection itemsSection = configuration.getConfigurationSection("items");
        if (itemsSection == null) return null;

        for (final String key : itemsSection.getKeys(false)) {
            final ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection == null) continue;

            final Material itemMaterial = Material.valueOf(itemSection.getString("material"));
            final String display = itemSection.getString("display");
            final List<String> lore = itemSection.getStringList("lore");

            final GuiItem guiItem = new GuiItem(constructItem(
                    itemMaterial,
                    display,
                    lore
            ), event -> {
                final Player player = (Player) event.getWhoClicked();
                final String action = itemSection.get("action") == null ? "NONE" : itemSection.getString("action", "NONE");

                switch (action.toUpperCase()) {
                    case "CLOSE":
                        gui.close(player);
                        break;
                    case "NEXT_PAGE":
                        gui.next();
                        break;
                    case "PREVIOUS_PAGE":
                        gui.previous();
                        break;
                }
            });

            gui.setItem(itemSection.getIntegerList("slots"), guiItem);
        }

        setBoosterItems(gui, boosters).forEach(gui::addItem);

        return gui;
    }

    private Set<GuiItem> setBoosterItems(final PaginatedGui gui, final Set<BoosterHolder> boosters) {
        final Set<GuiItem> boosterItems = new HashSet<>();
        final ConfigurationSection boostersSection = configuration.getConfigurationSection("booster-item");
        if (boostersSection == null) return null;

        for (final BoosterHolder holder : boosters) {
            final BoosterContent content = holder.getContent();

            final Material itemMaterial = Material.valueOf(boostersSection.getString("material"));
            final String display = Replace.replaceString(
                    boostersSection.getString("display"),
                    "{formatted-type}", WordUtils.capitalize(holder.getType().name().toLowerCase().replace("_", " "))
            );

            final StringBuilder builder = new StringBuilder();
            for (final String line : boostersSection.getStringList("lore")) {
                builder.append(PlaceholderAPI.setPlaceholders(null, Replace.replaceString(
                        line,
                        "{type}", holder.getType().name(),
                        "{formatted-type}", WordUtils.capitalize(holder.getType().name().replace("_", " ").toLowerCase()),
                        "{magnitude}", content.getMagnitude(),
                        "{duration}", TimeDisplay.getFormattedTime(content.getDuration()),
                        "{scope}", holder.isPersonal() ? "Personal" : "Global",
                        "{owner}", holder.getOwner().getName())
                )).append("\n");
            }

            final List<String> lore = new ArrayList<>(Arrays.asList(builder.toString().split("\n")));
            if (player.getUniqueId().toString().equalsIgnoreCase(holder.getOwner().getUniqueId().toString())) {
                lore.addAll(boostersSection.getStringList("owner-lore-addition"));
            }

            final GuiItem item = new GuiItem(constructItem(
                    itemMaterial,
                    display,
                    lore
            ), event -> {
                final Player player = (Player) event.getWhoClicked();
                if (!player.getUniqueId().toString().equalsIgnoreCase(holder.getOwner().getUniqueId().toString())) return;

                plugin.getBoosterStorage().removeBooster(holder);

                final ItemStack voucherItem = new BoosterVoucherBuilder(plugin.getConfig())
                        .ofMaterial(Material.NAME_TAG)
                        .ofType(holder.getType())
                        .ofTarget(holder.isPersonal() ? holder.getOwner().getName() : "ALL")
                        .ofContents(content.getMagnitude(), content.getDuration())
                        .build(null);

                player.getInventory().addItem(voucherItem);
                Message.send(player, Replace.replaceList(
                        plugin.getConfig().getStringList("message.cancelled-booster"),
                        "{formatted-type}", WordUtils.capitalize(holder.getType().name().replace("_", " ").toLowerCase())
                ));
                gui.close(player);
            });

            boosterItems.add(item);
        }

        return boosterItems;
    }

    private ItemStack constructItem(final Material material, final String display, final List<String> lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Color.translate(display));
        meta.setLore(Color.translate(lore));

        item.setItemMeta(meta);
        return item;
    }

    public void initializeUpdateInterval() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (menu.getInventory().getViewers().isEmpty()) {
                    System.out.println("No viewers, cancelled updating");
                    cancel();
                    return;
                }

                setBoosterItems(menu, plugin.getBoosterStorage().getBoostersApplicableToUser(player.getUniqueId())).forEach(menu::addItem);
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public PaginatedGui getMenu() {
        return this.menu;
    }

}
