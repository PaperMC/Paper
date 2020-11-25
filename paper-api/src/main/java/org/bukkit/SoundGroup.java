package org.bukkit;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a group of sounds for blocks that are played when various actions
 * happen (ie stepping, breaking, hitting, etc).
 */
public interface SoundGroup {

    /**
     * Get the volume these sounds are played at.
     *
     * Note that this volume does not always represent the actual volume
     * received by the client.
     *
     * @return volume
     */
    public float getVolume();

    /**
     * Gets the pitch these sounds are played at.
     *
     * Note that this pitch does not always represent the actual pitch received
     * by the client.
     *
     * @return pitch
     */
    public float getPitch();

    /**
     * Gets the corresponding breaking sound for this group.
     *
     * @return the break sound
     */
    @NotNull
    public Sound getBreakSound();

    /**
     * Gets the corresponding step sound for this group.
     *
     * @return the step sound
     */
    @NotNull
    public Sound getStepSound();

    /**
     * Gets the corresponding place sound for this group.
     *
     * @return the place sound
     */
    @NotNull
    public Sound getPlaceSound();

    /**
     * Gets the corresponding hit sound for this group.
     *
     * @return the hit sound
     */
    @NotNull
    public Sound getHitSound();

    /**
     * Gets the corresponding fall sound for this group.
     *
     * @return the fall sound
     */
    @NotNull
    public Sound getFallSound();
}
