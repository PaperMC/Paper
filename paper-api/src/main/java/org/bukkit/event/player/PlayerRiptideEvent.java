package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * This event is fired when the player activates the riptide enchantment, using
 * their trident to propel them through the air.
 * <br>
 * N.B. the riptide action is currently performed client side, so manipulating
 * the player in this event may have undesired effects.
 */
public interface PlayerRiptideEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the item containing the used enchantment.
     *
     * @return held enchanted item
     */
    ItemStack getItem();

    /**
     * Get the velocity applied to the player as a result of this riptide.
     *
     * @return the riptide velocity
     */
    Vector getVelocity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
