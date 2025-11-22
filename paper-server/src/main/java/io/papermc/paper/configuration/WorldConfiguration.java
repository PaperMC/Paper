package io.papermc.paper.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.configuration.legacy.MaxEntityCollisionsInitializer;
import io.papermc.paper.configuration.legacy.RequiresSpigotInitialization;
import io.papermc.paper.configuration.mapping.MergeMap;
import io.papermc.paper.configuration.serializer.NbtPathSerializer;
import io.papermc.paper.configuration.serializer.collection.map.ThrowExceptions;
import io.papermc.paper.configuration.transformation.world.FeatureSeedsGeneration;
import io.papermc.paper.configuration.type.BooleanOrDefault;
import io.papermc.paper.configuration.type.DespawnRange;
import io.papermc.paper.configuration.type.Duration;
import io.papermc.paper.configuration.type.DurationOrDisabled;
import io.papermc.paper.configuration.type.EngineMode;
import io.papermc.paper.configuration.type.fallback.ArrowDespawnRate;
import io.papermc.paper.configuration.type.fallback.AutosavePeriod;
import io.papermc.paper.configuration.type.number.BelowZeroToEmpty;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
public class WorldConfiguration extends ConfigurationPart {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final int CURRENT_VERSION = 31; // (when you change the version, change the comment, so it conflicts on rebases): migrate spawn loaded configs to gamerule

    private final transient SpigotWorldConfig spigotConfig;
    private final transient ResourceLocation worldKey;

    WorldConfiguration(final SpigotWorldConfig spigotConfig, final ResourceLocation worldKey) {
        this.spigotConfig = spigotConfig;
        this.worldKey = worldKey;
    }

    public boolean isDefault() {
        return this.worldKey.equals(PaperConfigurations.WORLD_DEFAULTS_KEY);
    }

    @Setting(Configuration.VERSION_FIELD)
    public int version = CURRENT_VERSION;

    public Anticheat anticheat;

    public class Anticheat extends ConfigurationPart {

        public AntiXray antiXray;

        public class AntiXray extends ConfigurationPart {
            public boolean enabled = false;
            public EngineMode engineMode = EngineMode.HIDE;
            public int maxBlockHeight = 64;
            public int updateRadius = 2;
            public boolean lavaObscures = false;
            public boolean usePermission = false;
            public List<Block> hiddenBlocks = List.of(
                //<editor-fold desc="Anti-Xray Hidden Blocks" defaultstate="collapsed">
                Blocks.COPPER_ORE,
                Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.RAW_COPPER_BLOCK,
                Blocks.GOLD_ORE,
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.IRON_ORE,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.RAW_IRON_BLOCK,
                Blocks.COAL_ORE,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.LAPIS_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.MOSSY_COBBLESTONE,
                Blocks.OBSIDIAN,
                Blocks.CHEST,
                Blocks.DIAMOND_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.CLAY,
                Blocks.EMERALD_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.ENDER_CHEST
                //</editor-fold>
            );
            public List<Block> replacementBlocks = List.of(Blocks.STONE, Blocks.OAK_PLANKS, Blocks.DEEPSLATE);
        }
    }

    public Entities entities;

    public class Entities extends ConfigurationPart {
        public MobEffects mobEffects;

        public class MobEffects extends ConfigurationPart {
            public boolean spidersImmuneToPoisonEffect = true;
            public ImmuneToWitherEffect immuneToWitherEffect;

            public class ImmuneToWitherEffect extends ConfigurationPart {
                public boolean wither = true;
                public boolean witherSkeleton = true;
            }
        }

        public ArmorStands armorStands;

        public class ArmorStands extends ConfigurationPart {
            public boolean doCollisionEntityLookups = true;
            public boolean tick = true;
        }

        public Markers markers;

        public class Markers extends ConfigurationPart {
            public boolean tick = true;
        }

        public Sniffer sniffer;

        public class Sniffer extends ConfigurationPart {
            public IntOr.Default hatchTime = IntOr.Default.USE_DEFAULT;
            public IntOr.Default boostedHatchTime = IntOr.Default.USE_DEFAULT;
        }

        public Spawning spawning;

