package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface SwingAnimation {

    @Contract(value = "-> new", pure = true)
    static Builder swingAnimation() {
        return ItemComponentTypesBridge.bridge().swingAnimation();
    }

    @Contract(pure = true)
    Animation type();

    @Contract(pure = true)
    @Positive int duration();

    enum Animation {
        // Start generate - SwingAnimationType
        NONE,
        WHACK,
        STAB;
        // End generate - SwingAnimationType
    }

    /**
     * Builder for {@link SwingAnimation}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<SwingAnimation> {

        /**
         * Sets the swing animation type.
         *
         * @param type animation type
         * @return the builder for chaining
         * @see #type()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder type(Animation type);

        /**
         * Sets the swing animation duration in ticks.
         *
         * @param duration duration (>= 0)
         * @return the builder for chaining
         * @see #duration()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder duration(@Positive int duration);
    }
}
