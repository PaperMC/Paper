package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called whenever a villager acquires a new trade.
 */
public class VillagerAcquireTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private MerchantRecipe recipe;
    private boolean cancelled;

    @ApiStatus.Internal
    public VillagerAcquireTradeEvent(@NotNull AbstractVillager villager, @NotNull MerchantRecipe recipe) {
        super(villager);
        this.recipe = recipe;
    }

    @NotNull
    @Override
    public AbstractVillager getEntity() {
        return (AbstractVillager) this.entity;
    }

    /**
     * Get the recipe to be acquired.
     *
     * @return the new recipe
     */
    @NotNull
    public MerchantRecipe getRecipe() {
        return this.recipe;
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
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
