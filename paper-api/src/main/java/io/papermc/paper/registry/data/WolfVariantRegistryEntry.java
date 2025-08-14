package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import java.util.List;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link Wolf.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WolfVariantRegistryEntry {

    /**
     * Provides the client texture asset of the wolf variant for when it is angry, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset angryClientTextureAsset();

    /**
     * Provides the client texture asset of the wolf variant for when it is wild, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset wildClientTextureAsset();

    /**
     * Provides the client texture asset of the wolf variant for when it is tame, which is the location of the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset tameClientTextureAsset();

    /**
     * Provides the spawn conditions of the wolf variant, which is a list of {@link SpawnConditionPriority} that
     * determine the priority of the spawn conditions for this variant.
     *
     * @return the list of spawn condition priorities.
     */
    @Unmodifiable List<SpawnConditionPriority> spawnConditions();

    /**
     * A mutable builder for the {@link WolfVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #angryClientTextureAsset(ClientTextureAsset)}</li>
     *     <li>{@link #wildClientTextureAsset(ClientTextureAsset)}</li>
     *     <li>{@link #tameClientTextureAsset(ClientTextureAsset)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends WolfVariantRegistryEntry, RegistryBuilder<Wolf.Variant> {

        /**
         * Sets the client texture asset of the wolf variant for when it is angry, which is the location of the texture to use.
         *
         * @param angryClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#angryClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder angryClientTextureAsset(ClientTextureAsset angryClientTextureAsset);

        /**
         * Sets the client texture asset of the wolf variant for when it is wild, which is the location of the texture to use.
         *
         * @param wildClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#wildClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder wildClientTextureAsset(ClientTextureAsset wildClientTextureAsset);

        /**
         * Sets the client texture asset of the wolf variant for when it is tame, which is the location of the texture to use.
         *
         * @param tameClientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#tameClientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder tameClientTextureAsset(ClientTextureAsset tameClientTextureAsset);

        /**
         * Sets the spawn conditions of the wolf variant, which is a list of {@link SpawnConditionPriority} that
         * determine the priority of the spawn conditions for this variant.
         *
         * @param spawnConditions the list of spawn condition priorities.
         * @return this builder instance.
         * @see WolfVariantRegistryEntry#spawnConditions()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spawnConditions(List<SpawnConditionPriority> spawnConditions);
    }
}
