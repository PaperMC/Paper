package com.destroystokyo.paper.event.entity;

import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event called when a player gets close to a skeleton horse and triggers the lightning trap
 */
@NullMarked
public class SkeletonHorseTrapEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<HumanEntity> eligibleHumans;
    private boolean cancelled;

    @ApiStatus.Internal
    public SkeletonHorseTrapEvent(final SkeletonHorse horse, final List<HumanEntity> eligibleHumans) {
        super(horse);
        this.eligibleHumans = eligibleHumans;
    }

    @Override
    public SkeletonHorse getEntity() {
        return (SkeletonHorse) super.getEntity();
    }

    public List<HumanEntity> getEligibleHumans() {
        return this.eligibleHumans;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

