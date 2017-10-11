package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;

/**
 * Represents a client requesting the current status from the server (e.g. from
 * the server list).
 *
 * @see PaperServerListPingEvent
 */
public interface StatusClient extends NetworkClient {

    /**
     * Returns whether the client is using an older version that doesn't
     * support all the features in {@link PaperServerListPingEvent}.
     *
     * <p>For Vanilla, this returns {@code true} for all clients older than 1.7.</p>
     *
     * @return {@code true} if the client is using legacy ping
     */
    default boolean isLegacy() {
        return false;
    }

}
