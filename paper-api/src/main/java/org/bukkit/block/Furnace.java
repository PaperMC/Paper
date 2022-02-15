package org.bukkit.block;

import java.util.Map;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a furnace.
 */
public interface Furnace extends Container {

    /**
     * Get burn time.
     *
     * @return Burn time
     */
    public short getBurnTime();

    /**
     * Set burn time.
     *
     * A burn time greater than 0 will cause this block to be lit, whilst a time
     * less than 0 will extinguish it.
     *
     * @param burnTime Burn time
     */
    public void setBurnTime(short burnTime);

    /**
     * Get cook time.
     *
     * This is the amount of time the item has been cooking for.
     *
     * @return Cook time
     */
    public short getCookTime();

    /**
     * Set cook time.
     *
     * This is the amount of time the item has been cooking for.
     *
     * @param cookTime Cook time
     */
    public void setCookTime(short cookTime);

    /**
     * Get cook time total.
     *
     * This is the amount of time the item is required to cook for.
     *
     * @return Cook time total
     */
    public int getCookTimeTotal();

    /**
     * Set cook time.
     *
     * This is the amount of time the item is required to cook for.
     *
     * @param cookTimeTotal Cook time total
     */
    public void setCookTimeTotal(int cookTimeTotal);

    /**
     * Get the recipes used in this furnace.
     *
     * <b>Note:</b> These recipes used are reset when the result item is
     * manually taken from the furnace.
     *
     * @return An immutable map with the recipes used and the times used
     */
    @NotNull
    public Map<CookingRecipe<?>, Integer> getRecipesUsed();

    @NotNull
    @Override
    public FurnaceInventory getInventory();

    @NotNull
    @Override
    public FurnaceInventory getSnapshotInventory();
}
