package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.Color;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the custom model data.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CUSTOM_MODEL_DATA
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CustomModelData {

    @Contract(value = "-> new", pure = true)
    static CustomModelData.Builder customModelData() {
        return ItemComponentTypesBridge.bridge().customModelData();
    }

    /**
     * Gets the custom model data float values.
     *
     * @return the float values
     */
    @Contract(pure = true)
    @Unmodifiable List<Float> floats();

    /**
     * Gets the custom model data boolean values.
     *
     * @return the boolean values
     */
    @Contract(pure = true)
    @Unmodifiable List<Boolean> flags();

    /**
     * Gets the custom model data string values.
     *
     * @return the string values
     */
    @Contract(pure = true)
    @Unmodifiable List<String> strings();

    /**
     * Gets the custom model data color values.
     *
     * @return the color values
     */
    @Contract(pure = true)
    @Unmodifiable List<Color> colors();

    /**
     * Builder for {@link CustomModelData}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<CustomModelData> {

        /**
         * Adds a float to this custom model data.
         *
         * @param f the float
         * @return the builder for chaining
         * @see #floats()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addFloat(float f);

        /**
         * Adds multiple floats to this custom model data.
         *
         * @param floats the floats
         * @return the builder for chaining
         * @see #floats()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addFloats(List<Float> floats);

        /**
         * Adds a flag to this custom model data.
         *
         * @param flag the flag
         * @return the builder for chaining
         * @see #flags()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addFlag(boolean flag);

        /**
         * Adds multiple flags to this custom model data.
         *
         * @param flags the flags
         * @return the builder for chaining
         * @see #flags()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addFlags(List<Boolean> flags);

        /**
         * Adds a string to this custom model data.
         *
         * @param string the string
         * @return the builder for chaining
         * @see #strings()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addString(String string);

        /**
         * Adds multiple strings to this custom model data.
         *
         * @param strings the strings
         * @return the builder for chaining
         * @see #strings()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addStrings(List<String> strings);

        /**
         * Adds a color to this custom model data.
         *
         * @param color the color
         * @return the builder for chaining
         * @see #colors()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addColor(Color color);

        /**
         * Adds multiple colors to this custom model data.
         *
         * @param colors the colors
         * @return the builder for chaining
         * @see #colors()
         */
        @Contract(value = "_ -> this", mutates = "this")
        CustomModelData.Builder addColors(List<Color> colors);
    }
}