        public class Spawning extends ConfigurationPart {
            public ArrowDespawnRate nonPlayerArrowDespawnRate = ArrowDespawnRate.def(WorldConfiguration.this.spigotConfig);
            public ArrowDespawnRate creativeArrowDespawnRate = ArrowDespawnRate.def(WorldConfiguration.this.spigotConfig);
            public boolean filterBadTileEntityNbtFromFallingBlocks = true;
            public List<NbtPathArgument.NbtPath> filteredEntityTagNbtPaths = NbtPathSerializer.fromString(List.of("Pos", "Motion", "sleeping_pos"));
            public boolean disableMobSpawnerSpawnEggTransformation = false;
            public boolean perPlayerMobSpawns = true;
            public boolean scanForLegacyEnderDragon = true;
            @MergeMap
            public Reference2IntMap<MobCategory> spawnLimits = Util.make(new Reference2IntOpenHashMap<>(NaturalSpawner.SPAWNING_CATEGORIES.length), map -> Arrays.stream(NaturalSpawner.SPAWNING_CATEGORIES).forEach(mobCategory -> map.put(mobCategory, -1)));
            @MergeMap
            public Map<MobCategory, DespawnRangePair> despawnRanges = Arrays.stream(MobCategory.values()).collect(Collectors.toMap(Function.identity(), category -> DespawnRangePair.createDefault()));
            public DespawnRange.Shape despawnRangeShape = DespawnRange.Shape.ELLIPSOID;
            @MergeMap
            public Reference2IntMap<MobCategory> ticksPerSpawn = Util.make(new Reference2IntOpenHashMap<>(NaturalSpawner.SPAWNING_CATEGORIES.length), map -> Arrays.stream(NaturalSpawner.SPAWNING_CATEGORIES).forEach(mobCategory -> map.put(mobCategory, -1)));

            @ConfigSerializable
            public record DespawnRangePair(@Required DespawnRange hard, @Required DespawnRange soft) {
                public static DespawnRangePair createDefault() {
                    return new DespawnRangePair(
                        new DespawnRange(IntOr.Default.USE_DEFAULT),
                        new DespawnRange(IntOr.Default.USE_DEFAULT)
                    );
                }
            }

            public @ThrowExceptions Reference2ObjectMap<EntityType<?>, IntOr.Disabled> despawnTime = Util.make(new Reference2ObjectOpenHashMap<>(), map -> {
                map.put(EntityType.SNOWBALL, IntOr.Disabled.DISABLED);
                map.put(EntityType.LLAMA_SPIT, IntOr.Disabled.DISABLED);
            });

            @PostProcess
            public void precomputeDespawnDistances() throws SerializationException {
                for (final Map.Entry<MobCategory, DespawnRangePair> entry : this.despawnRanges.entrySet()) {
                    final MobCategory category = entry.getKey();
                    final DespawnRangePair range = entry.getValue();
                    range.hard().preComputed(category.getDespawnDistance(), category.getSerializedName());
                    range.soft().preComputed(category.getNoDespawnDistance(), category.getSerializedName());
                }
            }

            public WaterAnimalSpawnHeight wateranimalSpawnHeight;

            public class WaterAnimalSpawnHeight extends ConfigurationPart {
                public IntOr.Default maximum = IntOr.Default.USE_DEFAULT;
                public IntOr.Default minimum = IntOr.Default.USE_DEFAULT;
            }

            public SlimeSpawnHeight slimeSpawnHeight;

            public class SlimeSpawnHeight extends ConfigurationPart {

                public SurfaceSpawnableSlimeBiome surfaceBiome;

                public class SurfaceSpawnableSlimeBiome extends ConfigurationPart {
                    public double maximum = 70;
                    public double minimum = 50;
                }

                public SlimeChunk slimeChunk;

                public class SlimeChunk extends ConfigurationPart {
                    public double maximum = 40;
                }
            }

            public WanderingTrader wanderingTrader;

            public class WanderingTrader extends ConfigurationPart {
                public int spawnMinuteLength = 1200;
                public int spawnDayLength = net.minecraft.world.entity.npc.WanderingTraderSpawner.DEFAULT_SPAWN_DELAY;
                public int spawnChanceFailureIncrement = 25;
                public int spawnChanceMin = 25;
                public int spawnChanceMax = 75;
            }

            public boolean allChunksAreSlimeChunks = false;
            @BelowZeroToEmpty
            public DoubleOr.Default skeletonHorseThunderSpawnChance = DoubleOr.Default.USE_DEFAULT;
            public boolean ironGolemsCanSpawnInAir = false;
            public boolean countAllMobsForSpawning = false;
            @BelowZeroToEmpty
            public IntOr.Default monsterSpawnMaxLightLevel = IntOr.Default.USE_DEFAULT;
            public DuplicateUUID duplicateUuid;

            public class DuplicateUUID extends ConfigurationPart {
                public DuplicateUUIDMode mode = DuplicateUUIDMode.SAFE_REGEN;
                public int safeRegenDeleteRange = 32;

