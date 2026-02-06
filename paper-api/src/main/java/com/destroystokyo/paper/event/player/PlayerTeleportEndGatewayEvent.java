package com.destroystokyo.paper.event.player;

import org.bukkit.block.EndGateway;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Fired when a teleport is triggered for an End Gateway
 */
public interface PlayerTeleportEndGatewayEvent extends PlayerTeleportEvent {

    /**
     * The gateway triggering the teleport
     *
     * @return EndGateway used
     */
    EndGateway getGateway();
}
