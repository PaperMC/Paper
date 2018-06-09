package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired any time an entity attempts to teleport in an end gateway
 */
@NullMarked
public class EntityTeleportEndGatewayEvent extends EntityTeleportEvent {

    private final EndGateway gateway;

    @ApiStatus.Internal
    public EntityTeleportEndGatewayEvent(final Entity entity, final Location from, final Location to, final EndGateway gateway) {
        super(entity, from, to);
        this.gateway = gateway;
    }

    /**
     * The gateway triggering the teleport
     *
     * @return EndGateway used
     */
    public EndGateway getGateway() {
        return this.gateway;
    }

}
