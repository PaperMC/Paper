package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a sheep's wool is dyed
 */
public class SheepDyeWoolEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private DyeColor color;

    public SheepDyeWoolEvent(final Entity what, final DyeColor color) {
        super(what);
        this.cancel = false;
        this.color = color;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the DyeColor the sheep is being dyed
     *
     * @return the DyeColor the sheep is being dyed
     */
    public DyeColor getColor() {
        return color;
    }

    /**
     * Sets the DyeColor the sheep is being dyed
     *
     * @param color the DyeColor the sheep will be dyed
     */
    public void setColor(DyeColor color) {
        this.color = color;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
