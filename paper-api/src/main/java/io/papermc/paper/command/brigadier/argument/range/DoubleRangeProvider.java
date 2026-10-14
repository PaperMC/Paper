package io.papermc.paper.command.brigadier.argument.range;

import org.jetbrains.annotations.ApiStatus;

/**
 * A provider for a {@link com.google.common.collect.Range} of doubles.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#doubleRange()
 */
@ApiStatus.NonExtendable
public non-sealed interface DoubleRangeProvider extends RangeProvider<Double> {
}
