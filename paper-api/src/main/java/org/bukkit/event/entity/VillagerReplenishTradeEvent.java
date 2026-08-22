package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;

/**
 * Called when a {@link Villager} is about to restock one of its trades.
 * <p>
 * If this event passes, the villager will reset the
 * {@link MerchantRecipe#getUses() uses} of the affected {@link #getRecipe()
 * MerchantRecipe} to {@code 0}.
 */
public interface VillagerReplenishTradeEvent extends EntityEventNew, Cancellable {

    @Override
    AbstractVillager getEntity();

    /**
     * Get the recipe to replenish.
     *
     * @return the replenished recipe
     */
    MerchantRecipe getRecipe();

    /**
     * Set the recipe to replenish.
     *
     * @param recipe the replenished recipe
     */
    void setRecipe(MerchantRecipe recipe);

    /**
     * Get the bonus uses added.
     *
     * @return the extra uses added
     * @deprecated MC 1.14 has changed how villagers restock their trades. Use
     * {@link MerchantRecipe#getUses()}.
     */
    @Deprecated(since = "1.18.1")
    default int getBonus() {
        return this.getRecipe().getUses();
    }

    /**
     * Set the bonus uses added.
     *
     * @param bonus the extra uses added
     * @deprecated MC 1.14 has changed how villagers restock their trades. This
     * has no effect anymore.
     */
    @Deprecated(since = "1.18.1")
    default void setBonus(final int bonus) {
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
