package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.inventory.FurnaceInventory;

/**
 * Represents a captured state of a furnace.
 */
public interface Furnace extends Container, Nameable {

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

    @Override
    public FurnaceInventory getInventory();

    @Override
    public FurnaceInventory getSnapshotInventory();
}
