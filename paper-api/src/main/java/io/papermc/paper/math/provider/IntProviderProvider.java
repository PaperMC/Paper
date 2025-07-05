package io.papermc.paper.math.provider;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

interface IntProviderProvider {

    static IntProviderProvider get() {
        final class Holder {
            static final Optional<IntProviderProvider> INSTANCE = ServiceLoader.load(IntProviderProvider.class).findFirst();
        }

        return Holder.INSTANCE.orElseThrow();
    }

    IntProvider.Constant constant(int value);

    IntProvider.Uniform uniform(int min, int max);

    IntProvider.BiasedToBottom biasedToBottom(int min, int max);

    IntProvider.Clamped clamped(IntProvider source, int min, int max);

    IntProvider.WeightedList weightedList(List<WeightedIntProvider> distribution);

    IntProvider.ClampedNormal clampedNormal(float mean, float deviation, int min, int max);
}
