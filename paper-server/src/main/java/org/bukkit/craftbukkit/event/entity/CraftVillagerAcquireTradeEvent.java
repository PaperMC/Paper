package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.MerchantRecipe;

public class CraftVillagerAcquireTradeEvent extends CraftEntityEvent implements VillagerAcquireTradeEvent {

    private MerchantRecipe recipe;
    private boolean cancelled;

    public CraftVillagerAcquireTradeEvent(final AbstractVillager villager, final MerchantRecipe recipe) {
        super(villager);
        this.recipe = recipe;
    }

    @Override
    public AbstractVillager getEntity() {
        return (AbstractVillager) this.entity;
    }

    @Override
    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    @Override
    public void setRecipe(final MerchantRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return VillagerAcquireTradeEvent.getHandlerList();
    }
}
