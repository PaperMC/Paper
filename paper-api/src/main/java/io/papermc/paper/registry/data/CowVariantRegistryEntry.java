package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Cow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Cow.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CowVariantRegistryEntry {

    /**
     * The model of the cow variant to render the configured texture on.
     */
    enum Model {
        /**
         * The normal cow model.
         */
        NORMAL,

        /**
         * The cold cow model.
         */
        COLD,

        /**
         * The warm cow model.
         */
        WARM,
    }

    /**
     * Provides the asset of the cow variant, which represents the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset clientAsset();

    /**
     * Provides the model of the cow variant.
     *
     * @return the model.
     */
    Model model();

    /**
     * A mutable builder for the {@link CowVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clientAsset(ClientAsset)}</li>
     *     <li>{@link #model(Model)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends CowVariantRegistryEntry, RegistryBuilder<Cow.Variant> {

        /**
         * Sets the client asset of the cow variant, which is the location of the texture to use.
         *
         * @param clientAsset the client asset.
         * @return this builder instance.
         * @see CowVariantRegistryEntry#clientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientAsset(ClientAsset clientAsset);

        /**
         * Sets the model to use for this cow variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see CowVariantRegistryEntry#model()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Model model);
    }
}
