package org.bukkit.craftbukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class CraftEvent implements Event {

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
        final Class<?>[] itfs = this.getClass().getInterfaces();
        if (itfs.length == 0) {
            return this.getClass().getSimpleName();
        }
        return itfs[0].getSimpleName();
    }

    @Override
    public final boolean isAsynchronous() {
        return this.isAsync;
    }
}