                public enum DuplicateUUIDMode {
                    SAFE_REGEN, DELETE, NOTHING, WARN;
                }
            }
            public AltItemDespawnRate altItemDespawnRate;

            public class AltItemDespawnRate extends ConfigurationPart {
                public boolean enabled = false;
                public Reference2IntMap<Item> items = new Reference2IntOpenHashMap<>(Map.of(Items.COBBLESTONE, 300));
            }
        }

        public Behavior behavior;

        public class Behavior extends ConfigurationPart {
            public boolean disableChestCatDetection = false;
            public boolean spawnerNerfedMobsShouldJump = false;
            public int experienceMergeMaxValue = -1;
            public boolean shouldRemoveDragon = false;
            public boolean zombiesTargetTurtleEggs = true;
            public boolean piglinsGuardChests = true;
            public double babyZombieMovementModifier = 0.5;
            public boolean allowSpiderWorldBorderClimbing = true;

            private static final List<EntityType<?>> ZOMBIE_LIKE = List.of(EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);
            @MergeMap
            public Map<EntityType<?>, List<Difficulty>> doorBreakingDifficulty = Util.make(new IdentityHashMap<>(), map -> {
                for (final EntityType<?> type : ZOMBIE_LIKE) {
                    map.put(type, Arrays.stream(Difficulty.values()).filter(Zombie.DOOR_BREAKING_PREDICATE).toList());
                }
                map.put(EntityType.VINDICATOR, Arrays.stream(Difficulty.values()).filter(Vindicator.DOOR_BREAKING_PREDICATE).toList());
            });

            public boolean disableCreeperLingeringEffect = false;
            public boolean enderDragonsDeathAlwaysPlacesDragonEgg = false;
            public boolean phantomsDoNotSpawnOnCreativePlayers = true;
            public boolean phantomsOnlyAttackInsomniacs = true;
            public int playerInsomniaStartTicks = 72000;
            public int phantomsSpawnAttemptMinSeconds = 60;
            public int phantomsSpawnAttemptMaxSeconds = 119;
            public boolean parrotsAreUnaffectedByPlayerMovement = false;
            @BelowZeroToEmpty
            public DoubleOr.Default zombieVillagerInfectionChance = DoubleOr.Default.USE_DEFAULT;
            public MobsCanAlwaysPickUpLoot mobsCanAlwaysPickUpLoot;

            public class MobsCanAlwaysPickUpLoot extends ConfigurationPart {
                public boolean zombies = false;
                public boolean skeletons = false;
            }

            public boolean disablePlayerCrits = false;
            public boolean nerfPigmenFromNetherPortals = false;
            @Comment("Prevents merging items that are not on the same y level, preventing potential visual artifacts.")
            public boolean onlyMergeItemsHorizontally = false;
            public PillagerPatrols pillagerPatrols;

            public class PillagerPatrols extends ConfigurationPart {
                public boolean disable = false;
                public double spawnChance = 0.2;
                public SpawnDelay spawnDelay;
                public Start start;

                public class SpawnDelay extends ConfigurationPart {
                    public boolean perPlayer = false;
                    public int ticks = 12000;
                }

                public class Start extends ConfigurationPart {
                    public boolean perPlayer = false;
                    public int day = 5;
                }
            }

            @Comment("Adds a cooldown to bees being released after a failed release, which can occur if the hive is blocked or it being night.")
            public boolean cooldownFailedBeehiveReleases = true;
            @Comment("The delay before retrying POI acquisition when entity navigation is stuck. This will reduce pathfinding performance impact. Measured in ticks.")
            public IntOr.Disabled stuckEntityPoiRetryDelay = new IntOr.Disabled(OptionalInt.of(200));
        }

        public TrackingRangeY trackingRangeY;

        public class TrackingRangeY extends ConfigurationPart {
            public boolean enabled = false;
            public IntOr.Default player = IntOr.Default.USE_DEFAULT;
            public IntOr.Default animal = IntOr.Default.USE_DEFAULT;
            public IntOr.Default monster = IntOr.Default.USE_DEFAULT;
            public IntOr.Default misc = IntOr.Default.USE_DEFAULT;
            public IntOr.Default display = IntOr.Default.USE_DEFAULT;
            public IntOr.Default other = IntOr.Default.USE_DEFAULT;

