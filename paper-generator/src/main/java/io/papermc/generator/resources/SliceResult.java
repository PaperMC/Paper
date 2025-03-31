package io.papermc.generator.resources;

import java.util.Collections;
import java.util.Set;

public record SliceResult<A, R>(Set<A> added, Set<R> removed) {

    static <A, R> SliceResult<A, R> empty() {
        return new SliceResult<>(Collections.emptySet(), Collections.emptySet());
    }

    static <V, A, R> SliceResult<A, R> empty(V value) {
        return empty();
    }

    public boolean isEmpty() {
        return this.added.isEmpty() && this.removed.isEmpty();
    }
}
