package io.papermc.paper.world.biome.effects;

import org.bukkit.Sound;
import org.jetbrains.annotations.Range;

/**
 * Settings for additions sound, which can play along
 * the biome music and provides extra ambience to the biome.
 */
public interface AdditionSound {

    /**
     * One sound event — The additions sound.
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
