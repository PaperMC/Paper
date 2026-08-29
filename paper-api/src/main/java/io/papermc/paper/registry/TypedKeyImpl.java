package io.papermc.paper.registry;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
record TypedKeyImpl<T>(Key key, RegistryKey<T> registryKey) implements TypedKey<T> {
}
