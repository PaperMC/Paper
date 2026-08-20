package io.papermc.paper.event.entity;

import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperEntityPortalReadyEvent extends CraftEntityEvent implements EntityPortalReadyEvent {

    private final PortalType portalType;
    private @Nullable World targetWorld;

    private boolean cancelled;

    public PaperEntityPortalReadyEvent(final Entity entity, final @Nullable World targetWorld, final PortalType portalType) {
        super(entity);
        this.targetWorld = targetWorld;
        this.portalType = portalType;
    }

    @Override
    public @Nullable World getTargetWorld() {
        return this.targetWorld;
    }

    @Override
    public void setTargetWorld(final @Nullable World targetWorld) {
        this.targetWorld = targetWorld;
    }

    @Override
    public PortalType getPortalType() {
        return this.portalType;
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
        return EntityPortalReadyEvent.getHandlerList();
    }
}
