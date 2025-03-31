package io.papermc.generator.resources;

import org.jspecify.annotations.Nullable;

public record FlattenSliceResult<A, R>(@Nullable A added, @Nullable R removed) {
}
