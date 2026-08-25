package org.bukkit.craftbukkit.event.server;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.server.ServerEvent;

public abstract class CraftServerEvent extends CraftEvent implements ServerEvent {

    public CraftServerEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    public CraftServerEvent(final boolean isAsync) {
        super(isAsync);
    }
}
