package io.papermc.paper.configuration.transformation;

import java.util.Properties;
import java.util.function.Consumer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;

/**
 * Access to {@code server.properties} for transformations moving options between it and the Paper configs.
 */
public interface ServerPropertiesAccess {

    DedicatedServerProperties current();

    /**
     * Edits a copy of the raw properties, then reloads and saves {@code server.properties} from it.
     *
     * @param editor edits the raw properties
     */
    void update(Consumer<Properties> editor);

    static ServerPropertiesAccess of(final DedicatedServer server) {
        return new ServerPropertiesAccess() {
            @Override
            public DedicatedServerProperties current() {
                return server.settings.getProperties();
            }

            @Override
            public void update(final Consumer<Properties> editor) {
                server.settings.update(config -> {
                    // same as Settings#cloneProperties, used by Settings.MutableValue#update
                    final Properties properties = new Properties();
                    properties.putAll(config.properties);
                    editor.accept(properties);
                    return config.reload(server.registryAccess(), properties, server.options);
                });
            }
        };
    }
}
