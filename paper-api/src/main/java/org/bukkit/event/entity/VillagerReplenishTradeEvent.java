package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Villager} is about to restock one of its trades.
 * <p>
 * If this event passes, the villager will reset the
 * {@link MerchantRecipe#getUses() uses} of the affected {@link #getRecipe()
 * MerchantRecipe} to <code>0</code>.
 */
public class VillagerReplenishTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private MerchantRecipe recipe;

    public VillagerReplenishTradeEvent(@NotNull AbstractVillager what, @NotNull MerchantRecipe recipe) {
        super(what);
        this.recipe = recipe;
    }

    /**
     * Get the recipe to replenish.
     *
     * @return the replenished recipe
     */
    @NotNull
    public MerchantRecipe getRecipe() {
        return recipe;
    }

    /**
     * Set the recipe to replenish.
     *
     * @param recipe the replenished recipe
     */
    public void setRecipe(@NotNull MerchantRecipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Get the bonus uses added.
     *
     * @return the extra uses added
     * @deprecated MC 1.14 has changed how villagers restock their trades. Use
     * {@link MerchantRecipe#getUses()}.
     */
    @Deprecated
    public int getBonus() {
        return recipe.getUses();
    }

    /**
     * Set the bonus uses added.
     *
     * @param bonus the extra uses added
     * @deprecated MC 1.14 has changed how villagers restock their trades. This
     * has no effect anymore.
     */
    @Deprecated
    public void setBonus(int bonus) {
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public AbstractVillager getEntity() {
        return (AbstractVillager) super.getEntity();
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
