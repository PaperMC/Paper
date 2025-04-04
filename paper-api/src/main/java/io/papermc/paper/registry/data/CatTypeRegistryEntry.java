package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Cat.Type} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CatTypeRegistryEntry {

    /**
     * Provides the asset id of the cat type, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Cat.Type#assetId()
     */
    Key assetId();

    /**
     * A mutable builder for the {@link CatTypeRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends CatTypeRegistryEntry, RegistryBuilder<Cat.Type> {

        /**
         * Sets the asset id of the cat type, which is the location of the texture to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see CatTypeRegistryEntry#assetId()
         * @see Cat.Type#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
