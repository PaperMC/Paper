package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.loot.number.ResolvableFloat;
import io.papermc.paper.loot.number.ResolvableInt;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Describes an item that can be used as fuel for a furnace, smoker or blast furnace.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#COOKING_FUEL
 */
@ApiStatus.NonExtendable
public interface CookingFuel {

    @Contract(value = "-> new", pure = true)
    static CookingFuel.Builder cookingFuel() {
        return ItemComponentTypesBridge.bridge().cookingFuel();
    }

    /**
     * @return the time, in ticks, for which this fuel will burn
     */
    @Contract(pure = true)
    ResolvableInt burnTime();

    /**
     * @return the speed of the cooking/smelting
     */
    @Contract(pure = true)
    ResolvableFloat speedMultiplier();

    /**
     * Builder for {@link CookingFuel}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<CookingFuel> {

        /**
         * @param speedMultiplier the speed of the cooking/smelting
         * @return the builder for chaining
         * @see #speedMultiplier()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder speedMultiplier(float speedMultiplier);

        /**
         * @param burnTime the time, in ticks, for which this fuel will burn
         * @return the builder for chaining
         * @see #speedMultiplier()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder burnTime(int burnTime);
    }
}
