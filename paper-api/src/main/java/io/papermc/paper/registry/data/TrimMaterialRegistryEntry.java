package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/// A data-centric version-specific registry entry for the [org.bukkit.inventory.meta.trim.TrimMaterial] type.
@ApiStatus.NonExtendable
public interface TrimMaterialRegistryEntry {

    /// Provides the palette texture id to be used for this trim material.
    ///
    /// @return the palette texture id
    @Contract(pure = true)
    Key paletteId();

    /// Provides the description of the trim material.
    ///
    /// @return the description
    @Contract(pure = true)
    Component description();

    /// A mutable builder for [TrimMaterialRegistryEntry] plugins may change in applicable registry events.
    ///
    /// The following values are required for each builder:
    ///
    ///   - [#paletteId(Key)]
    ///   - [#description(Component)]
    @ApiStatus.NonExtendable
    interface Builder extends TrimMaterialRegistryEntry, RegistryBuilder<TrimMaterial> {

        /// Sets the palette texture id to be used for this trim material.
        ///
        /// @param paletteId the palette texture id
        /// @return this builder instance
        /// @see #paletteId()
        @Contract(value = "_ -> this", mutates = "this")
        Builder paletteId(Key paletteId);

        /// Sets the description for the trim material.
        ///
        /// @param description the description
        /// @return this builder instance
        /// @see #description()
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);
    }
}
