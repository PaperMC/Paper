package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a living entity tries to lunge with a spear
 */
@NullMarked
public class EntityLungeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int lungePower;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityLungeEvent(final LivingEntity entity, int lungePower) {
        super(entity);
        this.lungePower = lungePower;
    }

    /**
     * Gets the lunge power, which when initially passed, matches the enchantment level of the item, but can be higher.
     *
     * @return the lunge power
     */
    public int getLungePower() {
        return lungePower;
    }

    /**
     * Sets the lunge power. This commonly matches the enchantment level of the item, and can be set higher.
     * <p>
     * If set higher than 3, the power of the lunge will continue to scale like normal, as if the max enchantment
     * level is higher.
     * @param lungePower the new lunge power
     */
    public void setLungePower(final int lungePower) {
        this.lungePower = lungePower;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Set whether to cancel the lunge. If cancelled, the living entity will not lunge forward.
     * @param cancel {@code true} if you wish to cancel this event
     */
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
