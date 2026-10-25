package io.papermc.paper.configuration;

import io.papermc.paper.configuration.yaml.BlankLineStyle;
import io.papermc.paper.configuration.yaml.NodeStyle;
import io.papermc.paper.configuration.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.util.MapFactories;

public final class ConfigurationLoaders {
    private ConfigurationLoaders() {
    }

    public static YamlConfigurationLoader.Builder naturallySorted() {
        return YamlConfigurationLoader.builder()
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
            .headerMode(HeaderMode.PRESET)
            .commentsEnabled(true)
            .blankLineStyle(BlankLineStyle.AFTER_NESTED)
            .defaultOptions(options -> options.mapFactory(MapFactories.sortedNatural()));
    }

    public static YamlConfigurationLoader naturallySortedWithoutHeader(final Path path) {
        return naturallySorted()
            .headerMode(HeaderMode.NONE)
            .path(path)
            .build();
    }
}
