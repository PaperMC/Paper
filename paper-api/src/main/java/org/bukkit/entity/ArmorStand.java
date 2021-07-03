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
     * @deprecated prefer {@link EntityEquipment#getItemInHand()}
     */
    @NotNull
    @Deprecated
    ItemStack getItemInHand();

    /**
     * Sets the item the armor stand is currently holding.
     *
     * @param item the item to hold
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setItemInHand(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated
    void setItemInHand(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its feet.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getBoots()}
     */
    @NotNull
    @Deprecated
    ItemStack getBoots();

    /**
     * Sets the item currently being worn by the armor stand on its feet.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setBoots(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated
    void setBoots(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its legs.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getLeggings()}
     */
    @NotNull
    @Deprecated
    ItemStack getLeggings();

    /**
     * Sets the item currently being worn by the armor stand on its legs.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setLeggings(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated
    void setLeggings(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its chest.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getChestplate()}
     */
    @NotNull
    @Deprecated
    ItemStack getChestplate();

    /**
     * Sets the item currently being worn by the armor stand on its chest.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setChestplate(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated
    void setChestplate(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn by the armor stand on its head.
     *
     * @return the worn item
     * @see #getEquipment()
     * @deprecated prefer {@link EntityEquipment#getHelmet()}
     */
    @NotNull
    @Deprecated
    ItemStack getHelmet();

    /**
     * Sets the item currently being worn by the armor stand on its head.
     *
     * @param item the item to wear
     * @see #getEquipment()
     * @deprecated prefer
     * {@link EntityEquipment#setHelmet(org.bukkit.inventory.ItemStack)}
     */
    @Deprecated
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
     * @param basePlate whether is has a base plate
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
}
