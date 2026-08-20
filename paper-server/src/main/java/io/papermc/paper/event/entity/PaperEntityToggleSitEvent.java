package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperEntityToggleSitEvent extends CraftEntityEvent implements EntityToggleSitEvent {

    private final boolean isSitting;
    private boolean cancelled;

    public PaperEntityToggleSitEvent(final Entity entity, final boolean isSitting) {
        super(entity);
        this.isSitting = isSitting;
    }

    @Override
    public boolean getSittingState() {
        return this.isSitting;
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
        return EntityToggleSitEvent.getHandlerList();
    }
}
