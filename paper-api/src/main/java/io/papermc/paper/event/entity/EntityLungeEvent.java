package io.papermc.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Called when a living entity tries to lunge with a spear.
 */
public interface EntityLungeEvent extends EntityEvent, Cancellable {

    /**
     * Gets the lunge power, which when initially passed, matches the enchantment level of the item, but can be higher.
     *
     * @return the lunge power
     */
    int getLungePower();

    /**
     * Sets the lunge power. This commonly matches the enchantment level of the item, and can be set higher.
     * <p>
     * If set higher than 3, the power of the lunge will continue to scale like normal, as if the max enchantment
     * level is higher.
     *
     * @param lungePower the new lunge power
     */
    void setLungePower(int lungePower);

    /**
     * Set whether to cancel the lunge. If cancelled, the living entity will not lunge forward.
     *
     * @param cancel {@code true} if you wish to cancel this event
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
