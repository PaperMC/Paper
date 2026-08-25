package org.bukkit.craftbukkit.event.world;

import com.google.common.base.Preconditions;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.GenericGameEvent;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

public class CraftGenericGameEvent extends CraftWorldEvent implements GenericGameEvent {

    private final GameEvent event;
    private final Location location;
    private final Entity entity;
    private int radius;

    private boolean cancelled;

    public CraftGenericGameEvent(final GameEvent event, final Location location, final @Nullable Entity entity, final int radius, final boolean isAsync) {
        super(location.getWorld(), isAsync);
        this.event = event;
        this.location = location;
        this.entity = entity;
        this.radius = radius;
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(final @NonNegative int radius) {
        Preconditions.checkArgument(radius >= 0, "Radius must be >= 0");
        this.radius = radius;
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
        return GenericGameEvent.getHandlerList();
    }
}
