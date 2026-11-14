package io.papermc.paper.loot;

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

class LootContextKeyImpl {

    static final Map<Key, LootContextKey> KEYS = new HashMap<>();

    static <T> LootContextKey.Valued<T> valued(@KeyPattern.Value final String name) {
        record ValuedImpl<T>(Key key) implements LootContextKey.Valued<T> {
        }

        final ValuedImpl<T> contextKey = new ValuedImpl<>(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (KEYS.put(contextKey.key(), contextKey) != null) {
            throw new IllegalStateException("Already registered " + name);
        }
        return contextKey;
    }

    static LootContextKey.NonValued unvalued(@KeyPattern.Value final String name) {
        record NonValuedImpl(Key key) implements LootContextKey.NonValued {
        }

        final NonValuedImpl contextKey = new NonValuedImpl(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (KEYS.put(contextKey.key(), contextKey) != null) {
            throw new IllegalStateException("Already registered " + name);
        }
        return contextKey;
    }
}
