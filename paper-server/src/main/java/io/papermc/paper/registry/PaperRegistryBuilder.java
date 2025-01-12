package io.papermc.paper.registry;

import io.papermc.paper.registry.data.util.Conversions;
import org.jspecify.annotations.Nullable;

public interface PaperRegistryBuilder<M, T> extends RegistryBuilder<T> {

    M build();

    @FunctionalInterface
    interface Filler<M, T, B extends PaperRegistryBuilder<M, T>> {

        B fill(Conversions conversions, @Nullable M nms);

        default B create(final Conversions conversions) {
            return this.fill(conversions, null);
        }
    }
}
