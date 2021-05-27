package org.bukkit.entity;

import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Piglin.
 */
public interface Piglin extends PiglinAbstract, InventoryHolder {

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
     */
    public boolean removeBarterMaterial(@NotNull Material material);

    /**
     * Adds a material the piglin will pickup and store in his inventory.
     *
     * @param material The material you want the piglin to be interested in
     *
     * @return true if the item has been added successfully, false otherwise
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
     */
    @NotNull
    public Set<Material> getBarterList();
}
