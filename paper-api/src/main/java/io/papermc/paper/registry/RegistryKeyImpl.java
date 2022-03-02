package io.papermc.paper.registry;

import com.google.common.collect.Sets;
import java.util.Set;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
record RegistryKeyImpl<T>(Key key) implements RegistryKey<T> {

    static final Set<RegistryKey<?>> REGISTRY_KEYS = Sets.newIdentityHashSet();

    // override equals and hashCode to this can be used to simulate an "identity" hashmap
    @Override
    public boolean equals(final @Nullable Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    static <T> RegistryKey<T> create(@Subst("some_key") final String key) {
        final RegistryKey<T> registryKey = createInternal(key);
        REGISTRY_KEYS.add(registryKey);
        return registryKey;
    }

    // creates the key without adding to the internal set of keys
    static <T> RegistryKey<T> createInternal(@Subst("some_key") final String key) {
        return new RegistryKeyImpl<>(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

}
