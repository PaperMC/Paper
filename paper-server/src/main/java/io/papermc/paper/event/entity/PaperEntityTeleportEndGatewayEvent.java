package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.event.entity.CraftEntityTeleportEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;

public class PaperEntityTeleportEndGatewayEvent extends CraftEntityTeleportEvent implements EntityTeleportEndGatewayEvent {

    private final EndGateway gateway;

    public PaperEntityTeleportEndGatewayEvent(final Entity entity, final Location from, final Location to, final EndGateway gateway) {
        super(entity, from, to);
        this.gateway = gateway;
    }

    public PaperEntityTeleportEndGatewayEvent(final net.minecraft.world.entity.Entity entity, final Level newLevel, final PositionMoveRotation destination, final BlockEntity gateway) {
        final Entity e = entity.getBukkitEntity();
        this(e, e.getLocation(), CraftLocation.toBukkit(destination, newLevel), (EndGateway) CraftBlockStates.snapshotOf(gateway));
    }

    @Override
    public EndGateway getGateway() {
        return this.gateway;
    }
}
