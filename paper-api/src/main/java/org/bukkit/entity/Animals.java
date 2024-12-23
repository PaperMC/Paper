package org.bukkit.entity;

import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Animal.
 *
 * @since 1.0.0
 */
public interface Animals extends Breedable {

    /**
     * Get the UUID of the entity that caused this entity to enter the
     * {@link #canBreed()} state.
     *
     * @return uuid if set, or null
     * @since 1.13.1
     */
    @Nullable
    UUID getBreedCause();

    /**
     * Set the UUID of the entity that caused this entity to enter the
     * {@link #canBreed()} state.
     *
     * @param uuid new uuid, or null
     * @since 1.13.1
     */
    void setBreedCause(@Nullable UUID uuid);

    /**
     * Get whether or not this entity is in love mode and will produce
     * offspring with another entity in love mode. Will return true if
     * and only if {@link #getLoveModeTicks()} is greater than 0.
     *
     * @return true if in love mode, false otherwise
     * @since 1.13.2
     */
    boolean isLoveMode();

    /**
     * Get the amount of ticks remaining for this entity in love mode.
     * If the entity is not in love mode, 0 will be returned.
     *
     * @return the remaining love mode ticks
     * @since 1.13.2
     */
    int getLoveModeTicks();

    /**
     * Set the amount of ticks for which this entity should be in love mode.
     * Setting the love mode ticks to 600 is the equivalent of a player
     * feeding the entity their breeding item of choice.
     *
     * @param ticks the love mode ticks. Must be positive
     * @since 1.13.2
     */
    void setLoveModeTicks(int ticks);

    /**
     * Check if the provided ItemStack is the correct item used for breeding
     * this entity.
     *
     * @param stack ItemStack to check.
     * @return if the provided ItemStack is the correct food item for this
     * entity.
     * @since 1.17
     */
    boolean isBreedItem(@NotNull ItemStack stack);

    /**
     * Check if the provided ItemStack is the correct item used for breeding
     * this entity..
     *
     * @param material Material to check.
     * @return if the provided ItemStack is the correct food item for this
     * entity.
     * @since 1.17
     */
    boolean isBreedItem(@NotNull Material material);
}
