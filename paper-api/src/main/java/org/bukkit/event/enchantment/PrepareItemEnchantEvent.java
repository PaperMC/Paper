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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an ItemStack is inserted in an enchantment table - can be
 * called multiple times
 */
public class PrepareItemEnchantEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player enchanter;
    private final Block table;
    private final ItemStack item;
    private final EnchantmentOffer[] offers;
    private final int bonus;

    private boolean cancelled;

    @ApiStatus.Internal
    public PrepareItemEnchantEvent(@NotNull final Player enchanter, @NotNull EnchantmentView view, @NotNull final Block table, @NotNull final ItemStack item, @org.jetbrains.annotations.Nullable final EnchantmentOffer @NotNull [] offers, final int bonus) { // Paper - offers can contain null values
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.offers = offers;
        this.bonus = bonus;
    }

    @NotNull
    @Override
    public EnchantmentView getView() {
        return (EnchantmentView) super.getView();
    }

    /**
     * Gets the player enchanting the item
     *
     * @return enchanting player
     */
    @NotNull
    public Player getEnchanter() {
        return this.enchanter;
    }

    /**
     * Gets the block being used to enchant the item
     *
     * @return the block used for enchanting
     */
    @NotNull
    public Block getEnchantBlock() {
        return this.table;
    }

    /**
     * Gets the item to be enchanted.
     *
     * @return ItemStack of item
     */
    @NotNull
    public ItemStack getItem() {
        return this.item;
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
        int[] levelOffers = new int[this.offers.length];
        for (int i = 0; i < this.offers.length; i++) {
            levelOffers[i] = this.offers[i] != null ? this.offers[i].getCost() : 0;
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
     */
    public @Nullable EnchantmentOffer @NotNull[] getOffers() {
        return this.offers;
    }

    /**
     * Get enchantment bonus in effect - corresponds to number of bookshelves
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
