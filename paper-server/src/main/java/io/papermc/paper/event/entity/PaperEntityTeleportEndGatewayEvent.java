package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperEntityTeleportEndGatewayEvent extends EntityTeleportEndGatewayEvent {

    @ApiStatus.Internal
    public PaperEntityTeleportEndGatewayEvent(final Entity entity, final Location from, final Location to, final EndGateway gateway) {
        super(entity, from, to, gateway);
    }

    @Override
    public void setTo(final @Nullable Location to) {
        if (to != null) {
            Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(to), "destination Location is not in spawnable bounds");
        }
        super.setTo(to);
    }

    @Override
    public void setFrom(final Location from) {
        Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(from), "origin Location is not in spawnable bounds");
        super.setFrom(from);
    }
}
