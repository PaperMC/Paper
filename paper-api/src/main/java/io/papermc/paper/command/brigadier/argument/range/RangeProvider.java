package io.papermc.paper.command.brigadier.argument.range;

import com.google.common.collect.Range;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A provider for a range of numbers
 *
 * @param <T>
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes
 */
@ApiStatus.Experimental
@NullMarked
public sealed interface RangeProvider<T extends Comparable<?>> permits DoubleRangeProvider, IntegerRangeProvider {

    /**
     * Provides the given range.
     * @return range
     */
    Range<T> range();
}
