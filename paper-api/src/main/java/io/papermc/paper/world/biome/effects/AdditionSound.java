package io.papermc.paper.world.biome.effects;

import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

/**
 * Settings for additions sound, which can play along
 * the biome music and provides extra ambience to the biome.
 */
@ApiStatus.Experimental
public interface AdditionSound {

    /**
     * The additions sound.
     *
     * @return the sound event.
     */
    Sound sound();

    /**
     * The probability to start playing the sound per tick.
     *
     * @return the probability.
     */
    @Range(from = 0, to = 1)
    double tickChance();
}
