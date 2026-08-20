package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item used by the player takes durability damage as a result of
 * being used.
 */
public interface PlayerItemDamageEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the item being damaged.
     *
     * @return the item
     */
    ItemStack getItem();

    /**
     * Gets the amount of durability damage this item will be taking.
     *
     * @return durability change
     */
    int getDamage();

    // todo javadocs?
    void setDamage(int damage);

    /**
     * Gets the amount of durability damage this item would have taken before
     * the Unbreaking reduction. If the item has no Unbreaking level then
     * this value will be the same as the {@link #getDamage()} value.
     *
     * @return pre-reduction damage amount
     */
    int getOriginalDamage();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
