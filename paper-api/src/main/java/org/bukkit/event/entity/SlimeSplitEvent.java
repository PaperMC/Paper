package org.bukkit.event.entity;

import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a Slime splits into smaller Slimes upon death
 */
public interface SlimeSplitEvent extends EntityEventNew, Cancellable {

    @Override
    AbstractCubeMob getEntity();

    /**
     * Gets the amount of smaller slimes to spawn
     *
     * @return the amount of slimes to spawn
     */
    int getCount();

    /**
     * Sets how many smaller slimes will spawn on the split
     *
     * @param count the amount of slimes to spawn
     */
    void setCount(int count);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
