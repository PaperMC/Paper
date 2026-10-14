package org.bukkit.event.entity;

import org.bukkit.Chunk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a creature is spawned into a world.
 * <p>
 * If this event is cancelled, the creature will not spawn.
 */
public class CreatureSpawnEvent extends EntitySpawnEvent {

    private final SpawnReason spawnReason;

    @ApiStatus.Internal
    public CreatureSpawnEvent(@NotNull final LivingEntity spawnee, @NotNull final SpawnReason spawnReason) {
        super(spawnee);
        this.spawnReason = spawnReason;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Gets the reason for why the creature is being spawned.
     *
     * @return A SpawnReason value detailing the reason for the creature being
     *     spawned
     */
    @NotNull
    public SpawnReason getSpawnReason() {
        return this.spawnReason;
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
         *
         * @deprecated no longer called, chunks are generated with entities
         * already existing. Consider using {@link ChunkLoadEvent},
         * {@link ChunkLoadEvent#isNewChunk()} and {@link Chunk#getEntities()}
         * for similar effect.
         */
        @Deprecated(since = "1.14", forRemoval = true)
        CHUNK_GEN,
        /**
         * When a creature spawns from a spawner
         */
        SPAWNER,
        /**
         * When a creature spawns from a trial spawner
         */
        TRIAL_SPAWNER,
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
         * When a copper golem is spawned by being built
         */
        BUILD_COPPERGOLEM,
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
         * When an entity breeds to create a child, this also include Shulker and Allay
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
         * When a creature is spawned by another entity drowning
         */
        DROWNED,
        /**
         * When a cow is spawned by shearing a mushroom cow
         */
        SHEARED,
        /**
         * When an entity is spawned as a result of an explosion. Like an area effect cloud from
         * a creeper or a dragon fireball.
         */
        EXPLOSION,
        /**
         * When an entity is spawned as part of a raid
         */
        RAID,
        /**
         * When an entity is spawned as part of a patrol
         */
        PATROL,
        /**
         * When a bee is released from a beehive/bee nest
         */
        BEEHIVE,
        /**
         * When a piglin is converted to a zombified piglin.
         */
        PIGLIN_ZOMBIFIED,
        /**
         * When an entity is created by a cast spell.
         */
        SPELL,
        /**
         * When an entity is shaking in Powder Snow and a new entity spawns.
         */
        FROZEN,
        /**
         * When a tadpole converts to a frog
         */
        METAMORPHOSIS,
        /**
         * When an Allay duplicate itself
         */
        DUPLICATION,
        /**
         * When a creature is spawned by the "/summon" command
         */
        COMMAND,
        /**
         * When a creature is spawned by an enchantment
         */
        ENCHANTMENT,
        /**
         * When an entity spawns from an ominous item spawner
         */
        OMINOUS_ITEM_SPAWNER,
        /**
         * When an entity spawns from a bucket
         */
        BUCKET,
        /**
         * When a creature is spawned by a potion effect, for example:
         * {@link org.bukkit.potion.PotionType#OOZING}, {@link org.bukkit.potion.PotionType#INFESTED}
         */
        POTION_EFFECT,
        /**
         * When a Copper Golem Statue turns back into a Copper Golem
         *
         * @apiNote Canceling a {@link CreatureSpawnEvent} with this reason does not prevent the statue block from being removed, use {@link EntityChangeBlockEvent} to account for all side effects
         */
        REANIMATE,
        /**
         * When a creature is spawned by being rehydrated
         */
        REHYDRATION,
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
