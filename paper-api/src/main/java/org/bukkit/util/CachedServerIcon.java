package org.bukkit.util;

import org.bukkit.Server;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.Nullable;

/**
 * This is a cached version of a server-icon. Its internal representation
 * and implementation is undefined.
 *
 * @see Server#getServerIcon()
 * @see Server#loadServerIcon(java.awt.image.BufferedImage)
 * @see Server#loadServerIcon(java.io.File)
 * @see ServerListPingEvent#setServerIcon(CachedServerIcon)
 */
public interface CachedServerIcon {

    @Nullable
    public String getData(); // Paper

    // Paper start
    default boolean isEmpty() {
        return this.getData() == null;
    }
    // Paper end
}