            public int get(Entity entity, int def) {
                if (entity instanceof EnderDragon) {
                    return -1; // Ender dragon is exempt
                } else if (entity instanceof Display) {
                    return display.or(def);
                } else if (entity instanceof Player) {
                    return player.or(def);
                } else if (entity instanceof HangingEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrb) {
                    return misc.or(def);
                }
                switch (entity.activationType) {
                    case ANIMAL, WATER, VILLAGER -> {
                        return animal.or(def);
                    }
                    case MONSTER, FLYING_MONSTER, RAIDER -> {
                        return monster.or(def);
                    }
                    default -> {
                        return other.or(def);
                    }
                }
            }
        }
    }

    public Lootables lootables;

    public class Lootables extends ConfigurationPart {
        public boolean autoReplenish = false;
        public boolean restrictPlayerReloot = true;
        public DurationOrDisabled restrictPlayerRelootTime = DurationOrDisabled.USE_DISABLED;
        public boolean resetSeedOnFill = true;
        public int maxRefills = -1;
        public Duration refreshMin = Duration.of("12h");
        public Duration refreshMax = Duration.of("2d");
        public boolean retainUnlootedShulkerBoxLootTableOnNonPlayerBreak = true;
    }

    public MaxGrowthHeight maxGrowthHeight;

    public class MaxGrowthHeight extends ConfigurationPart {
        public int cactus = 3;
        public int reeds = 3;
        public Bamboo bamboo;

        public class Bamboo extends ConfigurationPart {
            public int max = BambooStalkBlock.MAX_HEIGHT;
            public int min = 11;
        }
    }

    public Scoreboards scoreboards;

    public class Scoreboards extends ConfigurationPart {
        public boolean allowNonPlayerEntitiesOnScoreboards = true;
        public boolean useVanillaWorldScoreboardNameColoring = false;
    }

    public Environment environment;

    public class Environment extends ConfigurationPart {
        public boolean disableThunder = false;
        public boolean disableIceAndSnow = false;
        public boolean optimizeExplosions = false;
        public boolean disableExplosionKnockback = false;
        public boolean generateFlatBedrock = false;
        public FrostedIce frostedIce;
        public DoubleOr.Disabled voidDamageAmount = new DoubleOr.Disabled(OptionalDouble.of(4));
        public double voidDamageMinBuildHeightOffset = -64.0;

        public class FrostedIce extends ConfigurationPart {
            public boolean enabled = true;
            public Delay delay;

            public class Delay extends ConfigurationPart {
                public int min = 20;
                public int max = 40;
            }
        }

        public TreasureMaps treasureMaps;
        public class TreasureMaps extends ConfigurationPart {
            public boolean enabled = true;
            @NestedSetting({"find-already-discovered", "villager-trade"})
            public boolean findAlreadyDiscoveredVillager = false;
            @NestedSetting({"find-already-discovered", "loot-tables"})
            public BooleanOrDefault findAlreadyDiscoveredLootTable = BooleanOrDefault.USE_DEFAULT;
        }

        public int fireTickDelay = 30;
        public int waterOverLavaFlowSpeed = 5;
        public int portalSearchRadius = 128;
        public int portalCreateRadius = 16;
        public boolean portalSearchVanillaDimensionScaling = true;
        public IntOr.Disabled netherCeilingVoidDamageHeight = IntOr.Disabled.DISABLED;
        public int maxFluidTicks = 65536;
        public int maxBlockTicks = 65536;
        public boolean locateStructuresOutsideWorldBorder = false;
    }

    public Spawn spawn;

    public class Spawn extends ConfigurationPart {
        public boolean allowUsingSignsInsideSpawnProtection = false;
    }

    public Maps maps;

    public class Maps extends ConfigurationPart {
        public int itemFrameCursorLimit = 128;
        public int itemFrameCursorUpdateInterval = 10;
    }

    public Fixes fixes;

    public class Fixes extends ConfigurationPart {
        public boolean fixItemsMergingThroughWalls = false;
        public boolean disableUnloadedChunkEnderpearlExploit = false;
        public boolean preventTntFromMovingInWater = false;
        public boolean splitOverstackedLoot = true;
        public IntOr.Disabled fallingBlockHeightNerf = IntOr.Disabled.DISABLED;
        public IntOr.Disabled tntEntityHeightNerf = IntOr.Disabled.DISABLED;
    }

    public UnsupportedSettings unsupportedSettings;

    public class UnsupportedSettings extends ConfigurationPart {
        public boolean fixInvulnerableEndCrystalExploit = true;
        public boolean disableWorldTickingWhenEmpty = false;
    }

    public Hopper hopper;

    public class Hopper extends ConfigurationPart {
        public boolean cooldownWhenFull = true;
        public boolean disableMoveEvent = false;
        public boolean ignoreOccludingBlocks = false;
    }

    public Collisions collisions;

