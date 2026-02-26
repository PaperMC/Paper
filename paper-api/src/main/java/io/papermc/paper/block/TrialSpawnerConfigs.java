package io.papermc.paper.block;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.block.data.type.TrialSpawner;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the built-in configurations for a trial spawner provided by
 * the vanilla datapack. Each entry exists in two variants: a normal and
 * an ominous configuration denoted by {@link TrialSpawner#isOminous()}
 */
@NullMarked
public class TrialSpawnerConfigs {

    // Start generate - TrialSpawnerConfigs
    public static final TrialSpawnerConfig TRIAL_CHAMBER_BREEZE_NORMAL = getConfig("trial_chamber/breeze/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_BREEZE_OMINOUS = getConfig("trial_chamber/breeze/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_HUSK_NORMAL = getConfig("trial_chamber/melee/husk/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_HUSK_OMINOUS = getConfig("trial_chamber/melee/husk/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_SPIDER_NORMAL = getConfig("trial_chamber/melee/spider/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_SPIDER_OMINOUS = getConfig("trial_chamber/melee/spider/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_ZOMBIE_NORMAL = getConfig("trial_chamber/melee/zombie/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_MELEE_ZOMBIE_OMINOUS = getConfig("trial_chamber/melee/zombie/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_POISON_SKELETON_NORMAL = getConfig("trial_chamber/ranged/poison_skeleton/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_POISON_SKELETON_OMINOUS = getConfig("trial_chamber/ranged/poison_skeleton/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_SKELETON_NORMAL = getConfig("trial_chamber/ranged/skeleton/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_SKELETON_OMINOUS = getConfig("trial_chamber/ranged/skeleton/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_STRAY_NORMAL = getConfig("trial_chamber/ranged/stray/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_RANGED_STRAY_OMINOUS = getConfig("trial_chamber/ranged/stray/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON_NORMAL = getConfig("trial_chamber/slow_ranged/poison_skeleton/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON_OMINOUS = getConfig("trial_chamber/slow_ranged/poison_skeleton/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_SKELETON_NORMAL = getConfig("trial_chamber/slow_ranged/skeleton/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_SKELETON_OMINOUS = getConfig("trial_chamber/slow_ranged/skeleton/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_STRAY_NORMAL = getConfig("trial_chamber/slow_ranged/stray/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SLOW_RANGED_STRAY_OMINOUS = getConfig("trial_chamber/slow_ranged/stray/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE_NORMAL = getConfig("trial_chamber/small_melee/baby_zombie/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE_OMINOUS = getConfig("trial_chamber/small_melee/baby_zombie/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER_NORMAL = getConfig("trial_chamber/small_melee/cave_spider/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER_OMINOUS = getConfig("trial_chamber/small_melee/cave_spider/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH_NORMAL = getConfig("trial_chamber/small_melee/silverfish/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH_OMINOUS = getConfig("trial_chamber/small_melee/silverfish/ominous");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_SLIME_NORMAL = getConfig("trial_chamber/small_melee/slime/normal");

    public static final TrialSpawnerConfig TRIAL_CHAMBER_SMALL_MELEE_SLIME_OMINOUS = getConfig("trial_chamber/small_melee/slime/ominous");
    // End generate - TrialSpawnerConfigs

    private static TrialSpawnerConfig getConfig(@KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIAL_SPAWNER_CONFIG).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
