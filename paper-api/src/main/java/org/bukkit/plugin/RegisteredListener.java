
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
    private final EventExecutor executor;

    public RegisteredListener(final Listener pluginListener, final EventExecutor eventExecutor, final Event.Priority eventPriority, final Plugin registeredPlugin ) {
        listener = pluginListener;
        priority = eventPriority;
        plugin = registeredPlugin;
        executor = eventExecutor;
    }

    public RegisteredListener(final Listener pluginListener, final Event.Priority eventPriority, final Plugin registeredPlugin, Event.Type type ) {
        listener = pluginListener;
        priority = eventPriority;
        plugin = registeredPlugin;
        executor = registeredPlugin.getPluginLoader().createExecutor( type, pluginListener );
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

    /**
     * Calls the event executor
     * @return Registered Priority
     */
    public void callEvent(Event event) {
        executor.execute( listener, event );
    }
}
