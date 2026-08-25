package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.EnchantmentView;
import org.jspecify.annotations.Nullable;

/**
 * Called when an ItemStack is inserted in an enchantment table - can be
 * called multiple times
 */
public interface PrepareItemEnchantEvent extends InventoryEvent, Cancellable {

    @Override
    EnchantmentView getView();

    /**
     * Gets the player enchanting the item
     *
     * @return enchanting player
     */
    Player getEnchanter();

    /**
     * Gets the block being used to enchant the item
     *
     * @return the block used for enchanting
     */
    Block getEnchantBlock();

    /**
     * Gets the item to be enchanted.
     *
     * @return ItemStack of item
     */
    ItemStack getItem();

    /**
     * Get a list of offered experience level costs of the enchantment.
     *
     * @return experience level costs offered
     * @deprecated Use {@link #getOffers()} instead of this method
     */
    @Deprecated(since = "1.20.5")
    int[] getExpLevelCostsOffered();

    /**
     * Get a list of available {@link EnchantmentOffer} for the player. You can
     * modify the values to change the available offers for the player. An offer
     * may be null, if there isn't an enchantment offer at a specific slot. There
     * are 3 slots in the enchantment table available to modify.
     *
     * @return list of available enchantment offers
     */
    @Nullable EnchantmentOffer[] getOffers();

    /**
     * Get enchantment bonus in effect - corresponds to number of bookshelves
     *
     * @return enchantment bonus
     */
    int getEnchantmentBonus();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
