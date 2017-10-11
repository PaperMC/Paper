package com.destroystokyo.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;

/**
 * Represents a client requesting the current status from the server (e.g. from
 * the server list).
 *
 * @see PaperServerListPingEvent
 */
public interface StatusClient extends NetworkClient {

}
