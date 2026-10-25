package io.papermc.paper.configuration.transformation.global.versioned;

import io.papermc.paper.configuration.Configurations;
import io.papermc.paper.configuration.Configurations.ContextKey;
import io.papermc.paper.configuration.Configurations.ContextMap;
import io.papermc.paper.configuration.transformation.ContextualTransformation;
import io.papermc.paper.configuration.transformation.ServerPropertiesAccess;
import java.util.Set;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

public final class V31_AllowNetherPropertiesToConfig implements ContextualTransformation {

    public static final V31_AllowNetherPropertiesToConfig INSTANCE = new V31_AllowNetherPropertiesToConfig();
    private static final int VERSION = 31;

    private V31_AllowNetherPropertiesToConfig() {
    }

    @Override
    public int version() {
        return VERSION;
    }

    @Override
    public Set<ContextKey<?>> requiredContext() {
        return Set.of(Configurations.SERVER_PROPERTIES);
    }

    @Override
    public ConfigurationTransformation create(final ContextMap context) {
        final ServerPropertiesAccess serverProperties = context.require(Configurations.SERVER_PROPERTIES);
        return root -> {
            final String raw = serverProperties.current().properties.getProperty("allow-nether");
            if (raw != null) {
                final ConfigurationNode node = root.node("misc", "enable-nether");
                node.set(raw.equals("true"));
                serverProperties.update(properties -> properties.remove("allow-nether"));
            }
        };
    }
}
