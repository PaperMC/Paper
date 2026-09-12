package io.papermc.paper.math.provider;

import org.jetbrains.annotations.Contract;

/**
 * A weighted provider of a random integer.
 */
public sealed interface WeightedIntProvider permits WeightedIntProviderImpl {

    /**
     * Creates a weighted provider of a given weight and provider.
     *
     * @param weight   the weight
     * @param provider the provider
     * @return a weighted provider of the given weight and provider
     */
    @Contract(pure = true, value = "_, _ -> new")
    static WeightedIntProvider create(final int weight, final IntProvider provider) {
        return new WeightedIntProviderImpl(weight, provider);
    }

    /**
     * Returns the weight of this weighted provider.
     *
     * @return the weight
     */
    @Contract(pure = true)
    int weight();

    /**
     * Returns the provider of this weighted provider.
     *
     * @return the provider
     */
    @Contract(pure = true)
    IntProvider provider();
}
