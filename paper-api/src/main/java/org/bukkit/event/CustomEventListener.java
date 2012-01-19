package org.bukkit.event;

/**
 * Handles all custom events
 */
@Deprecated
public class CustomEventListener implements Listener {
    private static final HandlerList handlers = new HandlerList();
    public CustomEventListener() {}

    /**
     * Called when a custom event is fired
     *
     * @param event Relevant event details
     */
    public void onCustomEvent(Event event) {}
}
