package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.event.entity.CraftEntityTeleportEvent;
import org.bukkit.entity.Entity;

public class PaperEntityTeleportEndGatewayEvent extends CraftEntityTeleportEvent implements EntityTeleportEndGatewayEvent {

    private final EndGateway gateway;

    public PaperEntityTeleportEndGatewayEvent(final Entity entity, final Location from, final Location to, final EndGateway gateway) {
        super(entity, from, to);
        this.gateway = gateway;
    }

    @Override
    public EndGateway getGateway() {
        return this.gateway;
    }
}
