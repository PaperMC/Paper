package io.papermc.paper.configuration.transformation.global.versioned;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public class V30_SpawnMonsters implements TransformAction {

    private static final int VERSION = 30;
    private static final NodePath UNSUPPORTED_PATH = path("unsupported-settings");
    private static final NodePath PATH = path("use-legacy-spawn-monsters-behavior");
    private static final V30_SpawnMonsters INSTANCE = new V30_SpawnMonsters();

    private V30_SpawnMonsters() {}

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(VERSION, ConfigurationTransformation.builder().addAction(UNSUPPORTED_PATH, INSTANCE).build());
    }

    @Override
    public Object @Nullable [] visitPath(final NodePath path, final ConfigurationNode value) {
        final DedicatedServer server = ((DedicatedServer) MinecraftServer.getServer());
        final ConfigurationNode legacyNode = value.node(PATH);
        if (!server.settings.getProperties().spawnMonsters && legacyNode.virtual()) {
            legacyNode.raw(true);
        }
        return null;
    }
}
