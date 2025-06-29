package io.papermc.paper.loot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

/**
 * Builder for creating safe and comprehensive loot contexts.
 * This builder provides a fluent API for constructing loot contexts
 * that can be used with {@link LootGenerator}.
 * <p>
 * This API replaces the broken {@link org.bukkit.loot.LootContext.Builder}
 * and provides better support for all Minecraft loot context parameters.
 * 
 * @since 1.21.4
 */
public interface LootContextBuilder {

    /**
     * Create a new loot context builder.
     * 
     * @param location the location where loot generation occurs
     * @return a new builder instance
     */
    static LootContextBuilder create(Location location) {
        return LootContextBuilderImpl.create(location);
    }

    /**
     * Sets the location where loot generation occurs.
     * This is required for all loot contexts.
     * 
     * @param location the location
     * @return this builder
     */
    LootContextBuilder location(Location location);

    /**
     * Sets the luck modifier for loot generation.
     * Higher values increase the chance of better loot.
     * 
     * @param luck the luck value
     * @return this builder
     */
    LootContextBuilder luck(float luck);

    /**
     * Sets the entity that was killed or interacted with.
     * This affects entity-specific loot tables and conditions.
     * 
     * @param entity the target entity
     * @return this builder
     */
    LootContextBuilder lootedEntity(Entity entity);

    /**
     * Sets the entity that killed or interacted with the target.
     * This affects conditions like "killed_by_player".
     * 
     * @param killer the killer entity
     * @return this builder
     */
    LootContextBuilder killer(HumanEntity killer);

    /**
     * Builds the loot context from the configured parameters.
     * 
     * @return the built loot context
     * @throws IllegalStateException if required parameters are missing
     */
    LootContext build();
}
