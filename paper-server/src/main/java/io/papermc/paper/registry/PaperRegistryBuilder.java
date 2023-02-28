package io.papermc.paper.registry;

import io.papermc.paper.registry.data.util.Conversions;
import org.jspecify.annotations.Nullable;

public interface PaperRegistryBuilder<M, T> extends RegistryBuilder<T> {

    M build();

    @FunctionalInterface
    interface Filler<M, T, B extends PaperRegistryBuilder<M, T>> {

        B fill(Conversions conversions, @Nullable M nms);

        default Factory<M, T, B> asFactory() {
            return (lookup) -> this.fill(lookup, null);
        }
    }

    @FunctionalInterface
    interface Factory<M, T, B extends PaperRegistryBuilder<M, T>> {

        B create(Conversions conversions);
    }
}
