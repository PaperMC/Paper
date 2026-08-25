package org.bukkit.craftbukkit.event.server;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.server.ServerEventNew;

public abstract class CraftServerEvent extends CraftEvent implements ServerEventNew {

    public CraftServerEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    public CraftServerEvent(final boolean isAsync) {
        super(isAsync);
    }
}
