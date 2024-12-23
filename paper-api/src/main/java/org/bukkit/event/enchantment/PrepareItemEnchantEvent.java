package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is inserted in an enchantment table - can be
 * called multiple times
 *
 * @since 1.1.0
 */
public class PrepareItemEnchantEvent extends InventoryEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private final EnchantmentOffer[] offers;
    private final int bonus;
    private boolean cancelled;
    private final Player enchanter;

    public PrepareItemEnchantEvent(@NotNull final Player enchanter, @NotNull EnchantmentView view, @NotNull final Block table, @NotNull final ItemStack item, @org.jetbrains.annotations.Nullable final EnchantmentOffer @NotNull [] offers, final int bonus) { // Paper - offers can contain null values
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.offers = offers;
        this.bonus = bonus;
    }

    /**
     * Gets the player enchanting the item
     *
     * @return enchanting player
     */
    @NotNull
    public Player getEnchanter() {
        return enchanter;
    }

    /**
     * Gets the block being used to enchant the item
     *
     * @return the block used for enchanting
     */
    @NotNull
    public Block getEnchantBlock() {
        return table;
    }

    /**
     * Gets the item to be enchanted.
     *
     * @return ItemStack of item
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get a list of offered experience level costs of the enchantment.
     *
     * @return experience level costs offered
     * @deprecated Use {@link #getOffers()} instead of this method
     */
    @NotNull
    @Deprecated(since = "1.20.5")
    public int[] getExpLevelCostsOffered() {
        int[] levelOffers = new int[offers.length];
        for (int i = 0; i < offers.length; i++) {
            levelOffers[i] = offers[i] != null ? offers[i].getCost() : 0;
        }
        return levelOffers;
    }

    /**
     * Get a list of available {@link EnchantmentOffer} for the player. You can
     * modify the values to change the available offers for the player. An offer
     * may be null, if there isn't an enchantment offer at a specific slot. There
     * are 3 slots in the enchantment table available to modify.
     *
     * @return list of available enchantment offers
     * @since 1.11
     */
    public @org.jetbrains.annotations.Nullable EnchantmentOffer @NotNull [] getOffers() { // Paper offers can contain null values
        return offers;
    }

    /**
     * Get enchantment bonus in effect - corresponds to number of bookshelves
     *
     * @return enchantment bonus
     */
    public int getEnchantmentBonus() {
        return bonus;
    }

    /**
     * @since 1.21
     */
    @NotNull
    @Override
    public EnchantmentView getView() {
        return (EnchantmentView) super.getView();
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
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
