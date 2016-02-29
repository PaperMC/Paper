package org.bukkit.event.entity;

import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;

/**
 * Called whenever a villager acquires a new trade.
 */
public class VillagerAcquireTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private MerchantRecipe recipe;

    public VillagerAcquireTradeEvent(Villager what, MerchantRecipe recipe) {
        super(what);
        this.recipe = recipe;
    }

    /**
     * Get the recipe to be acquired.
     *
     * @return the new recipe
     */
    public MerchantRecipe getRecipe() {
        return recipe;
    }

    /**
     * Set the recipe to be acquired.
     *
     * @param recipe the new recipe
     */
    public void setRecipe(MerchantRecipe recipe) {
        this.recipe = recipe;
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
