package org.bukkit.event.enchantment;

import com.google.common.base.Preconditions;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully enchanted (currently at
 * enchantment table)
 */
public class EnchantItemEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player enchanter;
    private final Block table;
    private ItemStack item;
    private int level;
    private final Map<Enchantment, Integer> enchants;
    private final Enchantment enchantmentHint;
    private final int levelHint;
    private final int button;

    private boolean cancelled;

    @ApiStatus.Internal
    public EnchantItemEvent(@NotNull final Player enchanter, @NotNull final InventoryView view, @NotNull final Block table, @NotNull final ItemStack item, final int level, @NotNull final Map<Enchantment, Integer> enchants, @NotNull final Enchantment enchantmentHint, final int levelHint, final int button) {
        super(view);
        this.enchanter = enchanter;
        this.table = table;
        this.item = item;
        this.level = level;
        this.enchants = new HashMap<>(enchants);
        this.enchantmentHint = enchantmentHint;
        this.levelHint = levelHint;
        this.button = button;
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
     * Gets the item to be enchanted (can be modified)
     *
     * @return ItemStack of item
     */
    @NotNull
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Sets the item to be enchanted
     */
    public void setItem(@NotNull final ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @return experience level cost
     */
    public int getExpLevelCost() {
        return this.level;
    }

    /**
     * Sets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @param level cost in levels
     */
    public void setExpLevelCost(int level) {
        Preconditions.checkArgument(level > 0, "The cost must be greater than 0!");

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
        return this.enchants;
    }

    /**
     * Get the {@link Enchantment} that was displayed as a hint to the player
     * on the selected enchantment offer.
     *
     * @return the hinted enchantment
     */
    @NotNull
    public Enchantment getEnchantmentHint() {
        return this.enchantmentHint;
    }

    /**
     * Get the level of the enchantment that was displayed as a hint to the
     * player on the selected enchantment offer.
     *
     * @return the level of the hinted enchantment
     */
    public int getLevelHint() {
        return this.levelHint;
    }

    /**
     * Which button was pressed to initiate the enchanting.
     *
     * @return The button index (0, 1, or 2).
     */
    public int whichButton() {
        return this.button;
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
