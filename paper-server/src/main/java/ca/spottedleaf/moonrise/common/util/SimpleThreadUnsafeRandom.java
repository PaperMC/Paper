package ca.spottedleaf.moonrise.common.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.BitRandomSource;
import net.minecraft.world.level.levelgen.MarsagliaPolarGaussian;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

/**
 * Avoid costly CAS of superclass + division in nextInt
 */
public final class SimpleThreadUnsafeRandom implements BitRandomSource {

    private static final long MULTIPLIER = 25214903917L;
    private static final long ADDEND = 11L;
    private static final int BITS = 48;
    private static final long MASK = (1L << BITS) - 1L;

    private long value;
    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public SimpleThreadUnsafeRandom(final long seed) {
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

    @Override
    public int nextInt(final int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException();
        }

        // https://lemire.me/blog/2016/06/27/a-fast-alternative-to-the-modulo-reduction/
        final long value = this.advanceSeed() >>> (BITS - Integer.SIZE);
        return (int)((value * (long)bound) >>> Integer.SIZE);
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    @Override
    public RandomSource fork() {
        return new SimpleThreadUnsafeRandom(this.nextLong());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new SimpleRandomPositionalFactory(this.nextLong());
    }

    public static final class SimpleRandomPositionalFactory implements PositionalRandomFactory {

        private final long seed;

        public SimpleRandomPositionalFactory(final long seed) {
            this.seed = seed;
        }

        public long getSeed() {
            return this.seed;
        }

        @Override
        public RandomSource fromHashOf(final String string) {
            return new SimpleThreadUnsafeRandom((long)string.hashCode() ^ this.seed);
        }

        @Override
        public RandomSource fromSeed(final long seed) {
            return new SimpleThreadUnsafeRandom(seed);
        }

        @Override
        public RandomSource at(final int x, final int y, final int z) {
            return new SimpleThreadUnsafeRandom(Mth.getSeed(x, y, z) ^ this.seed);
        }

        @Override
        public void parityConfigString(final StringBuilder stringBuilder) {
            stringBuilder.append("SimpleRandomPositionalFactory{").append(this.seed).append('}');
        }
    }
}
