package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Chicken;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Chicken.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ChickenVariantRegistryEntry {

    /**
     * The model of the chicken variant to render the configured texture on.
     */
    enum Model {
        /**
         * The normal chicken model.
         */
        NORMAL,

        /**
         * The cold chicken model.
         */
        COLD,
    }

    /**
     * Provides the asset of the chicken variant, which represents the texture to use.
     *
     * @return the client asset.
     */
    ClientAsset clientAsset();

    /**
     * Provides the model of the chicken variant.
     *
     * @return the model.
     */
    Model model();

    /**
     * A mutable builder for the {@link ChickenVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clientAsset(ClientAsset)}</li>
     *     <li>{@link #model(Model)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ChickenVariantRegistryEntry, RegistryBuilder<Chicken.Variant> {

        /**
         * Sets the client asset of the chicken variant, which is the location of the texture to use.
         *
         * @param clientAsset the client asset.
         * @return this builder instance.
         * @see ChickenVariantRegistryEntry#clientAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientAsset(ClientAsset clientAsset);

        /**
         * Sets the model to use for this chicken variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see ChickenVariantRegistryEntry#model()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Model model);
    }
}
