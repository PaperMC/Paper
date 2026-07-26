package org.bukkit.entity;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArmorStand extends LivingEntity {

    /**
     * Returns the item the armor stand is currently holding.
     *
     * @return the held item
     * @see #getEquipment()
     * @deprecated prefer {@link ArmorStand#getItem(EquipmentSlot)}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    ItemStack getItemInHand();

    /**
     * Sets the item the armor stand is currently holding.
     *
     * @param item the item to hold
     * @see #getEquipment()
     * @deprecated prefer
     * {@link ArmorStand#setItem(EquipmentSlot, ItemStack)}
     */
    @Deprecated(since = "1.15.2")
    void setItemInHand(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its feet.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getBoots()}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    ItemStack getBoots();

    /**
     * Sets the item currently being worn by the armor stand on its feet.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setBoots(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated(since = "1.15.2")
    void setBoots(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its legs.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getLeggings()}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    ItemStack getLeggings();

    /**
     * Sets the item currently being worn by the armor stand on its legs.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setLeggings(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated(since = "1.15.2")
    void setLeggings(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its chest.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getChestplate()}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    ItemStack getChestplate();

    /**
     * Sets the item currently being worn by the armor stand on its chest.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setChestplate(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated(since = "1.15.2")
    void setChestplate(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its head.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getHelmet()}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    ItemStack getHelmet();

    /**
     * Sets the item currently being worn by the armor stand on its head.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setHelmet(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated(since = "1.15.2")
    void setHelmet(@Nullable ItemStack item);

    /**
     * Returns the armor stand's body's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getBodyPose();

    /**
     * Sets the armor stand's body's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setBodyPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's left arm's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getLeftArmPose();

    /**
     * Sets the armor stand's left arm's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setLeftArmPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's right arm's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getRightArmPose();

    /**
     * Sets the armor stand's right arm's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setRightArmPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's left leg's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getLeftLegPose();

    /**
     * Sets the armor stand's left leg's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setLeftLegPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's right leg's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getRightLegPose();

    /**
     * Sets the armor stand's right leg's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setRightLegPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's head's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getHeadPose();

    /**
     * Sets the armor stand's head's current pose as a
     * {@link org.bukkit.util.EulerAngle}.
     *
     * @param pose the current pose
     */
    void setHeadPose(@NotNull EulerAngle pose);

    /**
     * Returns whether the armor stand has a base plate.
     *
     * @return whether it has a base plate
     */
    boolean hasBasePlate();

    /**
     * Sets whether the armor stand has a base plate.
     *
     * @param basePlate whether it has a base plate
     */
    void setBasePlate(boolean basePlate);

    /**
     * Returns whether the armor stand should be visible or not.
     *
     * @return whether the stand is visible or not
     */
    boolean isVisible();

    /**
     * Sets whether the armor stand should be visible or not.
     *
     * @param visible whether the stand is visible or not
     */
    void setVisible(boolean visible);

    /**
     * Returns whether this armor stand has arms.
     *
     * @return whether this has arms or not
     */
    boolean hasArms();

    /**
     * Sets whether this armor stand has arms.
     *
     * @param arms whether this has arms or not
     */
    void setArms(boolean arms);

    /**
     * Returns whether this armor stand is scaled down.
     *
     * @return whether this is scaled down
     */
    boolean isSmall();

    /**
     * Sets whether this armor stand is scaled down.
     *
     * @param small whether this is scaled down
     */
    void setSmall(boolean small);

    /**
     * Returns whether this armor stand is a marker, meaning it has a very small
     * collision box.
     *
     * @return whether this is a marker
     */
    boolean isMarker();

    /**
     * Sets whether this armor stand is a marker, meaning it has a very small
     * collision box.
     *
     * @param marker whether this is a marker
     */
    void setMarker(boolean marker);

    /**
     * Locks the equipment slot with the specified
     * {@link LockType locking mechanism}.
     *
     * @param slot the equipment slot to lock
     * @param lockType the LockType to lock the equipment slot with
     */
    void addEquipmentLock(@NotNull EquipmentSlot slot, @NotNull LockType lockType);

    /**
     * Remove a {@link LockType locking mechanism}.
     *
     * @param slot the equipment slot to change
     * @param lockType the LockType to remove
     */
    void removeEquipmentLock(@NotNull EquipmentSlot slot, @NotNull LockType lockType);

    /**
     * Returns if the ArmorStand has the specified
     * {@link LockType locking mechanism}.
     *
     * @param slot the EquipmentSlot to test
     * @param lockType the LockType to test
     * @return if the ArmorStand has been locked with the parameters specified
     */
    boolean hasEquipmentLock(@NotNull EquipmentSlot slot, @NotNull LockType lockType);

    /**
     * Represents types of locking mechanisms for ArmorStand equipment.
     */
    public enum LockType {

        /**
         * Prevents adding or changing the respective equipment - players cannot
         * replace the empty slot with a new item or swap the items between
         * themselves and the ArmorStand.
         */
        ADDING_OR_CHANGING,
        /**
         * Prevents removing or changing the respective equipment - players
         * cannot take an item from the slot or swap the items between
         * themselves and the ArmorStand.
         */
        REMOVING_OR_CHANGING,
        /**
         * Prevents adding the respective equipment - players cannot replace the
         * empty slot with a new item, but can swap items between themselves and
         * the ArmorStand.
         */
        ADDING;
    }
    // Paper start
    /**
     * Tests if this armor stand can move.
     *
     * <p>The default value is {@code true}.</p>
     *
     * @return {@code true} if this armour stand can move, {@code false} otherwise
     */
    boolean canMove();

    /**
     * Sets if this armor stand can move.
     *
     * @param move {@code true} if this armour stand can move, {@code false} otherwise
     */
    void setCanMove(boolean move);

    @Override
    org.bukkit.inventory.@NotNull EntityEquipment getEquipment();

    /**
     * Tests if this armor stand can tick.
     *
     * <p>The default value is defined in {@code paper.yml}.</p>
     *
     * @return {@code true} if this armour stand can tick, {@code false} otherwise
     */
    boolean canTick();

    /**
     * Sets if this armor stand can tick.
     *
     * @param tick {@code true} if this armour stand can tick, {@code false} otherwise
     */
    void setCanTick(final boolean tick);

    /**
     * Returns the item the armor stand has
     * equipped in the given equipment slot
     *
     * @param slot the equipment slot to get
     * @return the ItemStack in the equipment slot
     * @throws IllegalArgumentException if the slot is invalid for the entity
     */
    @NotNull
    ItemStack getItem(@NotNull final org.bukkit.inventory.EquipmentSlot slot);

    /**
     * Sets the item the armor stand has
     * equipped in the given equipment slot
     *
     * @param slot the equipment slot to set
     * @param item the item to hold
     * @throws IllegalArgumentException if the slot is invalid for the entity
     */
    void setItem(@NotNull final org.bukkit.inventory.EquipmentSlot slot, @Nullable final ItemStack item);

    /**
     * Get the list of disabled slots
     *
     * @return list of disabled slots
     */
    @NotNull
    java.util.Set<org.bukkit.inventory.EquipmentSlot> getDisabledSlots();

    /**
     * Set the disabled slots
     *
     * This makes it so a player is unable to interact with the Armor Stand to place, remove, or replace an item in the given slot(s)
     * Note: Once a slot is disabled, the only way to get an item back it to break the armor stand.
     *
     * @param slots var-arg array of slots to lock
     */
    void setDisabledSlots(@NotNull org.bukkit.inventory.EquipmentSlot... slots);

    /**
     * Disable specific slots, adding them
     * to the currently disabled slots
     *
     * This makes it so a player is unable to interact with the Armor Stand to place, remove, or replace an item in the given slot(s)
     * Note: Once a slot is disabled, the only way to get an item back it to break the armor stand.
     *
     * @param slots var-arg array of slots to lock
     */
    void addDisabledSlots(@NotNull final org.bukkit.inventory.EquipmentSlot... slots);

    /**
     * Remove the given slots from the disabled
     * slots list, enabling them.
     *
     * This makes it so a player is able to interact with the Armor Stand to place, remove, or replace an item in the given slot(s)
     *
     * @param slots var-arg array of slots to unlock
     */
    void removeDisabledSlots(@NotNull final org.bukkit.inventory.EquipmentSlot... slots);

    /**
     * Check if a specific slot is disabled
     *
     * @param slot The slot to check
     * @return {@code true} if the slot is disabled, else {@code false}.
     */
    boolean isSlotDisabled(@NotNull org.bukkit.inventory.EquipmentSlot slot);

    /**
     * Returns the ArmorStand's body rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getBodyRotations();

    /**
     * Sets the ArmorStand's body rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setBodyRotations(@NotNull io.papermc.paper.math.Rotations rotations);

    /**
     * Returns the ArmorStand's left arm rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getLeftArmRotations();

    /**
     * Sets the ArmorStand's left arm rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setLeftArmRotations(@NotNull io.papermc.paper.math.Rotations rotations);

    /**
     * Returns the ArmorStand's right arm rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getRightArmRotations();

    /**
     * Sets the ArmorStand's right arm rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setRightArmRotations(@NotNull io.papermc.paper.math.Rotations rotations);

    /**
     * Returns the ArmorStand's left leg rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getLeftLegRotations();

    /**
     * Sets the ArmorStand's left leg rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setLeftLegRotations(@NotNull io.papermc.paper.math.Rotations rotations);

    /**
     * Returns the ArmorStand's right leg rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getRightLegRotations();

    /**
     * Sets the ArmorStand's right leg rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setRightLegRotations(@NotNull io.papermc.paper.math.Rotations rotations);

    /**
     * Returns the ArmorStand's head rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @return the current rotations
     */
    @NotNull io.papermc.paper.math.Rotations getHeadRotations();

    /**
     * Sets the ArmorStand's head rotations as
     * {@link io.papermc.paper.math.Rotations}.
     *
     * @param rotations the current rotations
     */
    void setHeadRotations(@NotNull io.papermc.paper.math.Rotations rotations);
    // Paper end
}
