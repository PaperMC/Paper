package io.papermc.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item on or used by an entity takes durability damage as a result of being hit/used.
 * <p>
 * NOTE: default vanilla behaviour dictates that armor/tools picked up by
 * mobs do not take damage (except via Thorns).
 */
public interface EntityDamageItemEvent extends EntityEventNew, Cancellable {

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

    /**
     * Sets the amount of durability damage this item will be taking.
     *
     * @param damage the damage amount to cause
     */
    void setDamage(int damage);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
