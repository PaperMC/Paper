package io.papermc.paper.util;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record EitherLeft<L, R>(L value) implements Either.Left<L, R> {
}