    public class Collisions extends ConfigurationPart {
        public boolean onlyPlayersCollide = false;
        public boolean allowVehicleCollisions = true;
        public boolean fixClimbingBypassingCrammingRule = false;
        @RequiresSpigotInitialization(MaxEntityCollisionsInitializer.class)
        public int maxEntityCollisions = 8;
        public boolean allowPlayerCrammingDamage = false;
    }

    public Chunks chunks;

    public class Chunks extends ConfigurationPart {
        public AutosavePeriod autoSaveInterval = AutosavePeriod.def();
        public int maxAutoSaveChunksPerTick = 24;
        public int fixedChunkInhabitedTime = -1;
        public boolean preventMovingIntoUnloadedChunks = false;
        public Duration delayChunkUnloadsBy = Duration.of("10s");
        public Reference2IntMap<EntityType<?>> entityPerChunkSaveLimit = Util.make(new Reference2IntOpenHashMap<>(BuiltInRegistries.ENTITY_TYPE.size()), map -> {
            map.defaultReturnValue(-1);
            map.put(EntityType.EXPERIENCE_ORB, -1);
            map.put(EntityType.SNOWBALL, -1);
            map.put(EntityType.ENDER_PEARL, -1);
            map.put(EntityType.ARROW, -1);
            map.put(EntityType.FIREBALL, -1);
            map.put(EntityType.SMALL_FIREBALL, -1);
        });
        public boolean flushRegionsOnSave = false;

        @PostProcess
        private void postProcess() {
            FeatureHooks.setPlayerChunkUnloadDelay(this.delayChunkUnloadsBy.ticks());
        }
    }

    public FishingTimeRange fishingTimeRange;

    public class FishingTimeRange extends ConfigurationPart {
        public int minimum = 100;
        public int maximum = 600;
    }

    public TickRates tickRates;

    public class TickRates extends ConfigurationPart {
        public int grassSpread = 1;
        public int containerUpdate = 1;
        public int mobSpawner = 1;
        public int wetFarmland = 1;
        public int dryFarmland = 1;
        public Table<EntityType<?>, String, Integer> sensor = Util.make(HashBasedTable.create(), table -> table.put(EntityType.VILLAGER, "secondarypoisensor", 40));
        public Table<EntityType<?>, String, Integer> behavior = Util.make(HashBasedTable.create(), table -> table.put(EntityType.VILLAGER, "validatenearbypoi", -1));
    }

    @Setting(FeatureSeedsGeneration.FEATURE_SEEDS_KEY)
    public FeatureSeeds featureSeeds;

    public class FeatureSeeds extends ConfigurationPart {
        @SuppressWarnings("unused") // Is used in FeatureSeedsGeneration
        @Setting(FeatureSeedsGeneration.GENERATE_KEY)
        public boolean generateRandomSeedsForAll = false;
        @Setting(FeatureSeedsGeneration.FEATURES_KEY)
        public Reference2LongMap<Holder<ConfiguredFeature<?, ?>>> features = new Reference2LongOpenHashMap<>();

        @PostProcess
        private void postProcess() {
            this.features.defaultReturnValue(-1);
        }
    }

    public CommandBlocks commandBlocks;

    public class CommandBlocks extends ConfigurationPart {
        public int permissionsLevel = 2;
        public boolean forceFollowPermLevel = true;
    }

    public Misc misc;

    public class Misc extends ConfigurationPart {
        public boolean updatePathfindingOnBlockUpdate = true;
        public boolean showSignClickCommandFailureMsgsToPlayer = false;
        public RedstoneImplementation redstoneImplementation = RedstoneImplementation.VANILLA;
        public AlternateCurrentUpdateOrder alternateCurrentUpdateOrder = AlternateCurrentUpdateOrder.HORIZONTAL_FIRST_OUTWARD;
        public boolean disableEndCredits = false;
        public DoubleOr.Default maxLeashDistance = DoubleOr.Default.USE_DEFAULT;
        public boolean disableSprintInterruptionOnAttack = false;
        public boolean disableRelativeProjectileVelocity = false;
        public boolean legacyEnderPearlBehavior = false;
        public boolean allowRemoteEnderDragonRespawning = false;

        public enum RedstoneImplementation {
            VANILLA, EIGENCRAFT, ALTERNATE_CURRENT
        }

        public enum AlternateCurrentUpdateOrder {
        	HORIZONTAL_FIRST_OUTWARD, HORIZONTAL_FIRST_INWARD, VERTICAL_FIRST_OUTWARD, VERTICAL_FIRST_INWARD
        }
    }
}
