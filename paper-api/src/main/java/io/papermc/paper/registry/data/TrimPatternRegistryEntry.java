package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.inventory.meta.trim.TrimPattern} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface TrimPatternRegistryEntry {

    /**
     * The asset ID of the trim pattern.
     *
     * @return the asset ID
     */
    @Contract(pure = true)
    Key assetId();

    /**
     * The description of the trim pattern.
     *
     * @return the description
     */
    @Contract(pure = true)
    Component description();

    /**
     * Checks if the trim pattern is a decal.
     *
     * @return true if decal, false otherwise
     */
    @Contract(pure = true)
    boolean decal();

    /**
     * A mutable builder for {@link TrimPatternRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     *     <li>{@link #description(Component)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends TrimPatternRegistryEntry, RegistryBuilder<TrimPattern> {

        /**
         * Sets the asset ID for the trim pattern.
         *
         * @param key the asset ID
         * @return the builder
         * @see #assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key key);

        /**
         * Sets the description for the trim pattern.
         *
         * @param description the description
         * @return the builder
         * @see #description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /**
         * Sets whether the trim pattern is a decal.
         *
         * @param decal true if decal, false otherwise
         * @return the builder
         * @see #decal()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder decal(boolean decal);
    }
}
