package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SkeletonHorseTrapEvent;
import java.util.List;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.HandlerList;

public class PaperSkeletonHorseTrapEvent extends CraftEntityEvent implements SkeletonHorseTrapEvent {

    private final List<HumanEntity> eligibleHumans;
    private boolean cancelled;

    public PaperSkeletonHorseTrapEvent(final SkeletonHorse horse, final List<HumanEntity> eligibleHumans) {
        super(horse);
        this.eligibleHumans = eligibleHumans;
    }

    @Override
    public SkeletonHorse getEntity() {
        return (SkeletonHorse) this.entity;
    }

    @Override
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
        return SkeletonHorseTrapEvent.getHandlerList();
    }
}

