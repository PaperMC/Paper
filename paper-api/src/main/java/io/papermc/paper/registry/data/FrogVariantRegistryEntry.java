package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import org.bukkit.entity.Frog;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/// A data-centric version-specific registry entry for the [Frog.Variant] type.
@ApiStatus.NonExtendable
public interface FrogVariantRegistryEntry {

    /// Provides the client texture asset of the frog variant, which represents the texture to use.
    ///
    /// @return the client texture asset
    ClientTextureAsset clientTextureAsset();

    /// A mutable builder for the [FrogVariantRegistryEntry] plugins may change in applicable registry events.
    ///
    /// The following values are required for each builder:
    ///
    ///   - [#clientTextureAsset(ClientTextureAsset)]
    @ApiStatus.NonExtendable
    interface Builder extends FrogVariantRegistryEntry, RegistryBuilder<Frog.Variant> {

        /// Sets the client texture asset of the frog variant, which is the location of the texture to use.
        ///
        /// @param clientTextureAsset the client texture asset
        /// @return this builder instance
        /// @see FrogVariantRegistryEntry#clientTextureAsset()
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientTextureAsset(ClientTextureAsset clientTextureAsset);
    }
}
