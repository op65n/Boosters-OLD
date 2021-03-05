package com.splicegames.sgboosters.listener.voucher;

import com.splicegames.sgboosters.BoostersPlugin;
import com.splicegames.sgboosters.booster.BoosterType;
import com.splicegames.sgboosters.booster.component.BoosterTarget;
import com.splicegames.sgboosters.booster.holder.BoosterHolder;
import com.splicegames.sgboosters.command.util.BoosterActivate;
import com.splicegames.sgboosters.listener.registerable.ListenerRequirement;
import me.mattstudios.mfgui.gui.components.ItemNBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class VoucherUseListener extends ListenerRequirement {
    private final BoosterActivate boosterActivate;

    public VoucherUseListener(final BoostersPlugin plugin) {
        this.boosterActivate = new BoosterActivate(plugin);
    }

    @EventHandler
    public void onVoucherUse(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.hasItem()) return;

        final ItemStack item = event.getItem();
        if (item == null) return;
        if (ItemNBT.getNBTTag(item, "booster-type").length() == 0) return;

        final BoosterType type = BoosterType.valueOf(ItemNBT.getNBTTag(item, "booster-type"));
        final BoosterTarget target = new BoosterTarget().of(BoosterTarget.getRecipientsFromString(getBoosterTarget(ItemNBT.getNBTTag(item, "booster-target"), player)));
        final BoosterHolder holder = new BoosterHolder(player, type, target);

        holder.assignContents(Double.parseDouble(ItemNBT.getNBTTag(item, "booster-magnitude")), Long.parseLong(ItemNBT.getNBTTag(item, "booster-duration")));
        if (!this.boosterActivate.activateBooster(player, holder)) return;

        player.getInventory().setItemInMainHand(null);
    }

    private String getBoosterTarget(final String input, final Player player) {
        return input.equalsIgnoreCase("all") ? "all" : player.getName();
    }

    public boolean isPluginInstalled() {
        return true;
    }

    public String getRequiredPluginName() {
        return "None";
    }

    @Override
    public String getClassIdentifier() {
        return "Voucher Use Listener";
    }
}
