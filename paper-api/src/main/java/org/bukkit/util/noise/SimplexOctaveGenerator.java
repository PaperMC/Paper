package org.bukkit.util.noise;

import java.util.Random;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Creates simplex noise through unbiased octaves
 */
public class SimplexOctaveGenerator extends OctaveGenerator {
    private double wScale = 1;

    /**
     * Creates a simplex octave generator for the given world
     *
     * @param world World to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public SimplexOctaveGenerator(@NotNull World world, int octaves) {
        this(new Random(world.getSeed()), octaves);
    }

    /**
     * Creates a simplex octave generator for the given world
     *
     * @param seed Seed to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public SimplexOctaveGenerator(long seed, int octaves) {
        this(new Random(seed), octaves);
    }

    /**
     * Creates a simplex octave generator for the given {@link Random}
     *
     * @param rand Random object to construct this generator for
     * @param octaves Amount of octaves to create
     */
    public SimplexOctaveGenerator(@NotNull Random rand, int octaves) {
        super(createOctaves(rand, octaves));
    }

    @Override
    public void setScale(double scale) {
        super.setScale(scale);
        setWScale(scale);
    }

    /**
     * Gets the scale used for each W-coordinates passed
     *
     * @return W scale
     */
    public double getWScale() {
        return wScale;
    }

    /**
     * Sets the scale used for each W-coordinates passed
     *
     * @param scale New W scale
     */
    public void setWScale(double scale) {
        wScale = scale;
    }

    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @param w W-coordinate
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    public double noise(double x, double y, double z, double w, double frequency, double amplitude) {
        return noise(x, y, z, w, frequency, amplitude, false);
    }

    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @param w W-coordinate
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    public double noise(double x, double y, double z, double w, double frequency, double amplitude, boolean normalized) {
        double result = 0;
        double amp = 1;
        double freq = 1;
        double max = 0;

        x *= xScale;
        y *= yScale;
        z *= zScale;
        w *= wScale;

        for (NoiseGenerator octave : octaves) {
            result += ((SimplexNoiseGenerator) octave).noise(x * freq, y * freq, z * freq, w * freq) * amp;
            max += amp;
            freq *= frequency;
            amp *= amplitude;
        }

        if (normalized) {
            result /= max;
        }

        return result;
    }

    @NotNull
    private static NoiseGenerator[] createOctaves(@NotNull Random rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];

        for (int i = 0; i < octaves; i++) {
            result[i] = new SimplexNoiseGenerator(rand);
        }

        return result;
    }
}
