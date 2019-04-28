package org.bukkit.event.entity;

import org.bukkit.entity.Slime;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Slime splits into smaller Slimes upon death
 */
public class SlimeSplitEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private int count;

    public SlimeSplitEvent(@NotNull final Slime slime, final int count) {
        super(slime);
        this.count = count;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public Slime getEntity() {
        return (Slime) entity;
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
