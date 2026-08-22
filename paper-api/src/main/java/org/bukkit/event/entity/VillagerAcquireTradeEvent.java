package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;

/**
 * Called whenever a villager acquires a new trade.
 */
public interface VillagerAcquireTradeEvent extends EntityEventNew, Cancellable {

    @Override
    AbstractVillager getEntity();

    /**
     * Get the recipe to be acquired.
     *
     * @return the new recipe
     */
    MerchantRecipe getRecipe();

    /**
     * Set the recipe to be acquired.
     *
     * @param recipe the new recipe
     */
    void setRecipe(MerchantRecipe recipe);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
