package org.bukkit.inventory;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to a creatures inventory
 */
public interface EntityEquipment {

    /**
     * Stores the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to put the ItemStack
     * @param item the ItemStack to set
     */
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item);

    /**
     * Stores the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to put the ItemStack
     * @param item the ItemStack to set
     * @param silent whether or not the equip sound should be silenced
     */
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item, boolean silent);

    /**
     * Gets the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to get the ItemStack
     * @return the ItemStack in the given slot
     */
    @NotNull
    public ItemStack getItem(@NotNull EquipmentSlot slot);

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
     * Sets the item the entity is holding in their main hand.
     *
     * @param item The item to put into the entities hand
     * @param silent whether or not the equip sound should be silenced
     */
    void setItemInMainHand(@Nullable ItemStack item, boolean silent);

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
     * Sets the item the entity is holding in their off hand.
     *
     * @param item The item to put into the entities hand
     * @param silent whether or not the equip sound should be silenced
     */
    void setItemInOffHand(@Nullable ItemStack item, boolean silent);

    /**
     * Gets a copy of the item the entity is currently holding
     *
     * @return the currently held item
     * @see #getItemInMainHand()
     * @see #getItemInOffHand()
     * @deprecated entities can duel wield now use the methods for the
     *      specific hand instead
     */
    @Deprecated
    @NotNull
    ItemStack getItemInHand();

    /**
     * Sets the item the entity is holding
     *
     * @param stack The item to put into the entities hand
     * @see #setItemInMainHand(ItemStack)
     * @see #setItemInOffHand(ItemStack)
     * @deprecated entities can duel wield now use the methods for the
     *      specific hand instead
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
     * Sets the helmet worn by the entity
     *
     * @param helmet The helmet to put on the entity
     * @param silent whether or not the equip sound should be silenced
     */
    void setHelmet(@Nullable ItemStack helmet, boolean silent);

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
     * Sets the chest plate worn by the entity
     *
     * @param chestplate The chest plate to put on the entity
     * @param silent whether or not the equip sound should be silenced
     */
    void setChestplate(@Nullable ItemStack chestplate, boolean silent);

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
     * Sets the leggings worn by the entity
     *
     * @param leggings The leggings to put on the entity
     * @param silent whether or not the equip sound should be silenced
     */
    void setLeggings(@Nullable ItemStack leggings, boolean silent);

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
     * Sets the boots worn by the entity
     *
     * @param boots The boots to put on the entity
     * @param silent whether or not the equip sound should be silenced
     */
    void setBoots(@Nullable ItemStack boots, boolean silent);

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
     * @return drop chance
     * @see #getItemInMainHandDropChance()
     * @see #getItemInOffHandDropChance()
     * @deprecated entities can duel wield now use the methods for the specific
     * hand instead
     */
    @Deprecated
    float getItemInHandDropChance();

    /**
     * @param chance drop chance
     * @see #setItemInMainHandDropChance(float)
     * @see #setItemInOffHandDropChance(float)
     * @deprecated entities can duel wield now use the methods for the specific
     * hand instead
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
     * @return chance of the currently held item being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
     * @return chance of the off hand item being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
     * @return the chance of the helmet being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
     * @return the chance of the chest plate being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
     * @return the chance of the leggings being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
     * @return the chance of the boots being dropped (1 for non-{@link Mob})
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
     * @throws UnsupportedOperationException when called on non-{@link Mob}
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
