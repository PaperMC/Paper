package io.papermc.paper.math.provider;

import java.util.Optional;
import java.util.ServiceLoader;

interface IntProviderProvider {

    static IntProviderProvider get() {
        final class Holder {
            static final Optional<IntProviderProvider> INSTANCE = ServiceLoader.load(IntProviderProvider.class).findFirst();
        }

        return Holder.INSTANCE.orElseThrow();
    }

    IntProvider.Constant constant(final int value);

    IntProvider.Uniform uniform(final int min, final int max);

    IntProvider.BiasedToBottom biasedToBottom(final int min, final int max);

    IntProvider.Clamped clamped(final IntProvider source, final int min, final int max);

    IntProvider.ClampedNormal clampedNormal(final float mean, final float deviation, final int min, final int max);
}
