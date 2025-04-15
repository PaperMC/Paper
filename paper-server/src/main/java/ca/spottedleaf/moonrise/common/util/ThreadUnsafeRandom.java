package ca.spottedleaf.moonrise.common.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.BitRandomSource;
import net.minecraft.world.level.levelgen.MarsagliaPolarGaussian;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

/**
 * A fast, non-thread-safe random number generator optimized for single-threaded usage.
 * <p>
 * IMPORTANT: This class is NOT thread-safe. Multiple threads accessing the same instance
 * will produce unpredictable results and lead to data corruption. Use either:
 * - One instance per thread (using ThreadLocal)
 * - Synchronization when accessing from multiple threads
 * - A thread-safe alternative when shared access is required
 * </p>
 * <p>
 * This implementation uses a linear congruential generator (LCG) for fast random number
 * generation with good statistical properties for game mechanics and simulations.
 * It trades thread safety for performance in single-threaded contexts.
 * </p>
 */
public class ThreadUnsafeRandom implements BitRandomSource { // Paper - replace random

    private static final long MULTIPLIER = 25214903917L;
    private static final long ADDEND = 11L;
    private static final int BITS = 48;
    private static final long MASK = (1L << BITS) - 1L;

    private long value;
    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public ThreadUnsafeRandom(final long seed) {
        this.setSeed(seed);
    }

    @Override
    public void setSeed(final long seed) {
        this.value = (seed ^ MULTIPLIER) & MASK;
        this.gaussianSource.reset();
    }

    private long advanceSeed() {
        return this.value = ((this.value * MULTIPLIER) + ADDEND) & MASK;
    }

    @Override
    public int next(final int bits) {
        return (int)(this.advanceSeed() >>> (BITS - bits));
    }

    @Override
    public int nextInt() {
        final long seed = this.advanceSeed();
        return (int)(seed >>> (BITS - Integer.SIZE));
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive)
     * and the specified bound (exclusive).
     *
     * @param bound the upper bound (exclusive) for the random value
     * @return a random int in the range [0, bound)
     * @throws IllegalArgumentException if bound is not positive
     */
    public int nextInt(final int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        // Fast path for power of 2 bounds
        if ((bound & -bound) == bound) {
            return (int)(advanceSeed() & (bound - 1));
        }

        // Rejection sampling for uniform distribution
        int r = nextInt();
        int m = bound - 1;
        if ((bound & m) == 0) {
            // Power of two case
            r = (int)((bound * (long)r) >> 31);
        } else {
            // General case
            int u = r >>> 1;
            while (u + m - (r = u % bound) < 0) {
                u = nextInt() >>> 1;
            }
        }

        return r;
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    @Override
    public RandomSource fork() {
        return new ThreadUnsafeRandom(this.nextLong());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new ThreadUnsafeRandomPositionalFactory(this.nextLong());
    }

    /**
     * Factory for creating position-based random sources.
     * This allows for consistent random generation based on world positions.
     */
    public static final class ThreadUnsafeRandomPositionalFactory implements PositionalRandomFactory {

        private final long seed;

        public ThreadUnsafeRandomPositionalFactory(final long seed) {
            this.seed = seed;
        }

        public long getSeed() {
            return this.seed;
        }

        @Override
        public RandomSource fromHashOf(final String string) {
            return new ThreadUnsafeRandom((long)string.hashCode() ^ this.seed);
        }

        @Override
        public RandomSource fromSeed(final long seed) {
            return new ThreadUnsafeRandom(seed);
        }

        @Override
        public RandomSource at(final int x, final int y, final int z) {
            return new ThreadUnsafeRandom(Mth.getSeed(x, y, z) ^ this.seed);
        }

        @Override
        public void parityConfigString(final StringBuilder stringBuilder) {
            stringBuilder.append("ThreadUnsafeRandomPositionalFactory{").append(this.seed).append('}');
        }
    }
}
