package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when a Slime splits into smaller Slimes upon death
 */
public class SlimeSplitEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private int count;

    public SlimeSplitEvent(Entity what, int count) {
        super(Type.SLIME_SPLIT, what);
        this.cancel = false;
        this.count = count;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the amount of smaller slimes to spawn
     *
     * @return the amount of slimes to spawn
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets how many smaller slimes will spawn on the split
     *
     * @param count the amount of slimes to spawn
     */
    public void setCount(int count) {
        this.count = count;
    }
}