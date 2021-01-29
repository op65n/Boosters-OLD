package com.splicegames.sgboosters.listener.registerable;

import org.bukkit.event.Listener;

public abstract class ListenerRequirement implements Listener {
    public abstract boolean isPluginInstalled();

    public abstract String getRequiredPluginName();

    public void initializeRequirements() {}
}
