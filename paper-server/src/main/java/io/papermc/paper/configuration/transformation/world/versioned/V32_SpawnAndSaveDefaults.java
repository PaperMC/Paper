package io.papermc.paper.configuration.transformation.world.versioned;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

/**
 * The spawn limits, spawn intervals and save interval used to defer to {@code bukkit.yml} via {@code -1},
 * or {@code default} for the save interval. Move those into the Paper configs directly.
 */
public final class V32_SpawnAndSaveDefaults {

    private static final int VERSION = 32;

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder) {
        final ConfigurationTransformation.Builder transformation = ConfigurationTransformation.builder()
            .addAction(path("chunks", "auto-save-interval"), (nodePath, value) -> {
                if ("default".equals(value.getString())) {
                    value.raw(null);
                }
                return null;
            });
        for (final String section : new String[]{"spawn-limits", "ticks-per-spawn"}) {
            transformation.addAction(
                path("entities", "spawning", section, ConfigurationTransformation.WILDCARD_OBJECT),
                dropNegativeSentinel()
            );
        }
        builder.addVersion(VERSION, transformation.build());
    }

    private static TransformAction dropNegativeSentinel() {
        return (nodePath, value) -> {
            if (value.getInt(0) < 0) {
                value.raw(null);
            }
            return null;
        };
    }

    private V32_SpawnAndSaveDefaults() {
    }
}
