package io.papermc.paper.world.biome;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import java.util.Optional;

/**
 * Ambient effects in this biome.
 */
public interface BiomeSpecialEffects {

    /**
     * The water color.
     *
     * @return color to use for water blocks and cauldrons.
     */
    Color waterColor();

    /**
     * The color to use for tree leaves and vines.
     * If not present, the value depends on downfall and the temperature.
     *
     * @return foliage color.
     */
    Optional<Color> foliageColor();

    /**
     * The color used by dry foliage like {@link BlockType#LEAF_LITTER}.
     * If not present, the value depends on downfall and the temperature.
     *
     * @return dry foliage color.
     */
    Optional<Color> dryFoliageColor();

    /**
     * The color to use for grass blocks, short grass,
     * tall grass, ferns, tall ferns, and sugar cane.
     * If not present, the value depends on downfall and the temperature.
     *
     * @return grass color.
     */
    Optional<Color> grassColor();

    /**
     * This method applies the unique biome grass color modifiers
     * depending on the coordinates of the block.
     *
     * @param original the initial grass color.
     * @param location the location of the block.
     * @return the modified grass color.
     */
    Color modifyGrassColor(Color original, Location location);
}
