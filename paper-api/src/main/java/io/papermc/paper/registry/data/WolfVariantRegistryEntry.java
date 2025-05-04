package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
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
     * Provides the client asset of the wolf variant for when it is angry, which is the location of the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset angryClientAsset();

    /**
     * Provides the client asset of the wolf variant for when it is wild, which is the location of the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset wildClientAsset();

    /**
     * Provides the client asset of the wolf variant for when it is tame, which is the location of the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset tameClientAsset();

    /**
     * A mutable builder for the {@link WolfVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #angryClientAsset(ClientAsset)}</li>
     *     <li>{@link #wildClientAsset(ClientAsset)}</li>
     *     <li>{@link #tameClientAsset(ClientAsset)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends WolfVariantRegistryEntry, RegistryBuilder<Wolf.Variant> {

        /**
         * Sets the client asset of the wolf variant for when it is angry, which is the location of the texture to use.
         *
         * @param angryClientAsset the client asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#angryClientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder angryClientAsset(ClientAsset angryClientAsset);

        /**
         * Sets the client asset of the wolf variant for when it is wild, which is the location of the texture to use.
         *
         * @param wildClientAsset the client asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#wildClientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder wildClientAsset(ClientAsset wildClientAsset);

        /**
         * Sets the client asset of the wolf variant for when it is tame, which is the location of the texture to use.
         *
         * @param tameClientAsset the client asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#tameClientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder tameClientAsset(ClientAsset tameClientAsset);
    }
}
