package org.bukkit.event.enchantment;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an ItemStack is successfully enchanted (currently at enchantment table)
 */
@SuppressWarnings("serial")
public class EnchantItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Block table;
    private ItemStack item;
    private int level;
    private boolean cancelled;
    private Map<Enchantment,Integer> enchants;
    private Player enchanter;

    public EnchantItemEvent(Player enchanter, Block table, ItemStack item, int level, Map<Enchantment, Integer> enchants) {
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap<Enchantment, Integer>(enchants);
        this.cancelled = false;
    }

    /**
     * Gets the player enchanting the item
     *
     * @returns enchanting player
     */
    public Player getEnchanter() {
        return enchanter;
    }

    /**
     * Gets the block being used to enchant the item
     *
     * @return the block used for enchanting
     */
    public Block getEnchantBlock() {
        return table;
    }

    /**
     * Gets the item to be enchanted (can be modified)
     *
     * @return ItemStack of item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get cost in exp levels of the enchantment
     * @return experience level cost
     */
    public int getExpLevelCost() {
        return level;
    }

    /**
     * Set cost in exp levels of the enchantment
     * @param level - cost in levels
     */
    public void setExpLevelCost(int level) {
        this.level = level;
    }

    /**
     * Get map of enchantment (levels, keyed by type) to be added to item (modify map returned to change values)
     * Note: Any enchantments not allowed for the item will be ignored
     *
     * @return map of enchantment levels, keyed by enchantment
     */
    public Map<Enchantment, Integer> getEnchantsToAdd() {
        return enchants;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
