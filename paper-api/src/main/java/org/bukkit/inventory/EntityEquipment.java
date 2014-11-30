package org.bukkit.inventory;

import org.bukkit.entity.Entity;

/**
 * An interface to a creatures inventory
 */
public interface EntityEquipment {

    /**
     * Gets a copy of the item the entity is currently holding
     *
     * @return the currently held item
     */
    ItemStack getItemInHand();

    /**
     * Sets the item the entity is holding
     *
     * @param stack The item to put into the entities hand
     */
    void setItemInHand(ItemStack stack);

    /**
     * Gets a copy of the helmet currently being worn by the entity
     *
     * @return The helmet being worn
     */
    ItemStack getHelmet();

    /**
     * Sets the helmet worn by the entity
     *
     * @param helmet The helmet to put on the entity
     */
    void setHelmet(ItemStack helmet);

    /**
     * Gets a copy of the chest plate currently being worn by the entity
     *
     * @return The chest plate being worn
     */
    ItemStack getChestplate();

    /**
     * Sets the chest plate worn by the entity
     *
     * @param chestplate The chest plate to put on the entity
     */
    void setChestplate(ItemStack chestplate);

    /**
     * Gets a copy of the leggings currently being worn by the entity
     *
     * @return The leggings being worn
     */
    ItemStack getLeggings();

    /**
     * Sets the leggings worn by the entity
     *
     * @param leggings The leggings to put on the entity
     */
    void setLeggings(ItemStack leggings);

    /**
     * Gets a copy of the boots currently being worn by the entity
     *
     * @return The boots being worn
     */
    ItemStack getBoots();

    /**
     * Sets the boots worn by the entity
     *
     * @param boots The boots to put on the entity
     */
    void setBoots(ItemStack boots);

    /**
     * Gets a copy of all worn armor
     *
     * @return The array of worn armor
     */
    ItemStack[] getArmorContents();

    /**
     * Sets the entities armor to the provided array of ItemStacks
     *
     * @param items The items to set the armor as
     */
    void setArmorContents(ItemStack[] items);

    /**
     * Clears the entity of all armor and held items
     */
    void clear();

    /**
     * Gets the chance of the currently held item being dropped upon this
     * creature's death.
     * 
     * <ul>
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
     * </ul>
     *
     * @return chance of the currently held item being dropped (1 for players)
     */
    float getItemInHandDropChance();

    /**
     * Sets the chance of the item this creature is currently holding being
     * dropped upon this creature's death.
     * 
     * <ul>
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
     * </ul>
     *
     * @param chance the chance of the currently held item being dropped
     * @throws UnsupportedOperationException when called on players
     */
    void setItemInHandDropChance(float chance);

    /**
     * Gets the chance of the helmet being dropped upon this creature's death.
     * 
     * <ul>
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
     * </ul>
     *
     * @return the chance of the helmet being dropped (1 for players)
     */
    float getHelmetDropChance();

    /**
     * Sets the chance of the helmet being dropped upon this creature's death.
     * 
     * <ul>
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
     * </ul>
     *
     * @return the chance of the boots being dropped (1 for players)
     */
    float getBootsDropChance();

    /**
     * Sets the chance of the boots being dropped upon this creature's death.
     * 
     * <ul>
     * <li>A drop chance of 0F will never drop
     * <li>A drop chance of 1F will always drop
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
    Entity getHolder();
}
