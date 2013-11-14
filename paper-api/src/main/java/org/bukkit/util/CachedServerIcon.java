package org.bukkit.util;

import org.bukkit.Server;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * This is a cached version of a server-icon. It's internal representation
 * and implementation is undefined.
 *
 * @see Server#getServerIcon()
 * @see Server#loadServerIcon(java.awt.image.BufferedImage)
 * @see Server#loadServerIcon(java.io.File)
 * @see ServerListPingEvent#setServerIcon(CachedServerIcon)
 */
public interface CachedServerIcon {}
