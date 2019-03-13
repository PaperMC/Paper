package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArmorStand extends LivingEntity {

    /**
     * Returns the item the armor stand is
     * currently holding
     *
     * @return the held item
     */
    @NotNull
    ItemStack getItemInHand();

    /**
     * Sets the item the armor stand is currently
     * holding
     *
     * @param item the item to hold
     */
    void setItemInHand(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its feet
     *
     * @return the worn item
     */
    @NotNull
    ItemStack getBoots();

    /**
     * Sets the item currently being worn
     * by the armor stand on its feet
     *
     * @param item the item to wear
     */
    void setBoots(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its legs
     *
     * @return the worn item
     */
    @NotNull
    ItemStack getLeggings();

    /**
     * Sets the item currently being worn
     * by the armor stand on its legs
     *
     * @param item the item to wear
     */
    void setLeggings(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its chest
     *
     * @return the worn item
     */
    @NotNull
    ItemStack getChestplate();

    /**
     * Sets the item currently being worn
     * by the armor stand on its chest
     *
     * @param item the item to wear
     */
    void setChestplate(@Nullable ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its head
     *
     * @return the worn item
     */
    @NotNull
    ItemStack getHelmet();

    /**
     * Sets the item currently being worn
     * by the armor stand on its head
     *
     * @param item the item to wear
     */
    void setHelmet(@Nullable ItemStack item);

    /**
     * Returns the armor stand's body's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getBodyPose();

    /**
     * Sets the armor stand's body's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setBodyPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's left arm's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getLeftArmPose();

    /**
     * Sets the armor stand's left arm's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setLeftArmPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's right arm's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getRightArmPose();

    /**
     * Sets the armor stand's right arm's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setRightArmPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's left leg's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getLeftLegPose();

    /**
     * Sets the armor stand's left leg's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setLeftLegPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's right leg's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getRightLegPose();

    /**
     * Sets the armor stand's right leg's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setRightLegPose(@NotNull EulerAngle pose);

    /**
     * Returns the armor stand's head's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @return the current pose
     */
    @NotNull
    EulerAngle getHeadPose();

    /**
     * Sets the armor stand's head's
     * current pose as a {@link org.bukkit.util.EulerAngle}
     *
     * @param pose the current pose
     */
    void setHeadPose(@NotNull EulerAngle pose);

    /**
     * Returns whether the armor stand has
     * a base plate
     *
     * @return whether it has a base plate
     */
    boolean hasBasePlate();

    /**
     * Sets whether the armor stand has a
     * base plate
     *
     * @param basePlate whether is has a base plate
     */
    void setBasePlate(boolean basePlate);

    /**
     * Returns whether the armor stand should be
     * visible or not
     *
     * @return whether the stand is visible or not
     */
    boolean isVisible();

    /**
     * Sets whether the armor stand should be
     * visible or not
     *
     * @param visible whether the stand is visible or not
     */
    void setVisible(boolean visible);

    /**
     * Returns whether this armor stand has arms
     *
     * @return whether this has arms or not
     */
    boolean hasArms();

    /**
     * Sets whether this armor stand has arms
     *
     * @param arms whether this has arms or not
     */
    void setArms(boolean arms);

    /**
     * Returns whether this armor stand is scaled
     * down
     *
     * @return whether this is scaled down
     */
    boolean isSmall();

    /**
     * Sets whether this armor stand is scaled
     * down
     *
     * @param small whether this is scaled down
     */
    void setSmall(boolean small);

    /**
     * Returns whether this armor stand is a marker,
     * meaning it has a very small collision box
     *
     * @return whether this is a marker
     */
    boolean isMarker();

    /**
     * Sets whether this armor stand is a marker,
     * meaning it has a very small collision box
     *
     * @param marker whether this is a marker
     */
    void setMarker(boolean marker);
}
