package io.papermc.paper.configuration.transformation.global;

import io.papermc.paper.configuration.transformation.Transformations;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static io.papermc.paper.configuration.transformation.Transformations.move;
import static org.spongepowered.configurate.NodePath.path;
import static org.spongepowered.configurate.transformation.TransformAction.remove;

public final class LegacyBukkitConfig {

    private LegacyBukkitConfig() {
    }

    public static void migrate(final ConfigurationNode node) throws ConfigurateException {
        final ConfigurationNode legacy = node.copy();
        toNewFormat().apply(node);
        // world settings, handled by migrateWorldDefaults
        node.removeChild("spawn-limits");
        node.removeChild("ticks-per");
        Transformations.removeUnmigrated(legacy, node, "bukkit.yml", Set.of());
        Transformations.pruneEmptySections(node, "settings", "chunk-gc", "worlds");
    }

    public static ConfigurationTransformation toNewFormat() {
        return ConfigurationTransformation.builder()
            .addAction(settings("world-container"), move(path("misc", "world-container")))
            .addAction(settings("update-folder"), move(path("misc", "update-folder")))
            .addAction(settings("allow-end"), move(path("misc", "enable-end")))
            .addAction(settings("permissions-file"), move(path("misc", "permissions-file")))
            .addAction(settings("connection-throttle"), move(path("misc", "connection-throttle")))
            .addAction(settings("query-plugins"), move(path("misc", "query-plugins")))
            .addAction(settings("use-map-color-cache"), move(path("misc", "use-map-color-cache")))
            .addAction(settings("minimum-api"), move(path("misc", "minimum-api")))
            .addAction(settings("deprecated-verbose"), (nodePath, value) -> {
                // was "true"/"false"/"default", it is a WarningState now
                value.set(switch (value.getString("default").toLowerCase(Locale.ROOT)) {
                    case "true" -> "ON";
                    case "false" -> "OFF";
                    default -> "DEFAULT";
                });
                return path("misc", "deprecated-verbose").array();
            })
            .addAction(settings("shutdown-message"), move(LegacySpigotConfig.miniMessage(), path("messages", "shutdown")))
            .addAction(path("chunk-gc", "period-in-ticks"), move(path("chunk-system", "plugin-ticket-timeout")))
            .addAction(path("ticks-per", "autosave"), move(path("misc", "auto-save-interval")))
            .addAction(path("worlds", ConfigurationTransformation.WILDCARD_OBJECT, "generator"), intoGenerators())
            .addAction(path("worlds", ConfigurationTransformation.WILDCARD_OBJECT, "biome-provider"), intoGenerators())
            // dropped, nothing reads these
            .addAction(settings("warn-on-overload"), remove())
            .addAction(settings("plugin-profiling"), remove())
            .addAction(settings("compatibility"), remove()) // CraftServer#loadCompatibilities has been a no-op for years
            .build();
    }

    private static NodePath settings(final String key) {
        return path("settings", key);
    }

    public static ConfigurationTransformation worldDefaultsToNewFormat() {
        // Keys that belong to the world config; applied to a separate copy and merged into the world defaults
        return ConfigurationTransformation.builder()
            .addAction(path("spawn-limits", "monsters"), spawning("spawn-limits", "monster"))
            .addAction(path("spawn-limits", "animals"), spawning("spawn-limits", "creature"))
            .addAction(path("spawn-limits", "water-animals"), spawning("spawn-limits", "water_creature"))
            .addAction(path("spawn-limits", "water-ambient"), spawning("spawn-limits", "water_ambient"))
            .addAction(path("spawn-limits", "water-underground-creature"), spawning("spawn-limits", "underground_water_creature"))
            .addAction(path("spawn-limits", "axolotls"), spawning("spawn-limits", "axolotls"))
            .addAction(path("spawn-limits", "ambient"), spawning("spawn-limits", "ambient"))
            .addAction(path("ticks-per", "monster-spawns"), spawning("ticks-per-spawn", "monster"))
            .addAction(path("ticks-per", "animal-spawns"), spawning("ticks-per-spawn", "creature"))
            .addAction(path("ticks-per", "water-spawns"), spawning("ticks-per-spawn", "water_creature"))
            .addAction(path("ticks-per", "water-ambient-spawns"), spawning("ticks-per-spawn", "water_ambient"))
            .addAction(path("ticks-per", "water-underground-creature-spawns"), spawning("ticks-per-spawn", "underground_water_creature"))
            .addAction(path("ticks-per", "axolotl-spawns"), spawning("ticks-per-spawn", "axolotls"))
            .addAction(path("ticks-per", "ambient-spawns"), spawning("ticks-per-spawn", "ambient"))
            .addAction(path("ticks-per", "autosave"), move(path("chunks", "auto-save-interval")))
            .build();
    }

    // Expects a copy of the whole bukkit.yml, keeps only what belongs in the world defaults
    public static void migrateWorldDefaults(final ConfigurationNode node) throws ConfigurateException {
        worldDefaultsToNewFormat().apply(node);
        for (final Object key : List.copyOf(node.childrenMap().keySet())) {
            if (!"entities".equals(key) && !"chunks".equals(key)) {
                node.removeChild(key);
            }
        }
    }

    private static TransformAction spawning(final String section, final String category) {
        return (nodePath, value) -> {
            if (value.getInt(0) < 0) { // meant the vanilla value, which is the default now
                value.raw(null);
                return null;
            }
            return path("entities", "spawning", section, category).array();
        };
    }

    private static TransformAction intoGenerators() {
        return (nodePath, value) -> new Object[]{"world-generators", nodePath.get(1), nodePath.get(2)};
    }
}
