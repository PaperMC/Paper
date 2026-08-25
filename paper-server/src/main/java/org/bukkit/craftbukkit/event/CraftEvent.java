package org.bukkit.craftbukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class CraftEvent implements Event {

    private @Nullable String name;
    private final boolean isAsync;

    protected CraftEvent() {
        this(false);
    }

    protected CraftEvent(final boolean isAsync) {
        this.isAsync = isAsync;
    }

    @Override
    public final boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        if (this instanceof final Cancellable cancellable) {
            return !cancellable.isCancelled();
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
