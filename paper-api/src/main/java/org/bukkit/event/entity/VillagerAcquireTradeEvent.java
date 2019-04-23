package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Called whenever a villager acquires a new trade.
 */
public class VillagerAcquireTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private MerchantRecipe recipe;

    public VillagerAcquireTradeEvent(@NotNull AbstractVillager what, @NotNull MerchantRecipe recipe) {
        super(what);
        this.recipe = recipe;
    }

    /**
     * Get the recipe to be acquired.
     *
     * @return the new recipe
     */
    @NotNull
    public MerchantRecipe getRecipe() {
        return recipe;
    }

    /**
     * Set the recipe to be acquired.
     *
     * @param recipe the new recipe
     */
    public void setRecipe(@NotNull MerchantRecipe recipe) {
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
