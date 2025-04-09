package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Frog;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Frog.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface FrogVariantRegistryEntry {

    /**
     * Provides the asset id of the frog variant, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Frog.Variant#assetId()
     */
    Key assetId();

    /**
     * A mutable builder for the {@link FrogVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends FrogVariantRegistryEntry, RegistryBuilder<Frog.Variant> {

        /**
         * Sets the asset id of the frog variant, which is the location of the texture to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see FrogVariantRegistryEntry#assetId()
         * @see Frog.Variant#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
