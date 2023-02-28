package io.papermc.paper.registry.legacy;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jspecify.annotations.Nullable;

/**
 * This is to support the now-deprecated fields in {@link Registry} for
 * data-driven registries.
 */
public final class DelayedRegistry<T extends Keyed, R extends Registry<T>> implements Registry<T> {

    private @Nullable Supplier<? extends R> delegate;

    public void load(final Supplier<? extends R> registry) {
        if (this.delegate != null) {
            throw new IllegalStateException("Registry already loaded!");
        }
        this.delegate = registry;
    }

    public Registry<T> delegate() {
        if (this.delegate == null) {
            throw new IllegalStateException("You are trying to access this registry too early!");
        }
        return this.delegate.get();
    }

    @Override
    public @Nullable T get(final NamespacedKey key) {
        return this.delegate().get(key);
    }

    @Override
    public T getOrThrow(final NamespacedKey key) {
        return this.delegate().getOrThrow(key);
    }

    @Override
    public Iterator<T> iterator() {
        return this.delegate().iterator();
    }

    @Override
    public Stream<T> stream() {
        return this.delegate().stream();
    }

    @Override
    public @Nullable NamespacedKey getKey(final T value) {
        return this.delegate().getKey(value);
    }
}
