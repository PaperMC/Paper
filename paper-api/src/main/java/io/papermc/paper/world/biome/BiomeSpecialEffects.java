package io.papermc.paper.world.biome;

import io.papermc.paper.util.RGBColor;
import io.papermc.paper.world.biome.effects.AdditionSound;
import io.papermc.paper.world.biome.effects.MoodSound;
import io.papermc.paper.world.biome.effects.MusicEntry;
import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.Optional;

/**
 * Ambient effects in this biome.
 */
@NullMarked
public interface BiomeSpecialEffects {
    /**
     * The fog color.
     *
     * @return color to use for fog.
     */
    RGBColor fogColor();

    /**
     * The water color.
     *
     * @return color to use for water blocks and cauldrons.
     */
    RGBColor waterColor();

    /**
     * The water fog color.
     *
     * @return color to use for fog underwater.
     */
    RGBColor waterFogColor();

    /**
     * The sky color.
     *
     * @return color to use for the sky.
     */
    RGBColor skyColor();

    /**
     * The color to use for tree leaves and vines.
     * If not present, the value depends on downfall and the temperature.
     *
     * @return foliage color.
     */
    Optional<RGBColor> foliageColor();

    /**
     * The color to use for grass blocks, short grass,
     * tall grass, ferns, tall ferns, and sugar cane.
     * If not present, the value depends on downfall and the temperature.
     *
     * @return grass color.
     */
    Optional<RGBColor> grassColor();

    /**
     * The biome ambient sound.
     *
     * @return the ambient sound.
     */
    Optional<Sound> ambientSound();

    /**
     * The mood sound settings.
     *
     * @return the mood sound settings.
     */
    Optional<MoodSound> moodSound();

    /**
     * The addition sound settings.
     *
     * @return the addition sound settings.
     */
    Optional<AdditionSound> additionSound();

    /**
     * Specific music that should be played in the biome.
     *
     * @return the music entries.
     */
    List<MusicEntry> music();

    /**
     * The game smoothly transitions between the current music volume
     * and the new volume when entering the biome.
     *
     * @return the music volume of this biome.
     */
    float musicVolume();

    /**
     * This method applies the unique biome grass color modifiers
     * depending on the coordinates of the block.
     *
     * @param original the initial grass color.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return the modified grass color.
     */
    RGBColor modifyGrassColor(RGBColor original, double x, double y, double z);
}
