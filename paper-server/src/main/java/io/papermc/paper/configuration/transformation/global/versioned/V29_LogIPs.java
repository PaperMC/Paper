package io.papermc.paper.configuration.transformation.global.versioned;

import io.papermc.paper.configuration.Configurations;
import io.papermc.paper.configuration.Configurations.ContextKey;
import io.papermc.paper.configuration.Configurations.ContextMap;
import io.papermc.paper.configuration.transformation.ContextualTransformation;
import io.papermc.paper.configuration.transformation.ServerPropertiesAccess;
import java.util.Set;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

public final class V29_LogIPs implements ContextualTransformation {

    public static final V29_LogIPs INSTANCE = new V29_LogIPs();
    private static final int VERSION = 29;
    private static final NodePath PATH = path("logging", "log-player-ip-addresses");

    private V29_LogIPs() {
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
        return ConfigurationTransformation.builder().addAction(PATH, (path, value) -> {
            final boolean val = value.getBoolean(serverProperties.current().logIPs);
            serverProperties.update(properties -> properties.setProperty("log-ips", String.valueOf(val)));

            value.raw(null);

            return null;
        }).build();
    }
}
