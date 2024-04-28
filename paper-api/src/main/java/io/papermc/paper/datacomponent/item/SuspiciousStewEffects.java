package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the effects that will be applied when consuming Suspicious Stew.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#SUSPICIOUS_STEW_EFFECTS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface SuspiciousStewEffects {

    @Contract(value = "_ -> new", pure = true)
    static SuspiciousStewEffects suspiciousStewEffects(final Collection<SuspiciousEffectEntry> effects) {
        return suspiciousStewEffects().addAll(effects).build();
    }

    @Contract(value = "-> new", pure = true)
    static SuspiciousStewEffects.Builder suspiciousStewEffects() {
        return ItemComponentTypesBridge.bridge().suspiciousStewEffects();
    }

    /**
     * Effects that will be applied when consuming Suspicious Stew.
     *
     * @return effects
     */
    @Contract(pure = true)
    @Unmodifiable List<SuspiciousEffectEntry> effects();

    /**
     * Builder for {@link SuspiciousStewEffects}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<SuspiciousStewEffects> {

        /**
         * Adds an effect applied to this builder.
         *
         * @param entry effect
         * @return the builder for chaining
         * @see #effects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(SuspiciousEffectEntry entry);

        /**
         * Adds effects applied to this builder.
         *
         * @param entries effect
         * @return the builder for chaining
         * @see #effects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(Collection<SuspiciousEffectEntry> entries);
    }
}
