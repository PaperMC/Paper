package io.papermc.paper.registry.tag;

import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record TagKeyImpl<T>(RegistryKey<T> registryKey, Key key) implements TagKey<T> {

    @Override
    public String toString() {
        return "#" + this.key + " (in " + this.registryKey + ")";
    }
}
