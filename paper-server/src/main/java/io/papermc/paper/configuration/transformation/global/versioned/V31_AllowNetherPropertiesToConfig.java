package io.papermc.paper.configuration.transformation.global.versioned;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.Properties;

public class V31_AllowNetherPropertiesToConfig implements ConfigurationTransformation {
    public static final V31_AllowNetherPropertiesToConfig INSTANCE = new V31_AllowNetherPropertiesToConfig();
    private static final int VERSION = 31;

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(VERSION, INSTANCE);
    }

    @Override
    public void apply(final ConfigurationNode root) throws ConfigurateException {
        final DedicatedServer server = ((DedicatedServer) MinecraftServer.getServer());
        final String raw = server.settings.getProperties().properties.getProperty("allow-nether");
        if (raw != null) {
            final ConfigurationNode node = root.node("misc", "enable-nether");
            node.set(raw.equals("true"));
            server.settings.update((config) -> {
                final Properties newProps = new Properties(config.properties);
                newProps.remove("allow-nether");
                return config.reload(server.registryAccess(), newProps, server.options);
            });
        }
    }
}
