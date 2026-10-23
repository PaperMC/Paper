package org.bukkit;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a group of sounds for blocks that are played when various actions
 * happen (ie stepping, breaking, hitting, etc.).
 */
@NullMarked
public interface SoundGroup {

    /**
     * Get the volume these sounds are played at.
     * <p>
     * Note that this volume does not always represent the actual volume
     * received by the client.
     *
     * @return volume
     */
    float getVolume();

    /**
     * Gets the pitch these sounds are played at.
     * <p>
     * Note that this pitch does not always represent the actual pitch received
     * by the client.
     *
     * @return pitch
     */
    float getPitch();

    /**
     * Gets the corresponding breaking sound for this group.
     *
     * @return the break sound, or {@link Sound#BLOCK_STONE_BREAK} if this group has none
     * @deprecated sounds may now be nullable, use {@link #breakSound()} instead.
     */
    @Deprecated(since = "26.4")
    Sound getBreakSound();

    /**
     * Gets the corresponding step sound for this group.
     *
     * @return the step sound, or {@link Sound#BLOCK_STONE_STEP} if this group has none
     * @deprecated sounds may now be nullable, use {@link #stepSound()} instead.
     */
    @Deprecated(since = "26.4")
    Sound getStepSound();

    /**
     * Gets the corresponding place sound for this group.
     *
     * @return the place sound, or {@link Sound#BLOCK_STONE_PLACE} if this group has none
     * @deprecated sounds may now be nullable, use {@link #placeSound()} instead.
     */
    @Deprecated(since = "26.4")
    Sound getPlaceSound();

    /**
     * Gets the corresponding hit sound for this group.
     *
     * @return the hit sound, or {@link Sound#BLOCK_STONE_HIT} if this group has none
     * @deprecated sounds may now be nullable, use {@link #hitSound()} instead.
     */
    @Deprecated(since = "26.4")
    Sound getHitSound();

    /**
     * Gets the corresponding fall sound for this group.
     *
     * @return the fall sound, or {@link Sound#BLOCK_STONE_FALL} if this group has none
     * @deprecated sounds may now be nullable, use {@link #fallSound()} instead.
     */
    @Deprecated(since = "26.4")
    Sound getFallSound();

    /**
     * Gets the corresponding breaking sound for this group.
     *
     * @return the break sound, or {@code null} if this group has none
     */
    @Nullable Sound breakSound();

    /**
     * Gets the corresponding step sound for this group.
     *
     * @return the step sound, or {@code null} if this group has none
     */
    @Nullable Sound stepSound();

    /**
     * Gets the corresponding place sound for this group.
     *
     * @return the place sound, or {@code null} if this group has none
     */
    @Nullable Sound placeSound();

    /**
     * Gets the corresponding hit sound for this group.
     *
     * @return the hit sound, or {@code null} if this group has none
     */
    @Nullable Sound hitSound();

    /**
     * Gets the corresponding fall sound for this group.
     *
     * @return the fall sound, or {@code null} if this group has none
     */
    @Nullable Sound fallSound();
}
