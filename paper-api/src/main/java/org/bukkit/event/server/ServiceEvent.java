package org.bukkit.event.server;

import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * An event relating to a registered service. This is called in a {@link
 * org.bukkit.plugin.ServicesManager}
 */
public interface ServiceEvent extends ServerEventNew {

    RegisteredServiceProvider<?> getProvider();
}
