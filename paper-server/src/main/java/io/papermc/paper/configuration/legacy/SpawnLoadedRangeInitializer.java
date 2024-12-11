package io.papermc.paper.configuration.legacy;

import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
import org.spongepowered.configurate.util.NamingSchemes;

public final class SpawnLoadedRangeInitializer implements NodeResolver {

    private final String name;
    private final SpigotWorldConfig spigotConfig;

    public SpawnLoadedRangeInitializer(String name, SpigotWorldConfig spigotConfig) {
        this.name = name;
        this.spigotConfig = spigotConfig;
    }

    @Override
    public ConfigurationNode resolve(ConfigurationNode parent) {
        final String key = NamingSchemes.LOWER_CASE_DASHED.coerce(this.name);
        final ConfigurationNode node = parent.node(key);
        if (node.virtual()) {
            node.raw(Math.min(spigotConfig.viewDistance, 10));
        }
        return node;
    }
}
