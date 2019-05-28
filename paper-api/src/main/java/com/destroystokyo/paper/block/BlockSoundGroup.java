package com.destroystokyo.paper.block;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the sounds that a {@link Block} makes in certain situations
 * <p>
 * The sound group includes break, step, place, hit, and fall sounds.
 * @deprecated use {@link org.bukkit.SoundGroup}
 */
@Deprecated(forRemoval = true, since = "1.18.2")
public interface BlockSoundGroup {
    /**
     * Gets the sound that plays when breaking this block
     *
     * @return The break sound
     * @deprecated use {@link org.bukkit.SoundGroup#getBreakSound()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    Sound getBreakSound();

    /**
     * Gets the sound that plays when stepping on this block
     *
     * @return The step sound
     * @deprecated use {@link org.bukkit.SoundGroup#getStepSound()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    Sound getStepSound();

    /**
     * Gets the sound that plays when placing this block
     *
     * @return The place sound
     * @deprecated use {@link org.bukkit.SoundGroup#getPlaceSound()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    Sound getPlaceSound();

    /**
     * Gets the sound that plays when hitting this block
     *
     * @return The hit sound
     * @deprecated use {@link org.bukkit.SoundGroup#getHitSound()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    Sound getHitSound();

    /**
     * Gets the sound that plays when this block falls
     *
     * @return The fall sound
     * @deprecated use {@link org.bukkit.SoundGroup#getFallSound()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.18.2")
    Sound getFallSound();
}
