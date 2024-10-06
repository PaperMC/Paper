package org.bukkit.inventory.view;

import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * furnace view data.
 */
public interface FurnaceView extends InventoryView {

    @NotNull
    @Override
    FurnaceInventory getTopInventory();

    /**
     * The cook time for this view.
     * <p>
     * See {@link Furnace#getCookTime()} for more information.
     *
     * @return a number between 0 and 1
     */
    float getCookTime();

    /**
     * The total burn time for this view.
     * <p>
     * See {@link Furnace#getBurnTime()} for more information.
     *
     * @return a number between 0 and 1
     */
    float getBurnTime();

    /**
     * Checks whether or not the furnace is burning
     *
     * @return true given that the furnace is burning
     */
    boolean isBurning();

    /**
     * Sets the cook time
     * <p>
     * Setting cook time requires manipulation of both cookProgress and
     * cookDuration. This method does a simple division to get total progress
     * within the furnaces visual duration bar. For a clear visual effect
     * (cookProgress / cookDuration) should return a number between 0 and 1
     * inclusively.
     *
     * @param cookProgress the current of the cooking
     * @param cookDuration the total cook time
     */
    void setCookTime(int cookProgress, int cookDuration);

    /**
     * Sets the burn time
     * <p>
     * Setting burn time requires manipulation of both burnProgress and
     * burnDuration. This method does a simple division to get total progress
     * within the furnaces visual burning bar. For a clear visual effect
     * (burnProgress / burnDuration) should return a number between 0 and 1
     * inclusively.
     *
     * @param burnProgress the progress towards the burnDuration
     * @param burnDuration the total duration the view should be lit
     */
    void setBurnTime(int burnProgress, int burnDuration);
}
