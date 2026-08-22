package org.bukkit.event.entity;

import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a firework explodes.
 */
public interface FireworkExplodeEvent extends EntityEvent, Cancellable {

    @Override
    Firework getEntity();

    /**
     * {@inheritDoc}
     * <p>
     * If the firework explosion is cancelled, the firework will
     * still be removed, but no particles will be displayed.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
