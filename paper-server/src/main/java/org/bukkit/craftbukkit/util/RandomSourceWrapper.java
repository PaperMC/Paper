package org.bukkit.craftbukkit.util;

import java.util.Random;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public final class RandomSourceWrapper implements RandomSource {

    private final Random random;

    public RandomSourceWrapper(Random random) {
        this.random = random;
    }

    @Override
    public RandomSource fork() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void setSeed(long seed) {
        random.setSeed(seed);
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public synchronized double nextGaussian() {
        return random.nextGaussian();
    }

    public static final class RandomWrapper extends Random {

        private final RandomSource random;

        public RandomWrapper(RandomSource random) {
            this.random = random;
        }

        @Override
        public void setSeed(long l) {
            random.setSeed(l);
        }

        @Override
        public int nextInt() {
            return random.nextInt();
        }

        @Override
        public int nextInt(int i) {
            return random.nextInt(i);
        }

        @Override
        public long nextLong() {
            return random.nextLong();
        }

        @Override
        public boolean nextBoolean() {
            return random.nextBoolean();
        }

        @Override
        public float nextFloat() {
            return random.nextFloat();
        }

        @Override
        public double nextDouble() {
            return random.nextDouble();
        }

        @Override
        public double nextGaussian() {
            return random.nextGaussian();
        }

        @Override
        public int nextInt(int var0, int var1) {
            return random.nextInt(var0, var1);
        }
    }
}
