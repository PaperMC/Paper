package io.papermc.paper.math.provider;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Represents a provider of a random integer.
 */
@ApiStatus.Experimental
public sealed interface IntProvider permits IntProvider.Constant, IntProvider.Uniform, IntProvider.BiasedToBottom, IntProvider.Clamped, IntProvider.WeightedList, IntProvider.ClampedNormal {

    /**
     * Creates a constant provider of a given value.
     *
     * @param value the value to always return
     * @return a constant provider of the given value
     */
    @Contract(pure = true, value = "_ -> new")
    static IntProvider.Constant constant(final int value) {
        return IntProviderProvider.get().constant(value);
    }

    /**
     * Creates a uniform provider of a given range.
     *
     * @param min the minimum value of the range (inclusive)
     * @param max the maximum value of the range (inclusive)
     * @return a uniform provider of the given range
     */
    @Contract(pure = true, value = "_, _ -> new")
    static IntProvider.Uniform uniform(final int min, final int max) {
        return IntProviderProvider.get().uniform(min, max);
    }

    /**
     * Creates a biased-to-bottom provider of a given range.
     *
     * @param min the minimum value of the range (inclusive)
     * @param max the maximum value of the range (inclusive)
     * @return a biased-to-bottom provider of the given range
     */
    @Contract(pure = true, value = "_, _ -> new")
    static IntProvider.BiasedToBottom biasedToBottom(final int min, final int max) {
        return IntProviderProvider.get().biasedToBottom(min, max);
    }

    /**
     * Creates a clamped provider of a given source and range.
     *
     * @param source the source provider
     * @param min    the minimum value of the range (inclusive)
     * @param max    the maximum value of the range (inclusive)
     * @return a clamped provider of the given source and range
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    static IntProvider.Clamped clamped(final IntProvider source, final int min, final int max) {
        return IntProviderProvider.get().clamped(source, min, max);
    }

    /**
     * Creates a weighted list provider of a given distribution.
     *
     * @param distribution the distribution of weighted providers
     * @return a weighted list provider of the given distribution
     */
    @Contract(pure = true, value = "_ -> new")
    static IntProvider.WeightedList weightedList(final List<WeightedIntProvider> distribution) {
        return IntProviderProvider.get().weightedList(distribution);
    }

    /**
     * Creates a clamped-normal provider of a given mean, deviation, and range.
     *
     * @param mean      the mean of the distribution
     * @param deviation the standard deviation of the distribution
     * @param min       the minimum value of the range (inclusive)
     * @param max       the maximum value of the range (inclusive)
     * @return a clamped-normal provider of the given mean, deviation, and range
     */
    @Contract(pure = true, value = "_, _, _, _ -> new")
    static IntProvider.ClampedNormal clampedNormal(final float mean, final float deviation, final int min, final int max) {
        return IntProviderProvider.get().clampedNormal(mean, deviation, min, max);
    }

    /**
     * Represents a constant provider of a single value.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Constant extends IntProvider {

        /**
         * Returns the value of this constant provider.
         *
         * @return the value
         */
        @Contract(pure = true)
        int value();
    }

    /**
     * Represents a uniform provider of a given range.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Uniform extends IntProvider {

        /**
         * Returns the minimum value of the range of this uniform provider.
         *
         * @return the minimum value
         */
        @Contract(pure = true)
        int minInclusive();

        /**
         * Returns the maximum value of the range of this uniform provider.
         *
         * @return the maximum value
         */
        @Contract(pure = true)
        int maxInclusive();
    }

    /**
     * Represents a biased-to-bottom provider of a given range.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface BiasedToBottom extends IntProvider {

        /**
         * Returns the minimum value of the range of this biased-to-bottom provider.
         *
         * @return the minimum value
         */
        @Contract(pure = true)
        int minInclusive();

        /**
         * Returns the maximum value of the range of this biased-to-bottom provider.
         *
         * @return the maximum value
         */
        @Contract(pure = true)
        int maxInclusive();
    }

    /**
     * Represents a clamped provider of a given source and range.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Clamped extends IntProvider {

        /**
         * Returns the source provider of this clamped provider.
         *
         * @return the source provider
         */
        @Contract(pure = true)
        IntProvider source();

        /**
         * Returns the minimum value of the range of this clamped provider.
         *
         * @return the minimum value
         */
        @Contract(pure = true)
        int minInclusive();

        /**
         * Returns the maximum value of the range of this clamped provider.
         *
         * @return the maximum value
         */
        @Contract(pure = true)
        int maxInclusive();
    }

    /**
     * Represents a weighted list provider.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface WeightedList extends IntProvider {

        /**
         * Returns the distribution of this weighted list provider.
         *
         * @return the distribution
         */
        @Contract(pure = true)
        @Unmodifiable
        List<WeightedIntProvider> distribution();
    }

    /**
     * Represents a clamped-normal provider of a given mean, deviation, and range.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface ClampedNormal extends IntProvider {

        /**
         * Returns the mean of the distribution of this clamped-normal provider.
         *
         * @return the mean of the distribution
         */
        @Contract(pure = true)
        float mean();

        /**
         * Returns the standard deviation of the distribution of this clamped-normal provider.
         *
         * @return the standard deviation
         */
        @Contract(pure = true)
        float deviation();

        /**
         * Returns the minimum value of the range of this clamped-normal provider.
         *
         * @return the minimum value
         */
        @Contract(pure = true)
        int minInclusive();

        /**
         * Returns the maximum value of the range of this clamped-normal provider.
         *
         * @return the maximum value
         */
        @Contract(pure = true)
        int maxInclusive();
    }
}
