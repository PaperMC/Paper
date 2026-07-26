package io.papermc.paper.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
record TypedKeyImpl<T>(Key key, RegistryKey<T> registryKey) implements TypedKey<T> {
    // Wrap key methods to make this easier to use
    @KeyPattern.Namespace
    @Override
    public String namespace() {
        return this.key.namespace();
    }

    @KeyPattern.Value
    @Override
    public String value() {
        return this.key.value();
    }

    @Override
    public String asString() {
        return this.key.asString();
    }
}
