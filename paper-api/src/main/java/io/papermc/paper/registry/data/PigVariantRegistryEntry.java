package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Pig.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PigVariantRegistryEntry {

    /**
     * The model of the pig variant to render the configured texture on.
     */
    enum Model {
        /**
         * The normal pig model.
         */
        NORMAL,

        /**
         * The cold pig model.
         */
        COLD,
    }

    /**
     * Provides the asset of the wolf variant, which represents the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset clientAsset();

    /**
     * Provides the model of the pig variant.
     *
     * @return the model.
     */
    Model model();

    /**
     * A mutable builder for the {@link PigVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clientAsset(ClientAsset)}</li>
     *     <li>{@link #model(Model)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends PigVariantRegistryEntry, RegistryBuilder<Pig.Variant> {

        /**
         * Sets the client asset of the pig variant, which is the location of the texture to use.
         *
         * @param clientAsset the client asset.
         * @return this builder instance.
         * @see CatTypeRegistryEntry#clientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientAsset(ClientAsset clientAsset);

        /**
         * Sets the model to use for this pig variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see PigVariantRegistryEntry#model()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Model model);
    }
}
