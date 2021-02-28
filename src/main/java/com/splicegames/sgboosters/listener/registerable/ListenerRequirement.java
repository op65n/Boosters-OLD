package com.splicegames.sgboosters.listener.registerable;

import com.splicegames.sgboosters.booster.BoosterType;
import org.bukkit.event.Listener;

public abstract class ListenerRequirement implements Listener {
    public abstract boolean isPluginInstalled();

    public abstract String getRequiredPluginName();

    public abstract String getClassIdentifier();

    public void initializeRequirements() {}

    public BoosterType getType() { return null; }

}
