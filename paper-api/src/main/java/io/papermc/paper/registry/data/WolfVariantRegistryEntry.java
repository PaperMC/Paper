package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Wolf.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WolfVariantRegistryEntry {

    /**
     * Provides the asset id of the wolf variant for when it is angry, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Wolf.Variant#assetIdAngry()
     */
    Key assetIdAngry();

    /**
     * Provides the asset id of the wolf variant for when it is wild, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Wolf.Variant#assetIdWild()
     */
    Key assetIdWild();

    /**
     * Provides the asset id of the wolf variant for when it is tame, which is the location of the texture to use.
     *
     * @return the asset id.
     * @see Wolf.Variant#assetIdTame()
     */
    Key assetIdTame();

    /**
     * A mutable builder for the {@link WolfVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #assetIdAngry(Key)}</li>
     *     <li>{@link #assetIdWild(Key)}</li>
     *     <li>{@link #assetIdTame(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends WolfVariantRegistryEntry, RegistryBuilder<Wolf.Variant> {

        /**
         * Sets the asset id of the wolf variant for when it is angry, which is the location of the texture to use.
         *
         * @param assetIdAngry the asset id.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#assetIdAngry()
         * @see Wolf.Variant#assetIdAngry()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetIdAngry(Key assetIdAngry);

        /**
         * Sets the asset id of the wolf variant for when it is wild, which is the location of the texture to use.
         *
         * @param assetIdWild the asset id.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#assetIdWild()
         * @see Wolf.Variant#assetIdWild()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetIdWild(Key assetIdWild);

        /**
         * Sets the asset id of the wolf variant for when it is tame, which is the location of the texture to use.
         *
         * @param assetIdTame the asset id.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#assetIdTame()
         * @see Wolf.Variant#assetIdTame()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetIdTame(Key assetIdTame);
    }
}
