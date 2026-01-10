package io.papermc.paper.configuration;

import org.spongepowered.configurate.NodePath;

import static org.spongepowered.configurate.NodePath.path;

interface RemovedConfigurations {

    NodePath[] REMOVED_WORLD_PATHS = {
        path("falling-blocks-collide-with-signs"),
        path("fast-drain"),
        path("lava-flow-speed"),
        path("load-chunks"),
        path("misc", "boats-drop-boats"),
        path("player-exhaustion"),
        path("remove-unloaded"),
        path("tick-next-tick-list-cap"),
        path("tick-next-tick-list-cap-ignores-redstone"),
        path("elytra-hit-wall-damage"),
        path("queue-light-updates"),
        path("save-queue-limit-for-auto-save"),
        path("max-chunk-sends-per-tick"),
        path("max-chunk-gens-per-tick"),
        path("fire-physics-event-for-redstone"),
        path("fix-zero-tick-instant-grow-farms"),
        path("bed-search-radius"),
        path("lightning-strike-distance-limit"),
        path("fix-wither-targeting-bug"),
        path("remove-corrupt-tile-entities"),
        path("allow-undead-horse-leashing"),
        path("reset-arrow-despawn-timer-on-fall"),
        path("seed-based-feature-search"),
        path("seed-based-feature-search-loads-chunks"),
        path("viewdistances", "no-tick-view-distance"),
        path("seed-based-feature-search"), // unneeded as of 1.18
        path("seed-based-feature-search-loads-chunks"), // unneeded as of 1.18
        path("reset-arrow-despawn-timer-on-fall"),
        path("squid-spawn-height"),
        path("viewdistances"),
        path("use-alternate-fallingblock-onGround-detection"),
        path("skip-entity-ticking-in-chunks-scheduled-for-unload"),
        path("tracker-update-distance"),
        path("allow-block-location-tab-completion"),
        path("cache-chunk-maps"),
        path("disable-mood-sounds"),
        path("fix-cannons"),
        path("player-blocking-damage-multiplier"),
        path("remove-invalid-mob-spawner-tile-entities"),
        path("use-hopper-check"),
        path("use-async-lighting"),
        path("tnt-explosion-volume"),
        path("entities", "spawning", "despawn-ranges", "soft"),
        path("entities", "spawning", "despawn-ranges", "hard"),
        path("fixes", "fix-curing-zombie-villager-discount-exploit"),
        path("entities", "mob-effects", "undead-immune-to-certain-effects"),
        path("entities", "entities-target-with-follow-range"),
        path("environment", "disable-teleportation-suffocation-check"),
        path("misc", "light-queue-size"),
        path("spawn", "keep-spawn-loaded"),
        path("spawn", "keep-spawn-loaded-range"),
        path("misc", "shield-blocking-delay")
    };
    // spawn.keep-spawn-loaded and spawn.keep-spawn-loaded-range are no longer used, but kept
    // in the world default config for compatibility with old worlds being migrated to use the gamerule

    NodePath[] REMOVED_GLOBAL_PATHS = {
        path("data-value-allowed-items"),
        path("effect-modifiers"),
        path("stackable-buckets"),
        path("async-chunks"),
        path("queue-light-updates-max-loss"),
        path("sleep-between-chunk-saves"),
        path("remove-invalid-statistics"),
        path("min-chunk-load-threads"),
        path("use-versioned-world"),
        path("save-player-data"), // to spigot (converted)
        path("log-named-entity-deaths"), // default in vanilla
        path("chunk-tasks-per-tick"), // removed in tuinity merge
        path("item-validation", "loc-name"),
        path("commandErrorMessage"),
        path("baby-zombie-movement-speed"),
        path("limit-player-interactions"),
        path("warnWhenSettingExcessiveVelocity"),
        path("logging", "use-rgb-for-named-text-colors"),
        path("unsupported-settings", "allow-grindstone-overstacking"),
        path("unsupported-settings", "allow-tripwire-disarming-exploits"),
        path("commands", "fix-target-selector-tag-completion"),
        path("misc", "fix-entity-position-desync"),
        path("chunk-system", "gen-parallelism")
    };

}
