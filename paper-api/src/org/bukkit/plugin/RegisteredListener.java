
package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Stores relevant information for plugin listeners
 */
public class RegisteredListener {
    private final Listener listener;
    private final Event.Priority priority;
    private final Plugin plugin;

    public RegisteredListener(final Listener pluginListener, final Event.Priority eventPriority, final Plugin registeredPlugin) {
        listener = pluginListener;
        priority = eventPriority;
        plugin = registeredPlugin;
    }

    /**
     * Gets the listener for this registration
     * @return Registered Listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Gets the plugin for this registration
     * @return Registered Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the priority for this registration
     * @return Registered Priority
     */
    public Event.Priority getPriority() {
        return priority;
    }
}
