package org.bukkit.event.entity;

import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a firework explodes.
 */
public class FireworkExplodeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;

    public FireworkExplodeEvent(@NotNull final Firework what) {
        super(what);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Set the cancelled state of this event. If the firework explosion is
     * cancelled, the firework will still be removed, but no particles will be
     * displayed.
     *
     * @param cancel whether to cancel or not.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public Firework getEntity() {
        return (Firework) super.getEntity();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
