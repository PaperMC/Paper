package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a villager's trade's maximum uses is increased, due to a player's
 * trade.
 *
 * @see MerchantRecipe#getMaxUses()
 */
public class VillagerReplenishTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private MerchantRecipe recipe;
    private int bonus;

    public VillagerReplenishTradeEvent(@NotNull AbstractVillager what, @NotNull MerchantRecipe recipe, int bonus) {
        super(what);
        this.recipe = recipe;
        this.bonus = bonus;
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
     * Get the bonus uses added. The maximum uses of the recipe will be
     * increased by this number.
     *
     * @return the extra uses added
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * Set the bonus uses added.
     *
     * @see VillagerReplenishTradeEvent#getBonus()
     * @param bonus the extra uses added
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
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
