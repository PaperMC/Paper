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
 * This event can be used to control whether smelting proceeds, replace the
 * cooking recipe, and change the total cooking time for the current smelt operation.
 */
@NullMarked
public class FurnacePreSmeltEvent extends BlockEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack source;
    private CookingRecipe<?> recipe;
    private boolean allowed;
    private int totalCookTime;
    private boolean totalCookTimeChanged;

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
     * Sets the cooking recipe to use for this smelt operation.
     * <p>
     * The recipe must already be registered on the server and must match
     * the type of furnace performing the smelt. The registered recipe
     * identified by the supplied recipe's key will be used.
     * <p>
     * Changing properties of a recipe object without changing its key
     * does not modify the registered recipe.
     *
     * @param recipe the registered cooking recipe to use
     */
    public void setRecipe(final CookingRecipe<?> recipe) {
        this.recipe = java.util.Objects.requireNonNull(recipe, "recipe");
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
        if (totalCookTime < 1) {
            throw new IllegalArgumentException("Total cooking time must be at least 1 tick");
        }
        this.totalCookTime = totalCookTime;
        this.totalCookTimeChanged = true;
    }

    /**
     * Gets whether the total cooking time was explicitly changed.
     *
     * @return whether the total cooking time was changed
     */
    @ApiStatus.Internal
    public boolean isTotalCookTimeChanged() {
        return this.totalCookTimeChanged;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
