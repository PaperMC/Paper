
package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface IExecutor {
    public void execute( Listener listener, Event event );
}