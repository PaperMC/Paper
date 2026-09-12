package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.loot.number.ResolvableFloat;
import io.papermc.paper.loot.number.ResolvableInt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Describes an item that can be used as fuel for a brewing stand.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BREWING_FUEL
 */
@ApiStatus.NonExtendable
public interface BrewingFuel {

    @Contract(value = "-> new", pure = true)
    static BrewingFuel.Builder brewingFuel() {
        return ItemComponentTypesBridge.bridge().brewingFuel();
    }

    /**
     * @return the number of times this fuel will brew before being consumed
     */
    @Contract(pure = true)
    ResolvableInt uses();

    /**
     * @return the speed of the brewing
     */
    @Contract(pure = true)
    ResolvableFloat speedMultiplier();

    /**
     * Builder for {@link BrewingFuel}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BrewingFuel> {

        /**
         * @param uses the number of times this fuel will brew before being consumed
         * @return the builder for chaining
         * @see #uses()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder uses(int uses);

        /**
         * @param speedMultiplier the speed of the brewing
         * @return the builder for chaining
         * @see #speedMultiplier()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder speedMultiplier(float speedMultiplier);
    }
}
