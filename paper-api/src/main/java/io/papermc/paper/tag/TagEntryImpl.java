package io.papermc.paper.tag;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
record TagEntryImpl<T>(Key key, boolean isTag, boolean isRequired) implements TagEntry<T> {
}
