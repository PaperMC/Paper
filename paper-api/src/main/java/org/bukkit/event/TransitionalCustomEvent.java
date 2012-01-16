package org.bukkit.event;

/**
 * A transitional class to avoid breaking plugins using custom events.
 */
@Deprecated
public class TransitionalCustomEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
