package io.papermc.paper.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import io.papermc.paper.FeatureHooks;
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
import io.papermc.paper.configuration.type.number.BelowZeroToEmpty;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.configuration.type.number.LongOr;
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
import net.minecraft.util.Util;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
public class WorldConfiguration extends ConfigurationPart {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final int CURRENT_VERSION = 32; // (when you change the version, change the comment, so it conflicts on rebases): spawn limits and save interval no longer defer to bukkit.yml

    private final transient Identifier worldKey;

    WorldConfiguration(final Identifier worldKey) {
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
            @Comment("The radius around a player, in chunks, that mobs may naturally spawn in.")
            public int mobSpawnRange = 8;
            @Comment("Whether mobs spawned by a monster spawner have reduced AI.")
            public boolean nerfSpawnerMobs = false;
            @Comment("Whether zombified piglins may spawn from nether portals.")
            public boolean enableZombiePigmenPortalSpawns = true;
            @Comment("Ticks before a dropped item despawns.")
            public int itemDespawnRate = 6000;
            @Comment("Ticks before a stuck arrow despawns.")
            public int arrowDespawnRate = 1200;
            @Comment("Ticks before a stuck trident despawns. Defaults to the arrow despawn rate.")
            public IntOr.Default tridentDespawnRate = IntOr.Default.USE_DEFAULT;
            public ArrowDespawnRate nonPlayerArrowDespawnRate = ArrowDespawnRate.def(WorldConfiguration.this);
            public ArrowDespawnRate creativeArrowDespawnRate = ArrowDespawnRate.def(WorldConfiguration.this);
            public IntOr.Disabled maxArrowDespawnInvulnerability = new IntOr.Disabled(OptionalInt.of(200));
            public boolean filterBadTileEntityNbtFromFallingBlocks = true;
            public List<NbtPathArgument.NbtPath> filteredEntityTagNbtPaths = NbtPathSerializer.fromString(List.of("Pos", "Motion", "sleeping_pos"));
            public boolean disableMobSpawnerSpawnEggTransformation = false;
            public boolean perPlayerMobSpawns = true;
            public boolean scanForLegacyEnderDragon = true;
            @MergeMap
            public Reference2IntMap<MobCategory> spawnLimits = Util.make(new Reference2IntOpenHashMap<>(NaturalSpawner.SPAWNING_CATEGORIES.length), map -> {
                map.put(MobCategory.MONSTER, 70);
                map.put(MobCategory.CREATURE, 10);
                map.put(MobCategory.WATER_CREATURE, 5);
                map.put(MobCategory.WATER_AMBIENT, 20);
                map.put(MobCategory.UNDERGROUND_WATER_CREATURE, 5);
                map.put(MobCategory.AXOLOTLS, 5);
                map.put(MobCategory.AMBIENT, 15);
            });
            @MergeMap
            public Map<MobCategory, DespawnRangePair> despawnRanges = Arrays.stream(MobCategory.values()).collect(Collectors.toMap(Function.identity(), category -> DespawnRangePair.createDefault()));
            public DespawnRange.Shape despawnRangeShape = DespawnRange.Shape.ELLIPSOID;
            @MergeMap
            public Reference2IntMap<MobCategory> ticksPerSpawn = Util.make(new Reference2IntOpenHashMap<>(NaturalSpawner.SPAWNING_CATEGORIES.length), map -> {
                map.put(MobCategory.MONSTER, 1);
                map.put(MobCategory.CREATURE, 400);
                map.put(MobCategory.WATER_CREATURE, 1);
                map.put(MobCategory.WATER_AMBIENT, 1);
                map.put(MobCategory.UNDERGROUND_WATER_CREATURE, 1);
                map.put(MobCategory.AXOLOTLS, 1);
                map.put(MobCategory.AMBIENT, 1);
            });

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
                map.put(EntityTypes.SNOWBALL, IntOr.Disabled.DISABLED);
                map.put(EntityTypes.LLAMA_SPIT, IntOr.Disabled.DISABLED);
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
                public int spawnDayLength = net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner.DEFAULT_SPAWN_DELAY;
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

            private static final List<EntityType<?>> ZOMBIE_LIKE = List.of(EntityTypes.ZOMBIE, EntityTypes.HUSK, EntityTypes.ZOMBIE_VILLAGER, EntityTypes.ZOMBIFIED_PIGLIN);
            @MergeMap
            public Map<EntityType<?>, List<Difficulty>> doorBreakingDifficulty = Util.make(new IdentityHashMap<>(), map -> {
                for (final EntityType<?> type : ZOMBIE_LIKE) {
                    map.put(type, Arrays.stream(Difficulty.values()).filter(Zombie.DOOR_BREAKING_PREDICATE).toList());
                }
                map.put(EntityTypes.VINDICATOR, Arrays.stream(Difficulty.values()).filter(Vindicator.DOOR_BREAKING_PREDICATE).toList());
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
            @Comment("Whether zombies are always hostile towards villagers, regardless of difficulty.")
            public boolean zombieAggressiveTowardsVillager = true;

            @Comment("The radius within which dropped items and experience orbs merge. Set to -1 to disable.")
            public MergeRadius mergeRadius;

            public class MergeRadius extends ConfigurationPart {
                public double item = 0.5;
                public double experience = -1.0;
            }
        }

        @Comment("The horizontal distance, in blocks, at which entities of each category are sent to players.")
        public TrackingRange trackingRange;

        public class TrackingRange extends ConfigurationPart {
            public int player = 128;
            public int animal = 96;
            public int monster = 96;
            public int misc = 96;
            public int display = 128;
            public int other = 64;
        }

        @Comment("The distance, in blocks, at which entities of each category are ticked. Entities outside it are mostly frozen.")
        public ActivationRange activationRange;

        public class ActivationRange extends ConfigurationPart {
            public int animals = 32;
            public int monsters = 32;
            public int raiders = 64;
            public int misc = 16;
            public int water = 16;
            public int villagers = 32;
            public int flyingMonsters = 32;
            @Comment("Ticks a villager is immune to deactivation after starting work.")
            public int villagersWorkImmunityAfter = 100;
            public int villagersWorkImmunityFor = 20;
            @Comment("Whether villagers stay active while panicking.")
            public boolean villagersActiveForPanic = true;
            public boolean tickInactiveVillagers = true;
            @Comment("Whether spectators keep nearby entities active.")
            public boolean ignoreSpectators = false;

            @Comment("Periodically wakes a limited number of deactivated entities so they do not stay frozen forever.")
            public WakeUpInactive wakeUpInactive;

            public class WakeUpInactive extends ConfigurationPart {
                public int animalsMaxPerTick = 4;
                public int animalsEvery = 1200;
                public int animalsFor = 100;
                public int monstersMaxPerTick = 8;
                public int monstersEvery = 400;
                public int monstersFor = 100;
                public int villagersMaxPerTick = 4;
                public int villagersEvery = 600;
                public int villagersFor = 100;
                public int flyingMonstersMaxPerTick = 8;
                public int flyingMonstersEvery = 200;
                public int flyingMonstersFor = 100;
            }
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

    @Comment("""
        Percentage chance that a growth tick actually advances the plant.
        100 is vanilla speed, 50 is half as fast, 200 is twice as fast.""")
    public GrowthModifiers growthModifiers;

    public class GrowthModifiers extends ConfigurationPart {
        public int cactus = 100;
        public int cane = 100;
        public int melon = 100;
        public int mushroom = 100;
        public int pumpkin = 100;
        public int sapling = 100;
        public int beetroot = 100;
        public int carrot = 100;
        public int potato = 100;
        public int torchFlower = 100;
        public int wheat = 100;
        public int netherWart = 100;
        public int vine = 100;
        public int cocoa = 100;
        public int bamboo = 100;
        public int sweetBerry = 100;
        public int kelp = 100;
        public int twistingVines = 100;
        public int weepingVines = 100;
        public int caveVines = 100;
        public int glowBerry = 100;
        public int pitcherPlant = 100;
    }

    @Comment("Exhaustion added by each action. Higher values make the hunger bar drain faster.")
    public Hunger hunger;

    public class Hunger extends ConfigurationPart {
        public float jumpWalkExhaustion = 0.05f;
        public float jumpSprintExhaustion = 0.2f;
        public float combatExhaustion = 0.1f;
        public float regenExhaustion = 6.0f;
        public float swimMultiplier = 0.01f;
        public float sprintMultiplier = 0.1f;
        public float otherMultiplier = 0.0f;
    }

    @Comment("""
        Salts used when deciding where structures and other seeded features generate.
        Changing these changes where they appear in newly generated chunks.""")
    public Seeds seeds;

    public class Seeds extends ConfigurationPart {
        public int village = 10387312;
        public int desert = 14357617;
        public int igloo = 14357618;
        public int jungle = 14357619;
        public int swamp = 14357620;
        public int monument = 10387313;
        public int shipwreck = 165745295;
        public int ocean = 14357621;
        public int outpost = 165745296;
        public int endCity = 10387313;
        public int slime = 987234911;
        public int nether = 30084232;
        public int mansion = 10387319;
        public int fossil = 14357921;
        public int portal = 34222645;
        public int ancientCity = 20083232;
        public int trailRuins = 83469867;
        public int trialChambers = 94251327;
        public int buriedTreasure = 10387320;
        @Comment("Set to 'default' to use the vanilla value.")
        public IntOr.Default mineshaft = IntOr.Default.USE_DEFAULT;
        @Comment("Set to 'default' to use the vanilla value.")
        public LongOr.Default stronghold = LongOr.Default.USE_DEFAULT;
    }

    public Environment environment;

    public class Environment extends ConfigurationPart {
        public boolean disableThunder = false;
        @Comment("The 1-in-N chance per tick of a thunderstorm starting. Higher means less frequent.")
        public int thunderChance = 100000;
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
        @Comment("The maximum number of primed TNT entities ticked per tick. Set to -1 to disable the limit.")
        public int maxTntPerTick = 100;
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
        public Ticking ticking;

        public class Ticking extends ConfigurationPart {
            public boolean chunks = true;
            public boolean blockEntities = true;
        }
    }

    public Hopper hopper;

    public class Hopper extends ConfigurationPart {
        public boolean cooldownWhenFull = true;
        @Comment("The number of items moved per hopper transfer.")
        public int amount = 1;
        @Comment("Whether hoppers may load chunks to pull from or push into containers across a chunk border.")
        public boolean canLoadChunks = false;
        public boolean disableMoveEvent = false;
        public boolean ignoreOccludingBlocks = false;
    }

    public Collisions collisions;

    public class Collisions extends ConfigurationPart {
        public boolean onlyPlayersCollide = false;
        public boolean allowVehicleCollisions = true;
        public boolean fixClimbingBypassingCrammingRule = false;
        public int maxEntityCollisions = 8;
        public boolean allowPlayerCrammingDamage = false;
    }

    public Chunks chunks;

    public class Chunks extends ConfigurationPart {
        @Comment("Ticks between saves of this world. Set to 0 or below to disable automatic saving.")
        public int autoSaveInterval = 6000;
        @Comment("Per-world view distance. Defaults to the server.properties value.")
        public IntOr.Default viewDistance = IntOr.Default.USE_DEFAULT;
        @Comment("Per-world simulation distance. Defaults to the server.properties value.")
        public IntOr.Default simulationDistance = IntOr.Default.USE_DEFAULT;
        @Comment("Whether chunks kept loaded by a ticking-frozen world are unloaded.")
        public boolean unloadFrozenChunks = false;
        public int maxAutoSaveChunksPerTick = 24;
        public int fixedChunkInhabitedTime = -1;
        public boolean preventMovingIntoUnloadedChunks = false;
        public Duration delayChunkUnloadsBy = Duration.of("10s");
        public Reference2IntMap<EntityType<?>> entityPerChunkSaveLimit = Util.make(new Reference2IntOpenHashMap<>(BuiltInRegistries.ENTITY_TYPE.size()), map -> {
            map.defaultReturnValue(-1);
            map.put(EntityTypes.EXPERIENCE_ORB, -1);
            map.put(EntityTypes.SNOWBALL, -1);
            map.put(EntityTypes.ENDER_PEARL, -1);
            map.put(EntityTypes.ARROW, -1);
            map.put(EntityTypes.FIREBALL, -1);
            map.put(EntityTypes.SMALL_FIREBALL, -1);
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
        @Comment("Ticks between hopper transfer attempts.")
        public int hopperTransfer = 8;
        @Comment("Ticks between hopper pickup checks.")
        public int hopperCheck = 1;
        @Comment("Ticks between hanging entity (item frame, painting) validity checks.")
        public int hangingTickFrequency = 100;

        public int containerUpdate = 1;
        public int mobSpawner = 1;
        public int wetFarmland = 1;
        public int dryFarmland = 1;
        public Table<EntityType<?>, String, Integer> sensor = Util.make(HashBasedTable.create(), table -> table.put(EntityTypes.VILLAGER, "secondarypoisensor", 40));
        public Table<EntityType<?>, String, Integer> behavior = Util.make(HashBasedTable.create(), table -> table.put(EntityTypes.VILLAGER, "validatenearbypoi", -1));
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
        @Comment("Radius in blocks that these sounds are broadcast to. Set to 0 to use the vanilla behaviour.")
        public SoundRadius soundRadius;

        public class SoundRadius extends ConfigurationPart {
            public int dragonDeath = 0;
            public int witherSpawn = 0;
            public int endPortal = 0;
        }

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
