package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Wolf.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WolfVariantRegistryEntry {

    /**
     * Provides the client texture asset of the wolf variant for when it is angry, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset angryClientTextureAsset();

    /**
     * Provides the client texture asset of the wolf variant for when it is wild, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset wildClientTextureAsset();

    /**
     * Provides the client texture asset of the wolf variant for when it is tame, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset tameClientTextureAsset();

    /**
     * A mutable builder for the {@link WolfVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #angryClientTextureAsset(ClientTextureAsset)}</li>
     *     <li>{@link #wildClientTextureAsset(ClientTextureAsset)}</li>
     *     <li>{@link #tameClientTextureAsset(ClientTextureAsset)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends WolfVariantRegistryEntry, RegistryBuilder<Wolf.Variant> {

        /**
         * Sets the client texture asset of the wolf variant for when it is angry, which is the location of the texture to use.
         *
         * @param angryClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#angryClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder angryClientTextureAsset(ClientTextureAsset angryClientTextureAsset);

        /**
         * Sets the client texture asset of the wolf variant for when it is wild, which is the location of the texture to use.
         *
         * @param wildClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#wildClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder wildClientTextureAsset(ClientTextureAsset wildClientTextureAsset);

        /**
         * Sets the client texture asset of the wolf variant for when it is tame, which is the location of the texture to use.
         *
         * @param tameClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#tameClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder tameClientTextureAsset(ClientTextureAsset tameClientTextureAsset);
    }
}
