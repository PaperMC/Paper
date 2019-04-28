package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

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
     * Represents the various different fox types there are.
     */
    public enum Type {
        RED,
        SNOW;
    }
}
