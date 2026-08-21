package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.Positive;

import static io.papermc.paper.util.BoundChecker.requirePositive;

public class PaperEntityIgniteEvent extends CraftEntityEvent implements EntityIgniteEvent {

    private int fuseTime;
    private boolean cancelled;

    public PaperEntityIgniteEvent(final Entity entity, final int fuseTime) {
        super(entity);
        this.fuseTime = fuseTime;
    }

    @Override
    public @Positive int getFuseTime() {
        return this.fuseTime;
    }

    @Override
    public void setFuseTime(final @Positive int ticks) {
        this.fuseTime = requirePositive(ticks, "ticks");
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
        return EntityIgniteEvent.getHandlerList();
    }
}
