package com.splicegames.sgboosters.listener.booster.shopguiplus;

import com.github.frcsty.frozenactions.util.Replace;
import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterContent;
import com.splicegames.sgboosters.booster.data.BoosterStorage;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import com.splicegames.sgboosters.message.Message;
import com.splicegames.sgboosters.util.time.TimeDisplay;
import net.brcdev.shopgui.event.ShopPostTransactionEvent;
import net.brcdev.shopgui.shop.ShopTransactionResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;
import java.util.Set;

public final class ShopGUIPlusListener extends ListenerRequirement {

    private final DecimalFormat format = new DecimalFormat("###.##");
    private final FileConfiguration configuration;
    private final BoosterStorage storage;

    private Economy economy;

    public ShopGUIPlusListener(final BoostersPlugin plugin) {
        this.configuration = plugin.getConfig();
        this.storage = plugin.getBoosterStorage();
    }

    @EventHandler
    public void onShopSell(final ShopPostTransactionEvent event) {
        final ShopTransactionResult result = event.getResult();
        final Player player = result.getPlayer();

        if (result.getResult() != ShopTransactionResult.ShopTransactionResultType.SUCCESS) return;
        switch (result.getShopAction()) {
            case SELL:
            case SELL_ALL:
                manageSellBooster(player, result);
                break;
            case BUY:
                manageBuyBooster(player, result);
                break;
        }
    }

    private void manageSellBooster(final Player player, final ShopTransactionResult result) {
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.SHOP_GUI_PLUS_SELL, player.getUniqueId());

        holders.forEach(holder -> {
            final BoosterContent content = holder.getContent();
            final double sellPrice = result.getPrice();
            final double sellBoosterAddition = sellPrice * content.getMagnitude() - sellPrice;

            this.economy.depositPlayer(player, sellBoosterAddition);
            Message.send(player, Replace.replaceList(
                    this.configuration.getStringList("booster-message.shop-sell-message"),
                    "{magnitude}", content.getMagnitude(),
                    "{amount}", this.format.format(sellBoosterAddition),
                    "{owner}", holder.getOwner().getName(),
                    "{duration}", TimeDisplay.getFormattedTime(holder.getContent().getDuration())
            ));
        });
    }


    private void manageBuyBooster(final Player player, final ShopTransactionResult result) {
        final Set<BoosterHolder> holders = this.storage.getHolderOfTypeForUser(BoosterType.SHOP_GUI_PLUS_DISCOUNT, player.getUniqueId());

        holders.forEach(holder -> {
            final BoosterContent content = holder.getContent();
            final double purchasePrice = result.getPrice();
            final double purchaseBoosterAddition = purchasePrice * content.getMagnitude() - purchasePrice;

            this.economy.depositPlayer(player, purchaseBoosterAddition);
            Message.send(player, Replace.replaceList(
                    this.configuration.getStringList("booster-message.shop-buy-message"),
                    "{magnitude}", content.getMagnitude(),
                    "{amount}", this.format.format(purchaseBoosterAddition),
                    "{owner}", holder.getOwner().getName(),
                    "{duration}", TimeDisplay.getFormattedTime(holder.getContent().getDuration())
            ));
        });
    }

    public void initializeRequirements() {
        final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
            return;
        }

        throw new RuntimeException("Failed to find provider for Economy.class!");
    }

    public boolean isPluginInstalled() {
        return Bukkit.getServer().getPluginManager().getPlugin("ShopGUIPlus") != null;
    }

    public String getRequiredPluginName() {
        return "ShopGUIPlus";
    }

    @Override
    public String getClassIdentifier() {
        return "Shop GUI Plus Listener";
    }

    @Override
    public BoosterType getType() {
        return BoosterType.SHOP_GUI_PLUS_SELL;
    }
}