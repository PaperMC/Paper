package io.papermc.paper.ban;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record BanListTypeImpl<T>(Class<T> typeClass) implements BanListType<T> {
}
