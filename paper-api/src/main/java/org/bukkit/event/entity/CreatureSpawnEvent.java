package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a creature is spawned into a world.
 * <p>
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
     * Gets the reason for why the creature is being spawned.
     *
     * @return A SpawnReason value detailing the reason for the creature being
     *     spawned
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
         * When an entity spawns as a jockey of another entity (mostly spider
         * jockeys)
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
         * When a snowman is spawned by being built
         */
        BUILD_SNOWMAN,
        /**
         * When an iron golem is spawned by being built
         */
        BUILD_IRONGOLEM,
        /**
         * When a wither boss is spawned by being built
         */
        BUILD_WITHER,
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
         * When an entity calls for reinforcements
         */
        REINFORCEMENTS,
        /**
         * When a creature is spawned by nether portal
         */
        NETHER_PORTAL,
        /**
         * When a creature is spawned by a dispenser dispensing an egg
         */
        DISPENSE_EGG,
        /**
         * When a zombie infects a villager
         */
        INFECTION,
        /**
         * When a villager is cured from infection
         */
        CURED,
        /**
         * When an ocelot has a baby spawned along with them
         */
        OCELOT_BABY,
        /**
         * When a silverfish spawns from a block
         */
        SILVERFISH_BLOCK,
        /**
         * When an entity spawns as a mount of another entity (mostly chicken
         * jockeys)
         */
        MOUNT,
        /**
         * When an entity spawns as a trap for players approaching
         */
        TRAP,
        /**
         * When an entity is spawned as a result of ender pearl usage
         */
        ENDER_PEARL,
        /**
         * When an entity is spawned as a result of the entity it is being
         * perched on jumping or being damaged
         */
        SHOULDER_ENTITY,
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
