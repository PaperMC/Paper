package io.papermc.paper.registry.data;

import io.papermc.paper.block.pot.PotPatternType;
import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link PotPatternType} type.
 */
@ApiStatus.NonExtendable
public interface PotPatternRegistryEntry {

    /**
     * Provides the asset id of the pattern type, which is the location of the sprite to use.
     *
     * @return the asset id
     */
    Key assetId();

    /**
     * A mutable builder for the {@link PotPatternRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends PotPatternRegistryEntry, RegistryBuilder<PotPatternType> {

        /**
         * Sets the asset id of the pattern type, which is the location of the sprite to use.
         *
         * @param assetId the asset id
         * @return this builder instance
         * @see PotPatternRegistryEntry#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
