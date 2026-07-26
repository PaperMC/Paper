package org.bukkit.inventory.meta.components;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a component which adds custom model data.
 */
@ApiStatus.Experimental
public interface CustomModelDataComponent extends ConfigurationSerializable {

    /**
     * Gets a list of the floats for the range_dispatch model type.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Float> getFloats();

    /**
     * Sets a list of the floats for the range_dispatch model type.
     *
     * @param floats new list
     */
    void setFloats(@NotNull List<Float> floats);

    /**
     * Gets a list of the booleans for the condition model type.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Boolean> getFlags();

    /**
     * Sets a list of the booleans for the condition model type.
     *
     * @param flags new list
     */
    void setFlags(@NotNull List<Boolean> flags);

    /**
     * Gets a list of strings for the select model type.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<String> getStrings();

    /**
     * Sets a list of strings for the select model type.
     *
     * @param strings new list
     */
    void setStrings(@NotNull List<String> strings);

    /**
     * Gets a list of colors for the model type's tints.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Color> getColors();

    /**
     * Sets a list of colors for the model type's tints.
     *
     * @param colors new list
     */
    void setColors(@NotNull List<Color> colors);
}
