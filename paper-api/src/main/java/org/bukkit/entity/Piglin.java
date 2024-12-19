package org.bukkit.entity;

import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Piglin.
 *
 * @since 1.16.1
 */
public interface Piglin extends PiglinAbstract, InventoryHolder, com.destroystokyo.paper.entity.RangedEntity { // Paper

    /**
     * Get whether the piglin is able to hunt hoglins.
     *
     * @return Whether the piglin is able to hunt hoglins
     */
    public boolean isAbleToHunt();

    /**
     * Sets whether the piglin is able to hunt hoglins.
     *
     * @param flag Whether the piglin is able to hunt hoglins.
     */
    public void setIsAbleToHunt(boolean flag);

    /**
     * Adds a material to the allowed list of materials to barter with.
     *
     * @param material The material to add
     *
     * @return true if the item has been added successfully, false otherwise
     * @since 1.16.5
     */
    public boolean addBarterMaterial(@NotNull Material material);

    /**
     * Removes a material from the allowed list of materials to barter with.
     *
     * <strong>Note:</strong> It's not possible to override the default
     * bartering item gold_ingots as payment. To block gold_ingots see
     * {@link org.bukkit.event.entity.PiglinBarterEvent}.
     *
     * @param material The material to remove
     *
     * @return true if the item has been removed successfully, false otherwise
     * @since 1.16.5
     */
    public boolean removeBarterMaterial(@NotNull Material material);

    /**
     * Adds a material the piglin will pickup and store in his inventory.
     *
     * @param material The material you want the piglin to be interested in
     *
     * @return true if the item has been added successfully, false otherwise
     * @since 1.16.5
     */
    public boolean addMaterialOfInterest(@NotNull Material material);

    /**
     * Removes a material from the list of materials the piglin will pickup.
     *
     * <strong>Note:</strong> It's not possible to override the default list of
     * item the piglin will pickup. To cancel pickup see
     * {@link org.bukkit.event.entity.EntityPickupItemEvent}.
     *
     * @param material The material you want removed from the interest list
     * @return true if the item has been removed successfully, false otherwise
     * @since 1.16.5
     */
    public boolean removeMaterialOfInterest(@NotNull Material material);

    /**
     * Returns a immutable set of materials the piglins will pickup.
     * <br>
     * <strong>Note:</strong> This set will not include the items that are set
     * by default. To interact with those items see
     * {@link org.bukkit.event.entity.EntityPickupItemEvent}.
     *
     * @return An immutable materials set
     * @since 1.16.5
     */
    @NotNull
    public Set<Material> getInterestList();

    /**
     * Returns a immutable set of materials the piglins will barter with.
     *
     * <strong>Note:</strong> This set will not include the items that are set
     * by default. To interact with those items see
     * {@link org.bukkit.event.entity.PiglinBarterEvent}.
     *
     * @return An immutable materials set
     * @since 1.16.5
     */
    @NotNull
    public Set<Material> getBarterList();

    // Paper start
    /**
     * Causes the piglin to appear as if they are charging
     * a crossbow.
     * <p>
     * This works with any item currently held in the piglin's hand.
     *
     * @param chargingCrossbow is charging
     * @since 1.18.2
     */
    void setChargingCrossbow(boolean chargingCrossbow);

    /**
     * Gets if the piglin is currently charging the
     * item in their hand.
     *
     * @return is charging
     * @since 1.18.2
     */
    boolean isChargingCrossbow();

    /**
     * Sets whether the Piglin is dancing or not
     *
     * @param dancing is dancing
     * @since 1.20.1
     */
    void setDancing(boolean dancing);

    /**
     * Causes the piglin to dance for a
     * specified amount of time
     *
     * @param duration duration of the dance in ticks
     * @since 1.20.1
     */
    void setDancing(long duration);

    /**
     * Gets if the piglin is currently dancing
     *
     * @return is dancing
     * @since 1.20.1
     */
    boolean isDancing();
    // Paper end

}
