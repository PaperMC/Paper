package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a creature is spawned into a world.
 * <p />
 * If a Creature Spawn event is cancelled, the creature will not spawn.
 */
public class CreatureSpawnEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final SpawnReason spawnReason;

    public CreatureSpawnEvent(final LivingEntity spawnee, final SpawnReason spawnReason) {
        super(spawnee);
        this.spawnReason = spawnReason;
    }

    @Deprecated
    public CreatureSpawnEvent(Entity spawnee, CreatureType type, Location loc, SpawnReason reason) {
        super(spawnee);
        spawnReason = reason;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the location at which the creature is spawning.
     *
     * @return The location at which the creature is spawning
     */
    public Location getLocation() {
        return getEntity().getLocation();
    }

    /**
     * Gets the type of creature being spawned.
     *
     * @return A CreatureType value detailing the type of creature being spawned
     * @deprecated In favour of {@link #getEntityType()}.
     */
    @Deprecated
    public CreatureType getCreatureType() {
        return CreatureType.fromEntityType(getEntityType());
    }

    /**
     * Gets the reason for why the creature is being spawned.
     *
     * @return A SpawnReason value detailing the reason for the creature being spawned
     */
    public SpawnReason getSpawnReason() {
        return spawnReason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * An enum to specify the type of spawning
     */
    public enum SpawnReason {

        /**
         * When something spawns from natural means
         */
        NATURAL,
        /**
         * When an entity spawns as a jockey of another entity (mostly spider jockeys)
         */
        JOCKEY,
        /**
         * When a creature spawns due to chunk generation
         */
        CHUNK_GEN,
        /**
         * When a creature spawns from a spawner
         */
        SPAWNER,
        /**
         * When a creature spawns from an egg
         */
        EGG,
        /**
         * When a creature spawns from a Spawner Egg
         */
        SPAWNER_EGG,
        /**
         * When a creature spawns because of a lightning strike
         */
        LIGHTNING,
        /**
         * When a creature is spawned by a player that is sleeping
         *
         * @deprecated No longer used
         */
        @Deprecated
        BED,
        /**
         * When a snowman is spawned by being built
         */
        BUILD_SNOWMAN,
        /**
         * When an iron golem is spawned by being built
         */
        BUILD_IRONGOLEM,
        /**
         * When an iron golem is spawned to defend a village
         */
        VILLAGE_DEFENSE,
        /**
         * When a zombie is spawned to invade a village
         */
        VILLAGE_INVASION,
        /**
         * When an animal breeds to create a child
         */
        BREEDING,
        /**
         * When a slime splits
         */
        SLIME_SPLIT,
        /**
         * When a creature is spawned by plugins
         */
        CUSTOM,
        /**
         * When an entity is missing a SpawnReason
         */
        DEFAULT
    }
}
