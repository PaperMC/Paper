package com.destroystokyo.paper.event.entity;

import org.bukkit.block.EndGateway;
import org.bukkit.event.entity.EntityTeleportEvent;

/**
 * Fired any time an entity attempts to teleport in an end gateway
 */
public interface EntityTeleportEndGatewayEvent extends EntityTeleportEvent {

    /**
     * The gateway triggering the teleport
     *
     * @return EndGateway used
     */
    EndGateway getGateway();
}
