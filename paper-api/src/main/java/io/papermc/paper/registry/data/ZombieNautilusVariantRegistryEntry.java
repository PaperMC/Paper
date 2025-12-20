package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import java.util.List;
import org.bukkit.entity.ZombieNautilus;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.entity.ZombieNautilus.Variant} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ZombieNautilusVariantRegistryEntry {

    /**
     * The model of the variant to render the configured texture on.
     */
    enum Model {
        /**
         * The normal model.
         */
        NORMAL,

        /**
         * The warm model.
         */
        WARM,
    }

    /**
     * Provides the client texture asset of the variant, which represents the texture to use.
     *
     * @return the client texture asset.
     */
    ClientTextureAsset clientTextureAsset();

    /**
     * Provides the model of the variant.
     *
     * @return the model.
     */
    Model model();

    /**
     * Provides the spawn conditions of the variant, which is a list of {@link SpawnConditionPriority} that
     * determine the priority of the spawn conditions for this variant.
     *
     * @return the list of spawn condition priorities.
     */
    @Unmodifiable List<SpawnConditionPriority> spawnConditions();

    /**
     * A mutable builder for the {@link ZombieNautilusVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #clientTextureAsset(ClientTextureAsset)}</li>
     *     <li>{@link #model(Model)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ZombieNautilusVariantRegistryEntry, RegistryBuilder<ZombieNautilus.Variant> {

        /**
         * Sets the client texture asset of the variant, which is the location of the texture to use.
         *
         * @param clientTextureAsset the client texture asset.
         * @return this builder instance.
         * @see ZombieNautilusVariantRegistryEntry#clientTextureAsset()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder clientTextureAsset(ClientTextureAsset clientTextureAsset);

        /**
         * Sets the model to use for this variant.
         *
         * @param model the model.
         * @return this builder instance.
         * @see ZombieNautilusVariantRegistryEntry#model()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder model(Model model);

        /**
         * Sets the spawn conditions of the variant, which is a list of {@link SpawnConditionPriority} that
         * determine the priority of the spawn conditions for this variant.
         *
         * @param spawnConditions the list of spawn condition priorities.
         * @return this builder instance.
         * @see ZombieNautilusVariantRegistryEntry#spawnConditions()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spawnConditions(List<SpawnConditionPriority> spawnConditions);
    }
}
