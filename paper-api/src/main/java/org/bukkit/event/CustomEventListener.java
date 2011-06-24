package org.bukkit.event;

import org.bukkit.event.Listener;

/**
 * Handles all custom events
 */
public class CustomEventListener implements Listener {
    public CustomEventListener() {}

    /**
     * Called when a custom event is fired
     *
     * @param event Relevant event details
     */
    public void onCustomEvent(Event event) {}
}
