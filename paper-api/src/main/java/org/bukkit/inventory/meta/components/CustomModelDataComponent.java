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
     * Gets a list of the custom floats.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Float> getFloats();

    /**
     * Sets a list of the custom floats.
     *
     * @param floats new list
     */
    void setFloats(@NotNull List<Float> floats);

    /**
     * Gets a list of the custom flags.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Boolean> getFlags();

    /**
     * Sets a list of the custom flags.
     *
     * @param flags new list
     */
    void setFlags(@NotNull List<Boolean> flags);

    /**
     * Gets a list of the custom strings.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<String> getStrings();

    /**
     * Sets a list of the custom strings.
     *
     * @param strings new list
     */
    void setStrings(@NotNull List<String> strings);

    /**
     * Gets a list of the custom colors.
     *
     * @return unmodifiable list
     */
    @NotNull
    List<Color> getColors();

    /**
     * Sets a list of the custom colors.
     *
     * @param colors new list
     */
    void setColors(@NotNull List<Color> colors);
}
