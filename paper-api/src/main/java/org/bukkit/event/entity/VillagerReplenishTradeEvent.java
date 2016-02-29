package org.bukkit.event.entity;

import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;

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

    public VillagerReplenishTradeEvent(Villager what, MerchantRecipe recipe, int bonus) {
        super(what);
        this.recipe = recipe;
        this.bonus = bonus;
    }

    /**
     * Get the recipe to replenish.
     *
     * @return the replenished recipe
     */
    public MerchantRecipe getRecipe() {
        return recipe;
    }

    /**
     * Set the recipe to replenish.
     *
     * @param recipe the replenished recipe
     */
    public void setRecipe(MerchantRecipe recipe) {
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

    @Override
    public Villager getEntity() {
        return (Villager) super.getEntity();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
