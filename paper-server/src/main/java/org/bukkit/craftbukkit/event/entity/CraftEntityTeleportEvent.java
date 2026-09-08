package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityTeleportEvent extends CraftEntityEvent implements EntityTeleportEvent {

    private Location from;
    private @Nullable Location to;

    private boolean cancelled;

    public CraftEntityTeleportEvent(final Entity entity, final Location from, final Location to) {
        super(entity);
        this.from = from;
        this.to = to;
    }

    public CraftEntityTeleportEvent(final net.minecraft.world.entity.Entity entity, final double x, final double y, final double z) {
        final Location to = new Location(entity.level().getWorld(), x, y, z, entity.getYRot(), entity.getXRot());
        this(entity, to);
    }

    public CraftEntityTeleportEvent(final net.minecraft.world.entity.Entity entity, final Location to) {
        final Entity e = entity.getBukkitEntity();
        this(e, e.getLocation(), to);
    }

    public CraftEntityTeleportEvent(final net.minecraft.world.entity.Entity entity, final Level newLevel, final PositionMoveRotation destination) {
        this(entity, CraftLocation.toBukkit(destination, newLevel));
    }

    @Override
    public Location getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(final Location from) {
        this.from = from.clone();
    }

    @Override
    public @Nullable Location getTo() {
        return this.to;
    }

    @Override
    public void setTo(final @Nullable Location to) {
        this.to = to != null ? to.clone() : null;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityTeleportEvent.getHandlerList();
    }
}
