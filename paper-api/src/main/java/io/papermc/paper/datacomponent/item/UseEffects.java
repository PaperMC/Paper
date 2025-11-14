package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface UseEffects {

    /**
     * Returns a new builder for creating a UseEffects component.
     *
     * @return a builder instance.
     */
    static Builder useEffects() {
        return ItemComponentTypesBridge.bridge().useEffects();
    }

    @Contract(pure = true)
    boolean canSprint();

    @Contract(pure = true)
    boolean interactVibrations();

    @Contract(pure = true)
    float speedMultiplier();

    /**
     * Builder for {@link UseEffects}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<UseEffects> {

        /**
         * Sets whether the player can sprint while using the item.
         *
         * @param canSprint true to allow sprinting
         * @return the builder for chaining
         * @see #canSprint()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder canSprint(boolean canSprint);

        /**
         * Sets whether using the item generates interaction vibrations.
         *
         * @param interactVibrations true to generate vibrations
         * @return the builder for chaining
         * @see #interactVibrations()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder interactVibrations(boolean interactVibrations);

        /**
         * Sets the speed multiplier while using the item.
         *
         * @param speedMultiplier multiplier (1.0 >= value >= 0.0)
         * @return the builder for chaining
         * @see #speedMultiplier()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder speedMultiplier(float speedMultiplier);
    }
}
