package io.papermc.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PaperEntityMoveEvent extends CraftEntityEvent implements EntityMoveEvent {

    private Location from;
    private Location to;

    private boolean cancelled;

    public PaperEntityMoveEvent(final LivingEntity entity, final Location from, final Location to) {
        super(entity);
        this.from = from;
        this.to = to;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public Location getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(final Location from) {
        this.from = CraftLocation.requireNonNull(from).clone();
    }

    @Override
    public Location getTo() {
        return this.to;
    }

    @Override
    public void setTo(final Location to) {
        this.to = CraftLocation.requireNonNull(to).clone();
    }

    @Override
    public boolean hasChangedPosition() {
        return this.hasExplicitlyChangedPosition() || !this.from.getWorld().equals(this.to.getWorld());
    }

    @Override
    public boolean hasExplicitlyChangedPosition() {
        return this.from.getX() != this.to.getX() || this.from.getY() != this.to.getY() || this.from.getZ() != this.to.getZ();
    }

    @Override
    public boolean hasChangedBlock() {
        return this.hasExplicitlyChangedBlock() || !this.from.getWorld().equals(this.to.getWorld());
    }

    @Override
    public boolean hasExplicitlyChangedBlock() {
        return this.from.getBlockX() != this.to.getBlockX() || this.from.getBlockY() != this.to.getBlockY() || this.from.getBlockZ() != this.to.getBlockZ();
    }

    @Override
    public boolean hasChangedOrientation() {
        return this.from.getPitch() != this.to.getPitch() || this.from.getYaw() != this.to.getYaw();
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
        return EntityMoveEvent.getHandlerList();
    }
}
