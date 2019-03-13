package org.bukkit.event.enchantment;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully enchanted (currently at
 * enchantment table)
 */
public class EnchantItemEvent extends InventoryEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block table;
    private final ItemStack item;
    private int level;
    private boolean cancelled;
    private final Map<Enchantment,Integer> enchants;
    private final Player enchanter;
    private int button;

    public EnchantItemEvent(@NotNull final Player enchanter, @NotNull final InventoryView view, @NotNull final Block table, @NotNull final ItemStack item, final int level, @NotNull final Map<Enchantment, Integer> enchants, final int i) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap<Enchantment, Integer>(enchants);
        this.cancelled = false;
        this.button = i;
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
     * Gets the item to be enchanted (can be modified)
     *
     * @return ItemStack of item
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get cost in exp levels of the enchantment
     *
     * @return experience level cost
     */
    public int getExpLevelCost() {
        return level;
    }

    /**
     * Set cost in exp levels of the enchantment
     *
     * @param level - cost in levels
     */
    public void setExpLevelCost(int level) {
        this.level = level;
    }

    /**
     * Get map of enchantment (levels, keyed by type) to be added to item
     * (modify map returned to change values). Note: Any enchantments not
     * allowed for the item will be ignored
     *
     * @return map of enchantment levels, keyed by enchantment
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantsToAdd() {
        return enchants;
    }

    /**
     * Which button was pressed to initiate the enchanting.
     *
     * @return The button index (0, 1, or 2).
     */
    public int whichButton() {
        return button;
    }

    public boolean isCancelled() {
        return cancelled;
    }

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
