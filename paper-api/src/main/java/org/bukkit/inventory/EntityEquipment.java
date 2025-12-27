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
     * @throws IllegalArgumentException if the slot is invalid for the entity
     * @see org.bukkit.entity.LivingEntity#canUseEquipmentSlot(EquipmentSlot)
     */
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item);

    /**
     * Stores the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to put the ItemStack
     * @param item the ItemStack to set
     * @param silent whether the equip sound should be silenced
     * @throws IllegalArgumentException if the slot is invalid for the entity
     * @see org.bukkit.entity.LivingEntity#canUseEquipmentSlot(EquipmentSlot)
     */
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item, boolean silent);

    /**
     * Gets the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to get the ItemStack
     * @return the ItemStack in the given slot
     * @throws IllegalArgumentException if the slot is invalid for the entity
     * @see org.bukkit.entity.LivingEntity#canUseEquipmentSlot(EquipmentSlot)
     */
    @NotNull
    public ItemStack getItem(@NotNull EquipmentSlot slot);

    /**
     * Gets the item the entity is currently holding
     * in their main hand.
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player,
     * or it's an empty stack (has AIR as its type).
     * For non-empty stacks from players, this returns a live mirror. You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getItemInMainHand(); // will return a mirror
     * } else {
     *     equipment.getItemInMainHand(); // will return a copy
     * }
     * }</pre>
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
     * Gets the item the entity is currently holding
     * in their off hand.
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player,
     * or it's an empty stack (has AIR as its type).
     * For non-empty stacks from players, this returns a live mirror. You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getItemInOffHand(); // will return a mirror
     * } else {
     *     equipment.getItemInOffHand(); // will return a copy
     * }
     * }</pre>
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
     * Gets the item the entity is currently holding
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player,
     * or it's an empty stack (has AIR as its type).
     * For non-empty stacks from players, this returns a live mirror. You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getItemInHand(); // will return a mirror
     * } else {
     *     equipment.getItemInHand(); // will return a copy
     * }
     * }</pre>
     *
     * @return the currently held item
     * @see #getItemInMainHand()
     * @see #getItemInOffHand()
     * @deprecated entities can duel wield now use the methods for the
     *      specific hand instead
     */
    @Deprecated(since = "1.9")
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
    @Deprecated(since = "1.9")
    void setItemInHand(@Nullable ItemStack stack);

    /**
     * Gets the helmet currently being worn by the entity
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player.
     * For stacks from players, this returns a live mirror (or null). You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getHelmet(); // will return a mirror
     * } else {
     *     equipment.getHelmet(); // will return a copy
     * }
     * }</pre>
     *
     * @return The helmet being worn
     */
    @org.bukkit.UndefinedNullability("not null for entities, nullable for players") // Paper
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
     * Gets the chest plate currently being worn by the entity
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player.
     * For stacks from players, this returns a live mirror (or null). You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getChestplate(); // will return a mirror
     * } else {
     *     equipment.getChestplate(); // will return a copy
     * }
     * }</pre>
     *
     * @return The chest plate being worn
     */
    @org.bukkit.UndefinedNullability("not null for entities, nullable for players") // Paper
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
     * Gets the leggings currently being worn by the entity
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player.
     * For stacks from players, this returns a live mirror (or null). You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getLeggings(); // will return a mirror
     * } else {
     *     equipment.getLeggings(); // will return a copy
     * }
     * }</pre>
     *
     * @return The leggings being worn
     */
    @org.bukkit.UndefinedNullability("not null for entities, nullable for players") // Paper
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
     * Gets the boots currently being worn by the entity
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player.
     * For stacks from players, this returns a live mirror (or null). You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getBoots(); // will return a mirror
     * } else {
     *     equipment.getBoots(); // will return a copy
     * }
     * }</pre>
     *
     * @return The boots being worn
     */
    @org.bukkit.UndefinedNullability("not null for entities, nullable for players") // Paper
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
     * Gets all ItemStacks from the armor slots.
     *
     * <p>
     * This returns a copy if this equipment instance is from a non-player,
     * or it's an empty stack (has AIR as its type).
     * For non-empty stacks from players, this returns a live mirror. You can check if this
     * will return a mirror with
     * <pre>{@code
     * EntityEquipment equipment = entity.getEquipment();
     * if (equipment instanceof PlayerInventory) {
     *     equipment.getArmorContents(); // will return an array of mirror
     * } else {
     *     equipment.getArmorContents(); // will return an array of copies
     * }
     * }</pre>
     *
     * @return all the ItemStacks from the armor slots. Individual items can be
     * null and are returned in a fixed order starting from the boots and going
     * up to the helmet
     */
    @org.bukkit.UndefinedNullability("not null elements for entities, nullable elements for players") ItemStack @NotNull [] getArmorContents(); // Paper

    /**
     * Sets the entities armor to the provided array of ItemStacks
     *
     * @param items The items to set the armor as. Individual items may be null.
     */
    void setArmorContents(@NotNull ItemStack @NotNull [] items);

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
    @Deprecated(since = "1.9")
    float getItemInHandDropChance();

    /**
     * @param chance drop chance
     * @see #setItemInMainHandDropChance(float)
     * @see #setItemInOffHandDropChance(float)
     * @deprecated entities can duel wield now use the methods for the specific
     * hand instead
     */
    @Deprecated(since = "1.9")
    void setItemInHandDropChance(float chance);

    /**
     * Gets the chance of the main hand item being dropped upon this creature's
     * death.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
     * <li>A drop chance of exactly 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
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
    @NotNull // Paper
    Entity getHolder();
    // Paper start
    /**
     * Gets the drop chance of specified slot.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
     * </ul>
     *
     * @param slot the slot to get the drop chance of
     * @return the drop chance for the slot
     */
    float getDropChance(@NotNull EquipmentSlot slot);

    /**
     * Sets the drop chance of the specified slot.
     *
     * <ul>
     * <li>A drop chance of 0.0F will never drop
     * <li>A drop chance of 1.0F will always drop if killed by a player
     * <li>A drop chance of greater than 1.0F will always drop if killed by anything
     * </ul>
     *
     * @param slot the slot to set the drop chance of
     * @param chance the drop chance for the slot
     * @throws UnsupportedOperationException when called on non-{@link Mob} entities
     */
    void setDropChance(@NotNull EquipmentSlot slot, float chance);
    // Paper end
}
