package io.papermc.paper.configuration.transformation.world;

import io.papermc.paper.configuration.Configuration;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static io.papermc.paper.configuration.transformation.Transformations.move;
import static org.spongepowered.configurate.NodePath.path;
import static org.spongepowered.configurate.transformation.TransformAction.remove;

public final class LegacySpigotWorldConfig {

    private LegacySpigotWorldConfig() {
    }

    public static void migrate(final ConfigurationNode node) throws ConfigurateException {
        toNewFormat().apply(node);
        pruneEmptyLegacySections(node);
        node.removeChild(Configuration.LEGACY_CONFIG_VERSION_FIELD);
    }

    public static ConfigurationTransformation toNewFormat() {
        return ConfigurationTransformation.versionedBuilder()
            .versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD)
            .addVersion(Configuration.FINAL_LEGACY_SPIGOT_VERSION + 1, newFormatTransformation())
            .build();
    }

    private static ConfigurationTransformation newFormatTransformation() {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder()
            // chunks
            .addAction(path("view-distance"), move(path("chunks", "view-distance")))
            .addAction(path("simulation-distance"), move(path("chunks", "simulation-distance")))
            .addAction(path("unload-frozen-chunks"), move(path("chunks", "unload-frozen-chunks")))
            // entities: spawning
            .addAction(path("mob-spawn-range"), move(entities("spawning", "mob-spawn-range")))
            .addAction(path("nerf-spawner-mobs"), move(entities("spawning", "nerf-spawner-mobs")))
            .addAction(path("enable-zombie-pigmen-portal-spawns"), move(entities("spawning", "enable-zombie-pigmen-portal-spawns")))
            .addAction(path("item-despawn-rate"), move(entities("spawning", "item-despawn-rate")))
            .addAction(path("arrow-despawn-rate"), move(entities("spawning", "arrow-despawn-rate")))
            .addAction(path("trident-despawn-rate"), move(entities("spawning", "trident-despawn-rate")))
            // entities: behavior
            .addAction(path("zombie-aggressive-towards-villager"), move(entities("behavior", "zombie-aggressive-towards-villager")))
            .addAction(path("merge-radius", "item"), move(entities("behavior", "merge-radius", "item")))
            .addAction(path("merge-radius", "exp"), move(entities("behavior", "merge-radius", "experience")))
            // entities: tracking range (spigot uses plurals, the paper section is singular)
            .addAction(path("entity-tracking-range", "players"), move(entities("tracking-range", "player")))
            .addAction(path("entity-tracking-range", "animals"), move(entities("tracking-range", "animal")))
            .addAction(path("entity-tracking-range", "monsters"), move(entities("tracking-range", "monster")))
            .addAction(path("entity-tracking-range", "misc"), move(entities("tracking-range", "misc")))
            .addAction(path("entity-tracking-range", "display"), move(entities("tracking-range", "display")))
            .addAction(path("entity-tracking-range", "other"), move(entities("tracking-range", "other")))
            // collisions
            .addAction(path("max-entity-collisions"), move(path("collisions", "max-entity-collisions")))
            // environment
            .addAction(path("thunder-chance"), move(path("environment", "thunder-chance")))
            // hopper
            .addAction(path("hopper-amount"), move(path("hopper", "amount")))
            .addAction(path("hopper-can-load-chunks"), move(path("hopper", "can-load-chunks")))
            // tick rates
            .addAction(path("ticks-per", "hopper-transfer"), move(path("tick-rates", "hopper-transfer")))
            .addAction(path("ticks-per", "hopper-check"), move(path("tick-rates", "hopper-check")))
            .addAction(path("hanging-tick-frequency"), move(path("tick-rates", "hanging-tick-frequency")))
            .addAction(path("max-tick-time"), remove())
            // fixes
            .addAction(path("max-tnt-per-tick"), move(path("fixes", "max-tnt-per-tick")))
            // misc
            .addAction(path("dragon-death-sound-radius"), move(path("misc", "sound-radius", "dragon-death")))
            .addAction(path("wither-spawn-sound-radius"), move(path("misc", "sound-radius", "wither-spawn")))
            .addAction(path("end-portal-sound-radius"), move(path("misc", "sound-radius", "end-portal")))
            // only ever controlled SpigotWorldConfig's own startup logging, which no longer exists
            .addAction(path("verbose"), remove())
            .addAction(path("below-zero-generation-in-existing-chunks"), remove()); // moved to the global config

        // entity-activation-range.* keeps its key names, the section is just renamed
        for (final String key : new String[]{
            "animals", "monsters", "raiders", "misc", "water", "villagers", "flying-monsters",
            "villagers-work-immunity-after", "villagers-work-immunity-for", "villagers-active-for-panic",
            "tick-inactive-villagers", "ignore-spectators"
        }) {
            builder.addAction(path("entity-activation-range", key), move(entities("activation-range", key)));
        }
        for (final String category : new String[]{"animals", "monsters", "villagers", "flying-monsters"}) {
            for (final String suffix : new String[]{"max-per-tick", "every", "for"}) {
                final String key = category + "-" + suffix;
                builder.addAction(
                    path("entity-activation-range", "wake-up-inactive", key),
                    move(entities("activation-range", "wake-up-inactive", key))
                );
            }
        }

        // growth.<plant>-modifier -> growth-modifiers.<plant>, dropping the suffix
        for (final String[] pair : new String[][]{
            {"cactus", "cactus"}, {"cane", "cane"}, {"melon", "melon"}, {"mushroom", "mushroom"},
            {"pumpkin", "pumpkin"}, {"sapling", "sapling"}, {"beetroot", "beetroot"}, {"carrot", "carrot"},
            {"potato", "potato"}, {"torchflower", "torch-flower"}, {"wheat", "wheat"}, {"netherwart", "nether-wart"},
            {"vine", "vine"}, {"cocoa", "cocoa"}, {"bamboo", "bamboo"}, {"sweetberry", "sweet-berry"},
            {"kelp", "kelp"}, {"twistingvines", "twisting-vines"}, {"weepingvines", "weeping-vines"},
            {"cavevines", "cave-vines"}, {"glowberry", "glow-berry"}, {"pitcherplant", "pitcher-plant"}
        }) {
            builder.addAction(path("growth", pair[0] + "-modifier"), move(path("growth-modifiers", pair[1])));
        }

        // hunger.* keeps its key names
        for (final String key : new String[]{
            "jump-walk-exhaustion", "jump-sprint-exhaustion", "combat-exhaustion", "regen-exhaustion",
            "swim-multiplier", "sprint-multiplier", "other-multiplier"
        }) {
            builder.addAction(path("hunger", key), move(path("hunger", key)));
        }

        // seed-<feature> -> seeds.<feature>
        for (final String[] pair : new String[][]{
            {"village", "village"}, {"desert", "desert"}, {"igloo", "igloo"}, {"jungle", "jungle"},
            {"swamp", "swamp"}, {"monument", "monument"}, {"shipwreck", "shipwreck"}, {"ocean", "ocean"},
            {"outpost", "outpost"}, {"endcity", "end-city"}, {"slime", "slime"}, {"nether", "nether"},
            {"mansion", "mansion"}, {"fossil", "fossil"}, {"portal", "portal"}, {"ancientcity", "ancient-city"},
            {"trailruins", "trail-ruins"}, {"trialchambers", "trial-chambers"}, {"buriedtreasure", "buried-treasure"},
            {"mineshaft", "mineshaft"}, {"stronghold", "stronghold"}
        }) {
            builder.addAction(path("seed-" + pair[0]), move(path("seeds", pair[1])));
        }

        return builder.build();
    }

    public static void pruneEmptyLegacySections(final ConfigurationNode node) {
        for (final String legacy : new String[]{
            "entity-activation-range", "entity-tracking-range", "growth", "merge-radius", "ticks-per", "max-tick-time"
        }) {
            final ConfigurationNode child = node.node(legacy);
            if (!child.virtual() && isEffectivelyEmpty(child)) {
                node.removeChild(legacy);
            }
        }
    }

    private static boolean isEffectivelyEmpty(final ConfigurationNode node) {
        if (node.isMap()) {
            return node.childrenMap().values().stream().allMatch(LegacySpigotWorldConfig::isEffectivelyEmpty);
        }
        return node.empty();
    }

    private static NodePath entities(final String... other) {
        return io.papermc.paper.configuration.transformation.Transformations.prefix("entities", other);
    }
}
