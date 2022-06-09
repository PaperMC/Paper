package io.papermc.paper.configuration.transformation.world.versioned;

import io.papermc.paper.configuration.Configurations;
import net.minecraft.world.level.GameRules;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public final class V31_SpawnLoadedRangeToGameRule implements TransformAction {

    private static final int VERSION = 31;
    private static final String SPAWN = "spawn";
    private static final String KEEP_SPAWN_LOADED_RANGE = "keep-spawn-loaded-range";
    private static final String KEEP_SPAWN_LOADED = "keep-spawn-loaded";

    private final GameRules gameRules;
    private final ConfigurationNode defaultsNode;

    private V31_SpawnLoadedRangeToGameRule(final GameRules gameRules, final ConfigurationNode defaultsNode) {
        this.gameRules = gameRules;
        this.defaultsNode = defaultsNode;
    }

    @Override
    public Object @Nullable [] visitPath(final NodePath path, final ConfigurationNode value) {
        final ConfigurationNode worldSpawnNode = value.node(SPAWN);
        final ConfigurationNode worldLoadedNode = worldSpawnNode.node(KEEP_SPAWN_LOADED);
        final boolean keepLoaded = worldLoadedNode.getBoolean(this.defaultsNode.node(SPAWN, KEEP_SPAWN_LOADED).getBoolean());
        worldLoadedNode.raw(null);
        final ConfigurationNode worldRangeNode = worldSpawnNode.node(KEEP_SPAWN_LOADED_RANGE);
        final int range = worldRangeNode.getInt(this.defaultsNode.node(SPAWN, KEEP_SPAWN_LOADED_RANGE).getInt());
        worldRangeNode.raw(null);
        if (worldSpawnNode.empty()) {
            worldSpawnNode.raw(null);
        }
        if (!keepLoaded) {
            this.gameRules.getRule(GameRules.RULE_SPAWN_CHUNK_RADIUS).set(0, null);
        } else {
            this.gameRules.getRule(GameRules.RULE_SPAWN_CHUNK_RADIUS).set(range, null);
        }
        return null;
    }

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder, final Configurations.ContextMap contextMap, final @Nullable ConfigurationNode defaultsNode) {
        if (defaultsNode != null) {
            builder.addVersion(VERSION, ConfigurationTransformation.builder().addAction(path(), new V31_SpawnLoadedRangeToGameRule(contextMap.require(Configurations.GAME_RULES), defaultsNode)).build());
        } else {
            builder.addVersion(VERSION, ConfigurationTransformation.empty()); // increment version of default world config
        }
    }
}
