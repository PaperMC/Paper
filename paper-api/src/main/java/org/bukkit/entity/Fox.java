package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What does the fox say?
 */
public interface Fox extends Animals, Sittable {

    /**
     * Gets the current type of this fox.
     *
     * @return Type of the fox.
     */
    @NotNull
    public Type getFoxType();

    /**
     * Sets the current type of this fox.
     *
     * @param type New type of this fox.
     */
    public void setFoxType(@NotNull Type type);

    /**
     * Checks if this animal is crouching
     *
     * @return true if crouching
     */
    boolean isCrouching();

    /**
     * Sets if this animal is crouching.
     *
     * @param crouching true if crouching
     */
    void setCrouching(boolean crouching);

    /**
     * Sets if this animal is sleeping.
     *
     * @param sleeping true if sleeping
     */
    void setSleeping(boolean sleeping);

    /**
     * Gets the first trusted player.
     *
     * @return the owning AnimalTamer, or null if not owned
     */
    @Nullable
    public AnimalTamer getFirstTrustedPlayer();

    /**
     * Set the first trusted player.
     * <p>
     * The first trusted player may only be removed after the second.
     *
     * @param player the AnimalTamer to be trusted
     */
    public void setFirstTrustedPlayer(@Nullable AnimalTamer player);

    /**
     * Gets the second trusted player.
     *
     * @return the owning AnimalTamer, or null if not owned
     */
    @Nullable
    public AnimalTamer getSecondTrustedPlayer();

    /**
     * Set the second trusted player.
     * <p>
     * The second trusted player may only be added after the first.
     *
     * @param player the AnimalTamer to be trusted
     */
    public void setSecondTrustedPlayer(@Nullable AnimalTamer player);

    /**
     * Gets whether the fox is faceplanting the ground
     *
     * @return Whether the fox is faceplanting the ground
     */
    boolean isFaceplanted();

    /**
     * Represents the various different fox types there are.
     */
    public enum Type {
        RED,
        SNOW;
    }
}
