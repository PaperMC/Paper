package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperEntityPathfindEvent extends CraftEntityEvent implements EntityPathfindEvent {

    private final @Nullable Entity targetEntity;
    private final Location location;

    private boolean cancelled;

    public PaperEntityPathfindEvent(final Entity entity, final Location location, final @Nullable Entity targetEntity) {
        super(entity);
        this.targetEntity = targetEntity;
        this.location = location;
    }

    @Override
    public @Nullable Entity getTargetEntity() {
        return this.targetEntity;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
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
        return EntityPathfindEvent.getHandlerList();
    }
}
