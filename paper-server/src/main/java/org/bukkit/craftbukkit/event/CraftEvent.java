package org.bukkit.craftbukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class CraftEvent implements Event {

    private String name;
    private final boolean isAsync;

    protected CraftEvent() {
        this(false);
    }

    protected CraftEvent(final boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof Cancellable) {
            return !((Cancellable) this).isCancelled();
        } else {
            return true;
        }
    }

    @Override
    public String getEventName() {
        if (this.name == null) {
            this.name = this.getClass().getSimpleName(); // TODO handle interface, print the API name
        }
        return this.name;
    }

    @Override
    public final boolean isAsynchronous() {
        return this.isAsync;
    }
}
