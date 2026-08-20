package org.bukkit.craftbukkit.event.player;

import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerMoveEvent extends CraftPlayerEvent implements PlayerMoveEvent {

    private Location from;
    private Location to;

    private boolean cancelled;

    public CraftPlayerMoveEvent(final Player player, final Location from, final @Nullable Location to) {
        super(player);
        this.from = from;
        this.to = to;
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
        return PlayerMoveEvent.getHandlerList();
    }
}
