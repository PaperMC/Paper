package io.papermc.paper.configuration.transformation.global.versioned;

import io.papermc.paper.configuration.Configuration;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public final class V32_ChunkLoadingBasicToWorld {
    private static final int VERSION = 32;
    private static final String CHUNK_LOADING_BASIC = "chunk-loading-basic";

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder, final YamlConfigurationLoader worldDefaultsLoader, final int worldConfigVersion) {
        builder.addVersion(VERSION, root -> {
            final ConfigurationNode chunkLoadingBasic = root.node(CHUNK_LOADING_BASIC);
            if (chunkLoadingBasic.virtual()) {
                return;
            }
            final ConfigurationNode worldDefaults = worldDefaultsLoader.load();
            if (worldDefaults.node(Configuration.VERSION_FIELD).virtual()) {
                worldDefaults.node(Configuration.VERSION_FIELD).set(worldConfigVersion);
            }
            worldDefaults.node(CHUNK_LOADING_BASIC).mergeFrom(chunkLoadingBasic);
            worldDefaultsLoader.save(worldDefaults);
            chunkLoadingBasic.raw(null);
        });
    }
}
