package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.FireworkEffect;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Stores all explosions crafted into a Firework Rocket, as well as flight duration.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#FIREWORKS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Fireworks {

    @Contract(value = "_, _ -> new", pure = true)
    static Fireworks fireworks(final List<FireworkEffect> effects, final int flightDuration) {
        return fireworks().addEffects(effects).flightDuration(flightDuration).build();
    }

    @Contract(value = "-> new", pure = true)
    static Fireworks.Builder fireworks() {
        return ItemComponentTypesBridge.bridge().fireworks();
    }

    /**
     * Lists the effects stored in this component.
     *
     * @return the effects
     */
    @Contract(pure = true)
    @Unmodifiable List<FireworkEffect> effects();

    /**
     * Number of gunpowder in this component.
     *
     * @return the flight duration
     */
    @Contract(pure = true)
    @IntRange(from = 0, to = 255) int flightDuration();

    /**
     * Builder for {@link Fireworks}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<Fireworks> {

        /**
         * Sets the number of gunpowder used in this builder.
         *
         * @param duration duration
         * @return the builder for chaining
         * @see #flightDuration()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder flightDuration(@IntRange(from = 0, to = 255) int duration);

        /**
         * Adds an explosion to this builder.
         *
         * @param effect effect
         * @return the builder for chaining
         * @see #effects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffect(FireworkEffect effect);

        /**
         * Adds explosions to this builder.
         *
         * @param effects effects
         * @return the builder for chaining
         * @see #effects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffects(List<FireworkEffect> effects);
    }
}
