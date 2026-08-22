package com.destroystokyo.paper.event.entity;

import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Event called when a player gets close to a skeleton horse and triggers the lightning trap
 */
public interface SkeletonHorseTrapEvent extends EntityEventNew, Cancellable { // todo javadocs?

    @Override
    SkeletonHorse getEntity();

    List<HumanEntity> getEligibleHumans();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

