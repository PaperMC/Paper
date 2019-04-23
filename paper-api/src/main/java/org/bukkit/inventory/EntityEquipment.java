package org.bukkit.inventory;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to a creatures inventory
 */
public interface EntityEquipment {

    /**
     * Gets a copy of the item the entity is currently holding
     * in their main hand.
     *
     * @return the currently held item
     */
    @NotNull
    ItemStack getItemInMainHand();

    /**
     * Sets the item the entity is holding in their main hand.
     *
     * @param item The item to put into the entities hand
     */
    void setItemInMainHand(@Nullable ItemStack item);

    /**
     * Gets a copy of the item the entity is currently holding
     * in their off hand.
     *
     * @return the currently held item
     */
    @NotNull
    ItemStack getItemInOffHand();

    /**
     * Sets the item the entity is holding in their off hand.
     *
     * @param item The item to put into the entities hand
     */
    void setItemInOffHand(@Nullable ItemStack item);

    /**
     * Gets a copy of the item the entity is currently holding
     *
     * @deprecated entities can duel wield now use the methods for the
     *      specific hand instead
     * @see #getItemInMainHand()
     * @see #getItemInOffHand()
     * @return the currently held item
     */
    @Deprecated
    @NotNull
    ItemStack getItemInHand();

    /**
     * Sets the item the entity is holding
     *
     * @deprecated entities can duel wield now use the methods for the
     *      specific hand instead
     * @see #setItemInMainHand(ItemStack)
     * @see #setItemInOffHand(ItemStack)
     * @param stack The item to put into the entities hand
     */
    @Deprecated
    void setItemInHand(@Nullable ItemStack stack);

    /**
     * Gets a copy of the helmet currently being worn by the entity
     *
     * @return The helmet being worn
     */
    @Nullable
    ItemStack getHelmet();

    /**
     * Sets the helmet worn by the entity
     *
     * @param helmet The helmet to put on the entity
     */
    void setHelmet(@Nullable ItemStack helmet);

    /**
     * Gets a copy of the chest plate currently being worn by the entity
     *
     * @return The chest plate being worn
     */
    @Nullable
    ItemStack getChestplate();

    /**
     * Sets the chest plate worn by the entity
     *
     * @param chestplate The chest plate to put on the entity
     */
    void setChestplate(@Nullable ItemStack chestplate);

    /**
     * Gets a copy of the leggings currently being worn by the entity
     *
     * @return The leggings being worn
     */
    @Nullable
    ItemStack getLeggings();

    /**
     * Sets the leggings worn by the entity
     *
     * @param leggings The leggings to put on the entity
     */
    void setLeggings(@Nullable ItemStack leggings);

    /**
     * Gets a copy of the boots currently being worn by the entity
     *
     * @return The boots being worn
     */
    @Nullable
    ItemStack getBoots();

    /**
     * Sets the boots worn by the entity
     *
     * @param boots The boots to put on the entity
     */
    void setBoots(@Nullable ItemStack boots);

    /**
     * Gets a copy of all worn armor
     *
     * @return The array of worn armor. Individual items may be null.
     */
    @NotNull
    ItemStack[] getArmorContents();

    /**
     * Sets the entities armor to the provided array of ItemStacks
     *
     * @param items The items to set the armor as. Individual items may be null.
     */
    void setArmorContents(@NotNull ItemStack[] items);

    /**
     * Clears the entity of all armor and held items
     */
    void clear();

    /**
     * @deprecated entities can duel wield now use the methods for the specific
     * hand instead
     * @see #getItemInMainHandDropChance()
     * @see #getItemInOffHandDropChance()
     * @return drop chance
     */
    @Deprecated
    float getItemInHandDropChance();

    /**
     * @deprecated entities can duel wield now use the methods for the specific
     * hand instead
     * @see #setItemInMainHandDropChance(float)
     * @see #setItemInOffHandDropChance(float)
     * @param chance drop chance
     */
    @Deprecated
    void setItemInHandDropChance(float chance);

    /**
     * Gets the chance of the main hand item being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return chance of the currently held item being dropped (1 for players)
     */
    float getItemInMainHandDropChance();

    /**
     * Sets the chance of the item this creature is currently holding in their
     * main hand being dropped upon this creature's death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance the chance of the main hand item being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setItemInMainHandDropChance(float chance);

    /**
     * Gets the chance of the off hand item being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return chance of the off hand item being dropped (1 for players)
     */
    float getItemInOffHandDropChance();

    /**
     * Sets the chance of the off hand item being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance the chance of off hand item being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setItemInOffHandDropChance(float chance);

    /**
     * Gets the chance of the helmet being dropped upon this creature's death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return the chance of the helmet being dropped (1 for players)
     */
    float getHelmetDropChance();

    /**
     * Sets the chance of the helmet being dropped upon this creature's death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance of the helmet being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setHelmetDropChance(float chance);

    /**
     * Gets the chance of the chest plate being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return the chance of the chest plate being dropped (1 for players)
     */
    float getChestplateDropChance();

    /**
     * Sets the chance of the chest plate being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance of the chest plate being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setChestplateDropChance(float chance);

    /**
     * Gets the chance of the leggings being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return the chance of the leggings being dropped (1 for players)
     */
    float getLeggingsDropChance();

    /**
     * Sets the chance of the leggings being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance chance of the leggings being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setLeggingsDropChance(float chance);

    /**
     * Gets the chance of the boots being dropped upon this creature's death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @return the chance of the boots being dropped (1 for players)
     */
    float getBootsDropChance();

    /**
     * Sets the chance of the boots being dropped upon this creature's death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop
     * </ul>
     *
     * @param chance of the boots being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setBootsDropChance(float chance);

    /**
     * Get the entity this EntityEquipment belongs to
     *
     * @return the entity this EntityEquipment belongs to
     */
    @Nullable
    Entity getHolder();
}
