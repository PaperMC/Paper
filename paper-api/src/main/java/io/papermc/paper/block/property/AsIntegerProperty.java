package io.papermc.paper.block.property;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.function.IntFunction;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
sealed interface AsIntegerProperty<T extends Comparable<T>> extends BlockProperty<T> permits NoteBlockProperty, RotationBlockProperty {

    static <T extends Comparable<T>> BiMap<Integer, T> createCache(final int max, final IntFunction<? extends T> function) {
        final ImmutableBiMap.Builder<Integer, T> builder = ImmutableBiMap.builder();
        for (int i = 0; i <= max; i++) {
            builder.put(i, function.apply(i));
        }
        return builder.buildOrThrow();
    }

    BiMap<Integer, T> cache();

    default int toIntValue(final T value) {
        if (!this.cache().inverse().containsKey(value)) {
            throw ExceptionCreator.INSTANCE.create(value, ExceptionCreator.Type.VALUE, this);
        }
        return this.cache().inverse().get(value);
    }

    default T fromIntValue(final int value) {
        if (!this.cache().containsKey(value)) {
            throw ExceptionCreator.INSTANCE.create(value, ExceptionCreator.Type.VALUE, this);
        }
        return this.cache().get(value);
    }
}
