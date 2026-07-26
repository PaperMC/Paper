package org.bukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Villager} is about to restock one of its trades.
 * <p>
 * If this event passes, the villager will reset the
 * {@link MerchantRecipe#getUses() uses} of the affected {@link #getRecipe()
 * MerchantRecipe} to <code>0</code>.
 */
public class VillagerReplenishTradeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private MerchantRecipe recipe;
    private boolean cancelled;

    @ApiStatus.Internal
    public VillagerReplenishTradeEvent(@NotNull AbstractVillager villager, @NotNull MerchantRecipe recipe) {
        super(villager);
        this.recipe = recipe;
    }

    @NotNull
    @Override
    public AbstractVillager getEntity() {
        return (AbstractVillager) super.getEntity();
    }

    /**
     * Get the recipe to replenish.
     *
     * @return the replenished recipe
     */
    @NotNull
    public MerchantRecipe getRecipe() {
        return this.recipe;
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
    @Deprecated(since = "1.18.1")
    public int getBonus() {
        return this.recipe.getUses();
    }

    /**
     * Set the bonus uses added.
     *
     * @param bonus the extra uses added
     * @deprecated MC 1.14 has changed how villagers restock their trades. This
     * has no effect anymore.
     */
    @Deprecated(since = "1.18.1")
    public void setBonus(int bonus) {
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
