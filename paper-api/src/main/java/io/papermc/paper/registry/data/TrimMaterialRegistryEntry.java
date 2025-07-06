package io.papermc.paper.registry.data;

import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.registry.RegistryBuilder;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.inventory.meta.trim.TrimMaterial} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface TrimMaterialRegistryEntry {

    /**
     * The base asset path for the trim material.
     *
     * @return the base asset path
     */
    @Contract(pure = true)
    @KeyPattern.Value String baseAssetPath();

    /**
     * An immutable map of asset path overrides for the trim material.
     * <p>
     * The key is the identifier of the asset, and the value is the path to the asset.
     *
     * @return the asset path overrides
     * @see Equippable#assetId()
     */
    @Contract(pure = true)
    @Unmodifiable Map<Key, String> assetPathOverrides();

    /**
     * The description of the trim material.
     *
     * @return the description
     */
    @Contract(pure = true)
    Component description();

    /**
     * A mutable builder for {@link TrimMaterialRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #baseAssetPath(String)}</li>
     *     <li>{@link #description(Component)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends TrimMaterialRegistryEntry, RegistryBuilder<TrimMaterial> {

        /**
         * Sets the base asset path for the trim material.
         *
         * @param baseAssetPath the base asset path
         * @return the builder
         * @see #baseAssetPath()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder baseAssetPath(@KeyPattern.Value String baseAssetPath);

        /**
         * Sets the asset path overrides for the trim material.
         *
         * @param assetPathOverrides the asset path overrides
         * @return the builder
         * @see #assetPathOverrides()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetPathOverrides(Map<Key, String> assetPathOverrides);

        /**
         * Sets the description for the trim material.
         *
         * @param description the description
         * @return the builder
         * @see #description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);
    }
}
