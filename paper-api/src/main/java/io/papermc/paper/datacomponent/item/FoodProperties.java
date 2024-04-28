package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the food properties of an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#FOOD
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface FoodProperties extends BuildableDataComponent<FoodProperties, FoodProperties.Builder> {

    @Contract(value = "-> new", pure = true)
    static FoodProperties.Builder food() {
        return ItemComponentTypesBridge.bridge().food();
    }

    /**
     * Number of food points to restore when eaten.
     *
     * @return the nutrition
     */
    @Contract(pure = true)
    @NonNegative int nutrition();

    /**
     * Amount of saturation to restore when eaten.
     *
     * @return the saturation
     */
    @Contract(pure = true)
    float saturation();

    /**
     * If {@code true}, this food can be eaten even if not hungry.
     *
     * @return can always be eaten
     */
    @Contract(pure = true)
    boolean canAlwaysEat();

    /**
     * Builder for {@link FoodProperties}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<FoodProperties> {

        /**
         * Set if this food can always be eaten, even if the
         * player is not hungry.
         *
         * @param canAlwaysEat true to allow always eating
         * @return the builder for chaining
         * @see #canAlwaysEat()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder canAlwaysEat(boolean canAlwaysEat);

        /**
         * Sets the saturation of the food.
         *
         * @param saturation the saturation
         * @return the builder for chaining
         * @see #saturation()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder saturation(float saturation);

        /**
         * Sets the nutrition of the food.
         *
         * @param nutrition the nutrition, must be non-negative
         * @return the builder for chaining
         * @see #nutrition()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder nutrition(@NonNegative int nutrition);
    }
}
