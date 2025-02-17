package io.papermc.paper.math.provider;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public sealed interface IntProvider permits IntProvider.Constant, IntProvider.Uniform, IntProvider.BiasedToBottom, IntProvider.Clamped, IntProvider.WeightedList, IntProvider.ClampedNormal {

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Constant extends IntProvider {

        int value();
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Uniform extends IntProvider {

        int minInclusive();

        int maxInclusive();
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface BiasedToBottom extends IntProvider {

        int minInclusive();

        int maxInclusive();
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface Clamped extends IntProvider {

        IntProvider source();

        int minInclusive();

        int maxInclusive();
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface WeightedList extends IntProvider {
        // TODO implement later when we have more need for "weighted" API
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    non-sealed interface ClampedNormal extends IntProvider {

        float mean();

        float deviation();

        int minInclusive();

        int maxInclusive();
    }
}
