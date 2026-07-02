package io.papermc.paper.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
record TypedKeyImpl<T>(Key key, RegistryKey<T> registryKey) implements TypedKey<T> {
    // Wrap key methods to make this easier to use
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof final TypedKey<?> otherTyped) {
            return this.key.equals(otherTyped.key()) && this.registryKey.equals(otherTyped.registryKey());
        }
        // A TypedKey is a Key, so it must compare equal to a plain Key with the same
        // namespace and value (adventure's Key equality) to keep equals symmetric.
        return other instanceof final Key otherKey
            && this.key.namespace().equals(otherKey.namespace())
            && this.key.value().equals(otherKey.value());
    }

    @Override
    public int hashCode() {
        // Match KeyImpl#hashCode so a TypedKey and an equal plain Key share a hash code.
        return this.key.hashCode();
    }

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
