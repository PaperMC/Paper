package org.bukkit.util.noise;

import java.util.Random;
import org.bukkit.World;

/**
 * Creates perlin noise through unbiased octaves
 */
public class PerlinOctaveGenerator extends OctaveGenerator {
    /**
     * Creates a perlin octave generator for the given world
     *
     * @param world World to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public PerlinOctaveGenerator(World world, int octaves) {
        this(new Random(world.getSeed()), octaves);
    }

    /**
     * Creates a perlin octave generator for the given world
     *
     * @param seed Seed to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public PerlinOctaveGenerator(long seed, int octaves) {
        this(new Random(seed), octaves);
    }

    /**
     * Creates a perlin octave generator for the given {@link Random}
     *
     * @param rand Random object to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public PerlinOctaveGenerator(Random rand, int octaves) {
        super(createOctaves(rand, octaves));
    }

    private static NoiseGenerator[] createOctaves(Random rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];

        for (int i = 0; i < octaves; i++) {
            result[i] = new PerlinNoiseGenerator(rand);
        }

        return result;
    }
}
