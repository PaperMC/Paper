package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
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
     * Provides the asset id of the pig variant, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Pig.Variant#assetId()
     */
    Key assetId();

    /**
     * Provides the model of the pig variant.
     * @return the model.
     * @see Pig.Variant#getModel()
     */
    Pig.Variant.@Nullable Model model();

    /**
     * A mutable builder for the {@link PigVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends PigVariantRegistryEntry, RegistryBuilder<Pig.Variant> {

        /**
         * Sets the model to use for this pig variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see PigVariantRegistryEntry#model()
         * @see Pig.Variant#getModel()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Pig.Variant.Model model);

        /**
         * Sets the asset id of the pig variant, which is the location of the texture to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see PigVariantRegistryEntry#assetId()
         * @see Pig.Variant#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
