package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.inventory.meta.trim.TrimMaterial} type.
 */
@ApiStatus.NonExtendable
public interface TrimMaterialRegistryEntry {

    /**
     * Provides the palette texture key to be used for this trim material.
     *
     * @return the palette texture key
     */
    @Contract(pure = true)
    Key paletteId();

    /**
     * Provides the description of the trim material.
     *
     * @return the description
     */
    @Contract(pure = true)
    Component description();

    /**
     * A mutable builder for {@link TrimMaterialRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #paletteId(Key)}</li>
     *     <li>{@link #description(Component)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends TrimMaterialRegistryEntry, RegistryBuilder<TrimMaterial> {

        /**
         * Sets the palette texture key to be used for this trim material.
         *
         * @param paletteId the palette texture key
         * @return this builder instance
         * @see #paletteId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder paletteId(Key paletteId);

        /**
         * Sets the description for the trim material.
         *
         * @param description the description
         * @return this builder instance
         * @see #description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);
    }
}
