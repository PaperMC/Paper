package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.EnchantmentView;
import org.jspecify.annotations.Nullable;

public class CraftPrepareItemEnchantEvent extends CraftInventoryEvent implements PrepareItemEnchantEvent {

    private final Player enchanter;
    private final Block table;
    private final ItemStack item;
    private final EnchantmentOffer[] offers;
    private final int bonus;

    private boolean cancelled;

    public CraftPrepareItemEnchantEvent(final Player enchanter, final EnchantmentView view, final Block table, final ItemStack item, final @Nullable EnchantmentOffer[] offers, final int bonus) { // Paper - offers can contain null values
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.offers = offers;
        this.bonus = bonus;
    }

    @Override
    public EnchantmentView getView() {
        return (EnchantmentView) super.getView();
    }

    @Override
    public Player getPlayer() {
        return this.enchanter;
    }

    @Override
    public Block getBlock() {
        return this.table;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    @Deprecated(since = "1.20.5")
    public int[] getExpLevelCostsOffered() {
        final int[] levelOffers = new int[this.offers.length];
        for (int i = 0; i < this.offers.length; i++) {
            levelOffers[i] = this.offers[i] != null ? this.offers[i].getCost() : 0;
        }
        return levelOffers;
    }

    @Override
    public @Nullable EnchantmentOffer[] getOffers() {
        return this.offers;
    }

    @Override
    public int getEnchantmentBonus() {
        return this.bonus;
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
        return PrepareItemEnchantEvent.getHandlerList();
    }
}
