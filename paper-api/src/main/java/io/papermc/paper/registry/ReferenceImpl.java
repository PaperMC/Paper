package io.papermc.paper.registry;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

record ReferenceImpl<T extends Keyed>(@NotNull Registry<T> registry, @NotNull NamespacedKey key) implements Reference<T> {

    @Override
    public @NotNull T value() {
        final T value = this.registry.get(this.key);
        if (value == null) {
            throw new NoSuchElementException("No such value with key " + this.key);
        }
        return value;
    }

    @Override
    public @Nullable T valueOrNull() {
        return this.registry.get(this.key);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }
}
