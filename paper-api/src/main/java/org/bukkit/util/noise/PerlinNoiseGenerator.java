package org.bukkit.util.noise;

import java.util.Random;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Generates noise using the "classic" perlin generator
 *
 * @see SimplexNoiseGenerator "Improved" and faster version with slightly
 *     different results
 */
public class PerlinNoiseGenerator extends NoiseGenerator {
    protected static final int[][] grad3 = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
        {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
        {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    private static final PerlinNoiseGenerator instance = new PerlinNoiseGenerator();

    protected PerlinNoiseGenerator() {
        int[] p = {151, 160, 137, 91, 90, 15, 131, 13, 201,
            95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
            240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
            94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
            87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
            48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
            230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25,
            63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200,
            196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
            64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255,
            82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153,
            101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
            113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
            193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
            14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
            176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
            114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

        for (int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
        }
    }

    /**
     * Creates a seeded perlin noise generator for the given world
     *
     * @param world World to construct this generator for
     */
    public PerlinNoiseGenerator(@NotNull World world) {
        this(new Random(world.getSeed()));
    }

    /**
     * Creates a seeded perlin noise generator for the given seed
     *
     * @param seed Seed to construct this generator for
     */
    public PerlinNoiseGenerator(long seed) {
        this(new Random(seed));
    }

    /**
     * Creates a seeded perlin noise generator with the given Random
     *
     * @param rand Random to construct with
     */
    public PerlinNoiseGenerator(@NotNull Random rand) {
        offsetX = rand.nextDouble() * 256;
        offsetY = rand.nextDouble() * 256;
        offsetZ = rand.nextDouble() * 256;

        for (int i = 0; i < 256; i++) {
            perm[i] = rand.nextInt(256);
        }

        for (int i = 0; i < 256; i++) {
            int pos = rand.nextInt(256 - i) + i;
            int old = perm[i];

            perm[i] = perm[pos];
            perm[pos] = old;
            perm[i + 256] = perm[i];
        }
    }

    /**
     * Computes and returns the 1D unseeded perlin noise for the given
     * coordinates in 1D space
     *
     * @param x X coordinate
     * @return Noise at given location, from range -1 to 1
     */
    public static double getNoise(double x) {
        return instance.noise(x);
    }

    /**
     * Computes and returns the 2D unseeded perlin noise for the given
     * coordinates in 2D space
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Noise at given location, from range -1 to 1
     */
    public static double getNoise(double x, double y) {
        return instance.noise(x, y);
    }

    /**
     * Computes and returns the 3D unseeded perlin noise for the given
     * coordinates in 3D space
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return Noise at given location, from range -1 to 1
     */
    public static double getNoise(double x, double y, double z) {
        return instance.noise(x, y, z);
    }

    /**
     * Gets the singleton unseeded instance of this generator
     *
     * @return Singleton
     */
    @NotNull
    public static PerlinNoiseGenerator getInstance() {
        return instance;
    }

    @Override
    public double noise(double x, double y, double z) {
        x += offsetX;
        y += offsetY;
        z += offsetZ;

        int floorX = floor(x);
        int floorY = floor(y);
        int floorZ = floor(z);

        // Find unit cube containing the point
        int X = floorX & 255;
        int Y = floorY & 255;
        int Z = floorZ & 255;

        // Get relative xyz coordinates of the point within the cube
        x -= floorX;
        y -= floorY;
        z -= floorZ;

        // Compute fade curves for xyz
        double fX = fade(x);
        double fY = fade(y);
        double fZ = fade(z);

        // Hash coordinates of the cube corners
        int A = perm[X] + Y;
        int AA = perm[A] + Z;
        int AB = perm[A + 1] + Z;
        int B = perm[X + 1] + Y;
        int BA = perm[B] + Z;
        int BB = perm[B + 1] + Z;

        return lerp(fZ, lerp(fY, lerp(fX, grad(perm[AA], x, y, z),
                        grad(perm[BA], x - 1, y, z)),
                    lerp(fX, grad(perm[AB], x, y - 1, z),
                        grad(perm[BB], x - 1, y - 1, z))),
                lerp(fY, lerp(fX, grad(perm[AA + 1], x, y, z - 1),
                        grad(perm[BA + 1], x - 1, y, z - 1)),
                    lerp(fX, grad(perm[AB + 1], x, y - 1, z - 1),
                        grad(perm[BB + 1], x - 1, y - 1, z - 1))));
    }

    /**
     * Generates noise for the 1D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x X-coordinate
     * @param octaves Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    public static double getNoise(double x, int octaves, double frequency, double amplitude) {
        return instance.noise(x, octaves, frequency, amplitude);
    }

    /**
     * Generates noise for the 2D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param octaves Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    public static double getNoise(double x, double y, int octaves, double frequency, double amplitude) {
        return instance.noise(x, y, octaves, frequency, amplitude);
    }

    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @param octaves Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    public static double getNoise(double x, double y, double z, int octaves, double frequency, double amplitude) {
        return instance.noise(x, y, z, octaves, frequency, amplitude);
    }
}
