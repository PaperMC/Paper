package io.papermc.paper.configuration.transformation.world.versioned;

import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;
import static org.spongepowered.configurate.transformation.TransformAction.rename;

/**
 * The {@code filter-nbt-data-from-spawn-eggs-and-related} setting had nothing
 * to do with spawn eggs, and was just filtering bad falling blocks.
 */
public final class V30_RenameFilterNbtFromSpawnEgg {

    private static final int VERSION = 30;
    private static final NodePath OLD_PATH = path("entities", "spawning", "filter-nbt-data-from-spawn-eggs-and-related");
    private static final String NEW_PATH = "filter-bad-tile-entity-nbt-from-falling-blocks";

    private V30_RenameFilterNbtFromSpawnEgg() {
    }

    public static void apply(ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(VERSION, ConfigurationTransformation.builder().addAction(OLD_PATH, rename(NEW_PATH)).build());
    }
}
