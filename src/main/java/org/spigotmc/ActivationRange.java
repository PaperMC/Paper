package org.spigotmc;

import java.util.Collection;
import java.util.List;

import net.minecraft.server.Activity;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BehaviorController;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkProviderServer; // Paper
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAmbient;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityBee;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityEnderSignal;
import net.minecraft.server.EntityFallingBlock; // Paper
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityFlying;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPillager;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EntityRaider;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntitySlice;
import net.minecraft.server.EntitySlime;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EntityThrownTrident;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityWither;
import net.minecraft.server.IMonster;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MemoryModuleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;
import co.aikar.timings.MinecraftTimings;
// Paper start
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLlama;
import net.minecraft.server.EntityWaterAnimal;
// Paper end

public class ActivationRange
{

    public enum ActivationType
    {
        WATER, // Paper
        FLYING_MONSTER, // Paper
        VILLAGER, // Paper
        MONSTER,
        ANIMAL,
        RAIDER,
        MISC;

        AxisAlignedBB boundingBox = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );
    }
    // Paper start

    static Activity[] VILLAGER_PANIC_IMMUNITIES = {
        Activity.HIDE,
        Activity.PRE_RAID,
        Activity.RAID,
        Activity.PANIC
    };

    private static int checkInactiveWakeup(Entity entity) {
        World world = entity.world;
        SpigotWorldConfig config = world.spigotConfig;
        long inactiveFor = MinecraftServer.currentTick - entity.activatedTick;
        if (entity.activationType == ActivationType.VILLAGER) {
            if (inactiveFor > config.wakeUpInactiveVillagersEvery && world.wakeupInactiveRemainingVillagers > 0) {
                world.wakeupInactiveRemainingVillagers--;
                return config.wakeUpInactiveVillagersFor;
            }
        } else if (entity.activationType == ActivationType.ANIMAL) {
            if (inactiveFor > config.wakeUpInactiveAnimalsEvery && world.wakeupInactiveRemainingAnimals > 0) {
                world.wakeupInactiveRemainingAnimals--;
                return config.wakeUpInactiveAnimalsFor;
            }
        } else if (entity.activationType == ActivationType.FLYING_MONSTER) {
            if (inactiveFor > config.wakeUpInactiveFlyingEvery && world.wakeupInactiveRemainingFlying > 0) {
                world.wakeupInactiveRemainingFlying--;
                return config.wakeUpInactiveFlyingFor;
            }
        } else if (entity.activationType == ActivationType.MONSTER || entity.activationType == ActivationType.RAIDER) {
            if (inactiveFor > config.wakeUpInactiveMonstersEvery && world.wakeupInactiveRemainingMonsters > 0) {
                world.wakeupInactiveRemainingMonsters--;
                return config.wakeUpInactiveMonstersFor;
            }
        }
        return -1;
    }
    // Paper end

    static AxisAlignedBB maxBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     *
     * @param entity
     * @return group id
     */
    public static ActivationType initializeEntityActivationType(Entity entity)
    {
        if (entity instanceof EntityWaterAnimal) { return ActivationType.WATER; } // Paper
        else if (entity instanceof EntityVillager) { return ActivationType.VILLAGER; } // Paper
        else if (entity instanceof EntityFlying && entity instanceof IMonster) { return ActivationType.FLYING_MONSTER; } // Paper - doing & Monster incase Flying no longer includes monster in future
        if ( entity instanceof EntityRaider )
        {
            return ActivationType.RAIDER;
        } else if ( entity instanceof IMonster ) // Paper - correct monster check
        {
            return ActivationType.MONSTER;
        } else if ( entity instanceof EntityCreature || entity instanceof EntityAmbient )
        {
            return ActivationType.ANIMAL;
        } else
        {
            return ActivationType.MISC;
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     *
     * @param entity Entity to initialize
     * @param config Spigot config to determine ranges
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config)
    {
        if ( ( entity.activationType == ActivationType.MISC && config.miscActivationRange <= 0 )
                || ( entity.activationType == ActivationType.RAIDER && config.raiderActivationRange <= 0 )
                || ( entity.activationType == ActivationType.ANIMAL && config.animalActivationRange <= 0 )
                || ( entity.activationType == ActivationType.MONSTER && config.monsterActivationRange <= 0 )
                || ( entity.activationType == ActivationType.VILLAGER && config.villagerActivationRange <= 0 ) // Paper
                || ( entity.activationType == ActivationType.WATER && config.waterActivationRange <= 0 ) // Paper
                || ( entity.activationType == ActivationType.FLYING_MONSTER && config.flyingMonsterActivationRange <= 0 ) // Paper
                || entity instanceof EntityEnderSignal // Paper
                || entity instanceof EntityHuman
                || entity instanceof EntityProjectile
                || entity instanceof EntityEnderDragon
                || entity instanceof EntityComplexPart
                || entity instanceof EntityWither
                || entity instanceof EntityFireball
                || entity instanceof EntityLightning
                || entity instanceof EntityTNTPrimed
                || entity instanceof EntityFallingBlock // Paper - Always tick falling blocks
                || entity instanceof EntityEnderCrystal
                || entity instanceof EntityFireworks
                || entity instanceof EntityThrownTrident )
        {
            return true;
        }

        return false;
    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     *
     * @param world
     */
    public static void activateEntities(World world)
    {
        MinecraftTimings.entityActivationCheckTimer.startTiming();
        final int miscActivationRange = world.spigotConfig.miscActivationRange;
        final int raiderActivationRange = world.spigotConfig.raiderActivationRange;
        final int animalActivationRange = world.spigotConfig.animalActivationRange;
        final int monsterActivationRange = world.spigotConfig.monsterActivationRange;
        // Paper start
        final int waterActivationRange = world.spigotConfig.waterActivationRange;
        final int flyingActivationRange = world.spigotConfig.flyingMonsterActivationRange;
        final int villagerActivationRange = world.spigotConfig.villagerActivationRange;
        world.wakeupInactiveRemainingAnimals = Math.min(world.wakeupInactiveRemainingAnimals + 1, world.spigotConfig.wakeUpInactiveAnimals);
        world.wakeupInactiveRemainingVillagers = Math.min(world.wakeupInactiveRemainingVillagers + 1, world.spigotConfig.wakeUpInactiveVillagers);
        world.wakeupInactiveRemainingMonsters = Math.min(world.wakeupInactiveRemainingMonsters + 1, world.spigotConfig.wakeUpInactiveMonsters);
        world.wakeupInactiveRemainingFlying = Math.min(world.wakeupInactiveRemainingFlying + 1, world.spigotConfig.wakeUpInactiveFlying);
        final ChunkProviderServer chunkProvider = (ChunkProviderServer) world.getChunkProvider();
        // Paper end

        int maxRange = Math.max( monsterActivationRange, animalActivationRange );
        maxRange = Math.max( maxRange, raiderActivationRange );
        maxRange = Math.max( maxRange, miscActivationRange );
        // Paper start
        maxRange = Math.max( maxRange, flyingActivationRange );
        maxRange = Math.max( maxRange, waterActivationRange );
        maxRange = Math.max( maxRange, villagerActivationRange );
        // Paper end
        maxRange = Math.min( ( ((net.minecraft.server.WorldServer)world).getChunkProvider().playerChunkMap.getEffectiveViewDistance() << 4 ) - 8, maxRange ); // Paper - no-tick view distance

        for ( EntityHuman player : world.getPlayers() )
        {

            player.activatedTick = MinecraftServer.currentTick;
            maxBB = player.getBoundingBox().grow( maxRange, 256, maxRange );
            ActivationType.MISC.boundingBox = player.getBoundingBox().grow( miscActivationRange, 256, miscActivationRange );
            ActivationType.RAIDER.boundingBox = player.getBoundingBox().grow( raiderActivationRange, 256, raiderActivationRange );
            ActivationType.ANIMAL.boundingBox = player.getBoundingBox().grow( animalActivationRange, 256, animalActivationRange );
            ActivationType.MONSTER.boundingBox = player.getBoundingBox().grow( monsterActivationRange, 256, monsterActivationRange );
            // Paper start
            ActivationType.WATER.boundingBox = player.getBoundingBox().grow( waterActivationRange, 256, waterActivationRange );
            ActivationType.FLYING_MONSTER.boundingBox = player.getBoundingBox().grow( flyingActivationRange, 256, flyingActivationRange );
            ActivationType.VILLAGER.boundingBox = player.getBoundingBox().grow( villagerActivationRange, 256, waterActivationRange );
            // Paper end

            int i = MathHelper.floor( maxBB.minX / 16.0D );
            int j = MathHelper.floor( maxBB.maxX / 16.0D );
            int k = MathHelper.floor( maxBB.minZ / 16.0D );
            int l = MathHelper.floor( maxBB.maxZ / 16.0D );

            for ( int i1 = i; i1 <= j; ++i1 )
            {
                for ( int j1 = k; j1 <= l; ++j1 )
                {
                    Chunk chunk = chunkProvider.getChunkAtIfLoadedMainThreadNoCache( i1, j1 ); // Paper
                    if ( chunk != null )
                    {
                        activateChunkEntities( chunk );
                    }
                }
            }
        }
        MinecraftTimings.entityActivationCheckTimer.stopTiming();
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     *
     * @param chunk
     */
    private static void activateChunkEntities(Chunk chunk)
    {
        // Paper start
        Entity[] rawData = chunk.entities.getRawData();
        for (int i = 0; i < chunk.entities.size(); i++) {
            Entity entity = rawData[i];
            //for ( Entity entity : (Collection<Entity>) slice )
            // Paper end
            {
                if (MinecraftServer.currentTick > entity.activatedTick) {
                    if (entity.defaultActivationState || entity.activationType.boundingBox.c(entity.getBoundingBox())) { // Paper
                        entity.activatedTick = MinecraftServer.currentTick;
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     *
     * @param entity
     * @return
     */
    public static int checkEntityImmunities(Entity entity) // Paper - return # of ticks to get immunity
    {
        // Paper start
        SpigotWorldConfig config = entity.world.spigotConfig;
        int inactiveWakeUpImmunity = checkInactiveWakeup(entity);
        if (inactiveWakeUpImmunity > -1) {
            return inactiveWakeUpImmunity;
        }
        if (entity.fireTicks > 0) {
            return 2;
        }
        long inactiveFor = MinecraftServer.currentTick - entity.activatedTick;
        // Paper end
        // quick checks.
        if ( (entity.activationType != ActivationType.WATER && entity.inWater && entity.isPushedByWater()) ) // Paper
        {
            return 100; // Paper
        }
        if ( !( entity instanceof EntityArrow ) )
        {
            if ( (!entity.isOnGround() && !(entity instanceof EntityFlying)) ) // Paper - remove passengers logic
            {
                return 10; // Paper
            }
        } else if ( !( (EntityArrow) entity ).inGround )
        {
            return 1; // Paper
        }
        // special cases.
        if ( entity instanceof EntityLiving )
        {
            EntityLiving living = (EntityLiving) entity;
            if ( living.isClimbing() || living.jumping || living.hurtTicks > 0 || living.effects.size() > 0 ) // Paper
            {
                return 1; // Paper
            }
            if ( entity instanceof EntityInsentient && ((EntityInsentient) entity ).getGoalTarget() != null) // Paper
            {
                return 20; // Paper
            }
            // Paper start
            if (entity instanceof EntityBee) {
                EntityBee bee = (EntityBee)entity;
                BlockPosition movingTarget = bee.getMovingTarget();
                if (bee.isAngry() ||
                    (bee.getHivePos() != null && bee.getHivePos().equals(movingTarget)) ||
                    (bee.getFlowerPos() != null && bee.getFlowerPos().equals(movingTarget))
                ) {
                    return 20;
                }
            }
            if ( entity instanceof EntityVillager ) {
                BehaviorController<EntityVillager> behaviorController = ((EntityVillager) entity).getBehaviorController();

                if (config.villagersActiveForPanic) {
                    for (Activity activity : VILLAGER_PANIC_IMMUNITIES) {
                        if (behaviorController.c(activity)) {
                            return 20*5;
                        }
                    }
                }

                if (config.villagersWorkImmunityAfter > 0 && inactiveFor >= config.villagersWorkImmunityAfter) {
                    if (behaviorController.c(Activity.WORK)) {
                        return config.villagersWorkImmunityFor;
                    }
                }
            }
            if ( entity instanceof EntityLlama && ( (EntityLlama ) entity ).inCaravan() )
            {
                return 1;
            }
            // Paper end
            if ( entity instanceof EntityAnimal )
            {
                EntityAnimal animal = (EntityAnimal) entity;
                if ( animal.isBaby() || animal.isInLove() )
                {
                    return 5; // Paper
                }
                if ( entity instanceof EntitySheep && ( (EntitySheep) entity ).isSheared() )
                {
                    return 1; // Paper
                }
            }
            if (entity instanceof EntityCreeper && ((EntityCreeper) entity).isIgnited()) { // isExplosive
                return 20; // Paper
            }
            // Paper start
            if (entity instanceof EntityInsentient && ((EntityInsentient) entity).targetSelector.hasTasks() ) {
                return 0;
            }
            if (entity instanceof EntityPillager) {
                EntityPillager pillager = (EntityPillager) entity;
                // TODO:?
            }
            // Paper end
        }
        return -1; // Paper
    }

    /**
     * Checks if the entity is active for this tick.
     *
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity)
    {
        // Never safe to skip fireworks or entities not yet added to chunk
        if ( !entity.inChunk || entity instanceof EntityFireworks ) {
            return true;
        }
        // Paper start - special case always immunities
        // immunize brand new entities, dead entities, and portal scenarios
        if (entity.defaultActivationState || entity.ticksLived < 20*10 || !entity.isAlive() || entity.inPortal || entity.portalCooldown > 0) {
            return true;
        }
        // immunize leashed entities
        if (entity instanceof EntityInsentient && ((EntityInsentient)entity).leashHolder instanceof EntityHuman) {
            return true;
        }
        // Paper end

        boolean isActive = entity.activatedTick >= MinecraftServer.currentTick;
        entity.isTemporarilyActive = false; // Paper

        // Should this entity tick?
        if ( !isActive )
        {
            if ( ( MinecraftServer.currentTick - entity.activatedTick - 1 ) % 20 == 0 )
            {
                // Check immunities every 20 ticks.
                // Paper start
                int immunity = checkEntityImmunities(entity);
                if (immunity >= 0) {
                    entity.activatedTick = MinecraftServer.currentTick + immunity;
                } else {
                    entity.isTemporarilyActive = true;
                }
                // Paper end
                isActive = true;

            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if (entity.ticksLived % 4 == 0 && checkEntityImmunities( entity) < 0 ) // Paper
        {
            isActive = false;
        }
        return isActive;
    }
}
