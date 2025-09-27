package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable; // Paper

/**
 * Represents an Experience Orb.
 */
public interface ExperienceOrb extends Entity {

    /**
     * Gets how much experience is contained within this orb
     *
     * @return Amount of experience
     */
    public int getExperience();

    /**
     * Sets how much experience is contained within this orb
     *
     * @param value Amount of experience
     */
    public void setExperience(int value);

    // Paper start - expose count
    /**
     * Get the stacked count for this experience orb.
     *
     * @return the count
     */
    int getCount();

    /**
     * Sets the stacked count for this experience orb.
     *
     * @param count the new count
     */
    void setCount(int count);
    // Paper end

    // Paper start
    /**
     * Check if this orb was spawned from a {@link ThrownExpBottle}
     *
     * @return if orb was spawned from a bottle
     * @deprecated Use getSpawnReason() == EXP_BOTTLE
     */
    @Deprecated
    default boolean isFromBottle() {
        return getSpawnReason() == SpawnReason.EXP_BOTTLE;
    }

    /**
     * Reasons for why this Experience Orb was spawned
     */
    enum SpawnReason {
        /**
         * Spawned by a player dying
         */
        PLAYER_DEATH,
        /**
         * Spawned by an entity dying after being damaged by a player
         */
        ENTITY_DEATH,
        /**
         * Spawned by player using a furnace
         */
        FURNACE,
        /**
         * Spawned by player breeding animals
         */
        BREED,
        /**
         * Spawned by player trading with a villager
         */
        VILLAGER_TRADE,
        /**
         * Spawned by player fishing
         */
        FISHING,
        /**
         * Spawned by player breaking a block that gives experience points such as Diamond Ore
         */
        BLOCK_BREAK,
        /**
         * Spawned by Bukkit API
         */
        CUSTOM,
        /**
         * Spawned by a player throwing an experience points bottle
         */
        EXP_BOTTLE,
        /**
         * Spawned by a player using a grindstone
         */
        GRINDSTONE,
        /**
         * We do not know why it was spawned
         */
        UNKNOWN
    }

    /**
     * If this experience orb was triggered to be spawned by
     * an entity such as a player, due to events such as killing entity,
     * breaking blocks, smelting in a furnace, etc, this will return the UUID
     * of the entity that triggered this orb to drop.
     *
     * In the case of an entity being killed, this will be the killers UUID.
     *
     * @return UUID of the player that triggered this orb to drop, or null if unknown/no triggering entity
     */
    @Nullable java.util.UUID getTriggerEntityId();

    /**
     * If this experience orb was spawned in relation to another
     * entity, such as a player or other living entity death, or breeding,
     * return the source entity UUID.
     *
     * In the case of breeding, this will be the new baby entities UUID.
     * In the case of an entity being killed, this will be the dead entities UUID.
     *
     * @return The UUID of the entity that sourced this experience orb
     */
    @Nullable java.util.UUID getSourceEntityId();

    /**
     * Gets the reason that this experience orb was spawned. For any case that we
     * do not know, such as orbs spawned before this API was added, UNKNOWN is returned.
     * @return The reason for this orb being spawned.
     */
    @NotNull
    SpawnReason getSpawnReason();
    // Paper end
}
