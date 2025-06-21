package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import java.util.List;
import org.bukkit.entity.Frog;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link Frog.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface FrogVariantRegistryEntry {

    /**
     * Provides the client texture asset of the frog variant, which represents the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset clientTextureAsset();

    /**
     * Provides the spawn conditions of the frog variant, which is a list of {@link SpawnConditionPriority} that
     * determine the priority of the spawn conditions for this variant.
     *
     * @return the list of spawn condition priorities.
     */
    @Unmodifiable List<SpawnConditionPriority> spawnConditions();

    /**
     * A mutable builder for the {@link FrogVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clientTextureAsset(ClientTextureAsset)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends FrogVariantRegistryEntry, RegistryBuilder<Frog.Variant> {

        /**
         * Sets the client texture asset of the frog variant, which is the location of the texture to use.
         *
         * @param clientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see FrogVariantRegistryEntry#clientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientTextureAsset(ClientTextureAsset clientTextureAsset);

        /**
         * Sets the spawn conditions of the frog variant, which is a list of {@link SpawnConditionPriority} that
         * determine the priority of the spawn conditions for this variant.
         *
         * @param spawnConditions the list of spawn condition priorities.
         * @return this builder instance.
         * @see FrogVariantRegistryEntry#spawnConditions()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spawnConditions(List<SpawnConditionPriority> spawnConditions);
    }
}
