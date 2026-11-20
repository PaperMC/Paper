package io.papermc.paper.ban;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record BanListTypeImpl<T>(Class<T> typeClass) implements BanListType<T> {
}
