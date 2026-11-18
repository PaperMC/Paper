package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Called when an ItemStack is inserted in an enchantment table - can be
 * called multiple times.
 */
public class PrepareItemEnchantEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player enchanter;
    private final Block table;
    private final ItemStack item;
    private final @Nullable EnchantmentOffer[] offers;
    private final int bonus;

    private boolean cancelled;

    @ApiStatus.Internal
    public PrepareItemEnchantEvent(
        final Player enchanter,
        final EnchantmentView view,
        final Block table,
        final ItemStack item,
        final @Nullable EnchantmentOffer[] offers,
        final int bonus
    ) {
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

    /**
     * Gets the player enchanting the item.
     *
     * @return enchanting player
     */
    public Player getEnchanter() {
        return this.enchanter;
    }

    /**
     * Gets the block being used to enchant the item.
     *
     * @return the block used for enchanting
     */
    public Block getEnchantBlock() {
        return this.table;
    }

    /**
     * Gets the item to be enchanted.
     *
     * @return ItemStack of item
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Get a list of offered experience level costs of the enchantment.
     *
     * @return experience level costs offered
     * @deprecated Use {@link #getOffers()} instead of this method
     */
    @Deprecated(since = "1.20.5")
    public int[] getExpLevelCostsOffered() {
        final int[] levelOffers = new int[this.offers.length];
        for (int i = 0; i < this.offers.length; i++) {
            final EnchantmentOffer offer = this.offers[i];
            levelOffers[i] = offer != null ? offer.getCost() : 0;
        }
        return levelOffers;
    }

    /**
     * Get a list of available {@link EnchantmentOffer} for the player. You can
     * modify the values to change the available offers for the player. An offer
     * may be null, if there isn't an enchantment offer at a specific slot. There
     * are 3 slots in the enchantment table available to modify.
     *
     * @return array of available enchantment offers
     */
    public @Nullable EnchantmentOffer[] getOffers() {
        return this.offers;
    }

    /**
     * Get enchantment bonus in effect - corresponds to number of bookshelves.
     *
     * @return enchantment bonus
     */
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
