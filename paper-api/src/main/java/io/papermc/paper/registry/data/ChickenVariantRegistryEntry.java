package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
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
     * Provides the asset id of the chicken variant, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Chicken.Variant#assetId()
     */
    Key assetId();

    /**
     * Provides the model of the chicken variant.
     * @return the model.
     * @see Chicken.Variant#getModel()
     */
    Chicken.Variant.@Nullable Model model();

    /**
     * A mutable builder for the {@link ChickenVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ChickenVariantRegistryEntry, RegistryBuilder<Chicken.Variant> {

        /**
         * Sets the model to use for this chicken variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see ChickenVariantRegistryEntry#model()
         * @see Chicken.Variant#getModel()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Chicken.Variant.Model model);

        /**
         * Sets the asset id of the chicken variant, which is the location of the texture to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see ChickenVariantRegistryEntry#assetId()
         * @see Chicken.Variant#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
