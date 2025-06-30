package io.papermc.paper.command.brigadier.argument.range;

import com.google.common.collect.Range;

/**
 * A provider for a range of numbers
 *
 * @param <T>
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes
 */
public sealed interface RangeProvider<T extends Comparable<?>> permits DoubleRangeProvider, IntegerRangeProvider {

    /**
     * Provides the given range.
     * @return range
     */
    Range<T> range();
}
