package io.papermc.paper.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public record EitherRight<L, R>(R value) implements Either.Right<L, R> {
}
