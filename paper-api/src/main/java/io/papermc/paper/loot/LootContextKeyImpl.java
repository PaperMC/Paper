package io.papermc.paper.loot;

import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
record LootContextKeyImpl<T>(Key key) implements LootContextKey<T> {

    static final Set<LootContextKey<?>> KEYS = new HashSet<>();

    static <T> LootContextKey<T> create(@KeyPattern final String name) {
        final LootContextKeyImpl<T> key = new LootContextKeyImpl<>(Key.key(name));
        if (!KEYS.add(key)) {
            throw new IllegalStateException("Already registered " + name);
        }
        return key;
    }
}
