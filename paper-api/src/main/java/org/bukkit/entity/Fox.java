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
        // Start generate - FoxType
        // @GeneratedFrom 1.21.6-pre1
        RED,
        SNOW;
        // End generate - FoxType
    }

    // Paper start - Add more fox behavior API
    /**
     * Sets if the fox is interested.
     *
     * @param interested is interested
     */
    public void setInterested(boolean interested);

    /**
     * Gets if the fox is interested.
     *
     * @return fox is interested
     */
    public boolean isInterested();

    /**
     * Sets if the fox is leaping.
     *
     * @param leaping is leaping
     */
    public void setLeaping(boolean leaping);

    /**
     * Gets if the fox is leaping.
     *
     * @return fox is leaping
     */
    public boolean isLeaping();

    /**
     * Sets if the fox is defending.
     *
     * @param defending is defending
     */
    public void setDefending(boolean defending);

    /**
     * Gets if the fox is defending.
     *
     * @return fox is defending
     */
    public boolean isDefending();

    /**
     * Sets if the fox face planted.
     *
     * @param faceplanted face planted
     */
    public void setFaceplanted(boolean faceplanted);
    // Paper end - Add more fox behavior API
}
