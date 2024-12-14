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
 * @since 1.7.2 R0.2
 */
public interface CachedServerIcon {

    /**
     * @since 1.9.4
     */
    @Nullable
    public String getData(); // Paper

    /**
     * @since 1.12.2
     */
    // Paper start
    default boolean isEmpty() {
        return getData() == null;
    }
    // Paper end
}
