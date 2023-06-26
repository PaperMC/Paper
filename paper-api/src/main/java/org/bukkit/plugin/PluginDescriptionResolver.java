package org.bukkit.plugin;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

final class PluginDescriptionResolver extends Resolver {

    @Override
    protected void addImplicitResolvers() {
        addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
        // addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789."); // Don't resolve floats. Preserve strings - SPIGOT-7370
        addImplicitResolver(Tag.INT, INT, "-+0123456789");
        addImplicitResolver(Tag.MERGE, MERGE, "<");
        addImplicitResolver(Tag.NULL, NULL, "~nN\0");
        addImplicitResolver(Tag.NULL, EMPTY, null);
        addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
    }
}
