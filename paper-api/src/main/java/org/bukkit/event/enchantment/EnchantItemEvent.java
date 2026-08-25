package org.bukkit.event.enchantment;

import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEventNew;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.Positive;

/**
 * Called when an item is successfully enchanted (currently at
 * enchantment table)
 */
public interface EnchantItemEvent extends InventoryEventNew, Cancellable {

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
     * Gets the item to be enchanted (can be modified)
     *
     * @return ItemStack of item
     */
    ItemStack getItem();

    /**
     * Sets the item to be enchanted
     */
    void setItem(ItemStack item);

    /**
     * Gets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @return experience level cost
     */
    int getExpLevelCost();

    /**
     * Sets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @param level cost in levels
     */
    void setExpLevelCost(@Positive int level);

    /**
     * Get map of enchantment (levels, keyed by type) to be added to item
     * (modify map returned to change values). Note: Any enchantments not
     * allowed for the item will be ignored
     *
     * @return map of enchantment levels, keyed by enchantment
     */
    Map<Enchantment, Integer> getEnchantsToAdd();

    /**
     * Get the {@link Enchantment} that was displayed as a hint to the player
     * on the selected enchantment offer.
     *
     * @return the hinted enchantment
     */
    Enchantment getEnchantmentHint();

    /**
     * Get the level of the enchantment that was displayed as a hint to the
     * player on the selected enchantment offer.
     *
     * @return the level of the hinted enchantment
     */
    int getLevelHint();

    /**
     * Which button was pressed to initiate the enchanting.
     *
     * @return The button index (0, 1, or 2).
     */
    int whichButton();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
