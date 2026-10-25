package io.papermc.paper.configuration;

import io.papermc.paper.configuration.Configurations.ContextMap;
import io.papermc.paper.configuration.transformation.ServerPropertiesAccess;
import java.util.Properties;
import java.util.function.Consumer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Normal
public class GlobalTransformationsTest {

    private static final class FakeServerProperties implements ServerPropertiesAccess {
        private DedicatedServerProperties current;

        private FakeServerProperties(final Properties properties) {
            this.current = new DedicatedServerProperties(properties, null);
        }

        @Override
        public DedicatedServerProperties current() {
            return this.current;
        }

        @Override
        public void update(final Consumer<Properties> editor) {
            final Properties copy = new Properties();
            copy.putAll(this.current.properties);
            editor.accept(copy);
            this.current = new DedicatedServerProperties(copy, null);
        }
    }

    private static ConfigurationNode node(final int version) throws SerializationException {
        final ConfigurationNode node = BasicConfigurationNode.root();
        node.node(Configuration.VERSION_FIELD).set(version);
        return node;
    }

    @Test
    public void missingContextFailsFullLoad() throws SerializationException {
        final ConfigurationNode node = node(30);
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> PaperConfigurations.applyGlobalTransformations(ContextMap.builder().build(), node));
        assertTrue(exception.getMessage().contains("server properties"), exception.getMessage());
    }

    @Test
    public void earlyLoadSkipsTransformationsMissingContext() throws Exception {
        final ConfigurationNode node = node(30);
        node.node("player-auto-save", "rate").set(5);
        PaperConfigurations.applyEarlyGlobalTransformations(node);

        assertEquals(5, node.node("players", "auto-save", "rate").getInt(), "transformations without requirements still run early");
        assertTrue(node.node("misc", "enable-nether").virtual(), "V31 requires server properties and must be skipped");
    }

    @Test
    public void serverPropertiesAreMigrated() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("allow-nether", "false");
        properties.setProperty("log-ips", "true");
        final FakeServerProperties serverProperties = new FakeServerProperties(properties);
        final ContextMap context = ContextMap.builder().put(Configurations.SERVER_PROPERTIES, serverProperties).build();

        final ConfigurationNode node = node(28);
        node.node("logging", "log-player-ip-addresses").set(false);
        PaperConfigurations.applyGlobalTransformations(context, node);

        assertFalse(serverProperties.current().logIPs, "V29 moves the option to server.properties");
        assertTrue(node.node("logging", "log-player-ip-addresses").virtual());
        assertFalse(node.node("misc", "enable-nether").getBoolean(true), "V31 moves the option from server.properties");
        assertFalse(serverProperties.current().properties.containsKey("allow-nether"));
        assertEquals(GlobalConfiguration.CURRENT_VERSION, node.node(Configuration.VERSION_FIELD).getInt());
    }
}
