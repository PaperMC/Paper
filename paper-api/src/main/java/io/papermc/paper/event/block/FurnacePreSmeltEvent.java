package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called every tick before a furnace checks whether its current recipe can be smelted.
 * <p>
 * This event can be used to control whether smelting proceeds and to change the
 * total cooking time for the current smelt operation.
 */
@NullMarked
public class FurnacePreSmeltEvent extends BlockEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack source;
    private final CookingRecipe<?> recipe;
    private boolean allowed;
    private int totalCookTime;

    @ApiStatus.Internal
    public FurnacePreSmeltEvent(
        final Block furnace,
        final ItemStack source,
        final CookingRecipe<?> recipe,
        final boolean allowed,
        final int totalCookTime
    ) {
        super(furnace);
        this.source = source;
        this.recipe = recipe;
        this.allowed = allowed;
        this.totalCookTime = totalCookTime;
    }

    /**
     * Gets the item being smelted.
     *
     * @return the source item
     */
    public ItemStack getSource() {
        return this.source;
    }

    /**
     * Gets the cooking recipe being used.
     *
     * @return the cooking recipe
     */
    public CookingRecipe<?> getRecipe() {
        return this.recipe;
    }

    /**
     * Gets whether the smelting is allowed to proceed.
     *
     * @return {@code true} if smelting is allowed
     */
    public boolean isAllowed() {
        return this.allowed;
    }

    /**
     * Sets whether this smelt operation is allowed to proceed.
     *
     * @param allowed whether smelting is allowed
     */
    public void setAllowed(final boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * Gets the total cooking time for this smelt operation.
     *
     * @return the total cooking time in ticks
     */
    public int getTotalCookTime() {
        return this.totalCookTime;
    }

    /**
     * Sets the total cooking time for this smelt operation.
     *
     * @param totalCookTime the total cooking time in ticks
     */
    public void setTotalCookTime(final int totalCookTime) {
        this.totalCookTime = totalCookTime;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
