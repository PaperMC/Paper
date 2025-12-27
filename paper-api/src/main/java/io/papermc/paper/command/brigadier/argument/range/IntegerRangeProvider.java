package io.papermc.paper.command.brigadier.argument.range;

import org.jetbrains.annotations.ApiStatus;

/**
 * A provider for a {@link com.google.common.collect.Range} of integers.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#integerRange()
 */
@ApiStatus.NonExtendable
public non-sealed interface IntegerRangeProvider extends RangeProvider<Integer> {
}
