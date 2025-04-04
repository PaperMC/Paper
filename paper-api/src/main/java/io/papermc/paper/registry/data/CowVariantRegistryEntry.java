package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Cow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Cow.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CowVariantRegistryEntry {

    /**
     * Provides the asset id of the cow variant, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Cow.Variant#assetId()
     */
    Key assetId();

    /**
     * Provides the model of the cow variant.
     * @return the model.
     * @see Cow.Variant#getModel()
     */
    Cow.Variant.@Nullable Model model();

    /**
     * A mutable builder for the {@link CowVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends CowVariantRegistryEntry, RegistryBuilder<Cow.Variant> {

        /**
         * Sets the model to use for this cow variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see CowVariantRegistryEntry#model()
         * @see Cow.Variant#getModel()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Cow.Variant.Model model);

        /**
         * Sets the asset id of the cow variant, which is the location of the texture to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see CowVariantRegistryEntry#assetId()
         * @see Cow.Variant#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
