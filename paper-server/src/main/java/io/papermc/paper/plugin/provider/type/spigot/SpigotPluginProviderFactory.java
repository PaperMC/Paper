package io.papermc.paper.plugin.provider.type.spigot;

import io.papermc.paper.plugin.provider.configuration.serializer.constraints.PluginConfigConstraints;
import io.papermc.paper.plugin.provider.type.PluginTypeFactory;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class SpigotPluginProviderFactory implements PluginTypeFactory<SpigotPluginProvider, PluginDescriptionFile> {

    @Override
    public SpigotPluginProvider build(JarFile file, PluginDescriptionFile configuration, Path source) throws InvalidDescriptionException {
        // Copied from SimplePluginManager#loadPlugins
        // Spigot doesn't validate the name when the config is created, and instead when the plugin is loaded.
        // Paper plugin configuration will do these checks in config serializer instead of when this is created.
        String name = configuration.getRawName();
        if (PluginConfigConstraints.RESERVED_KEYS.contains(name.toLowerCase(Locale.ROOT))) {
            throw new InvalidDescriptionException("Restricted name, cannot use %s as a plugin name.".formatted(name));
        } else if (name.indexOf(' ') != -1) {
            throw new InvalidDescriptionException("Restricted name, cannot use 0x20 (space character) in a plugin name.");
        }

        return new SpigotPluginProvider(source, file, configuration);
    }

    @Override
    public PluginDescriptionFile create(JarFile file, JarEntry config) throws InvalidDescriptionException {
        PluginDescriptionFile descriptionFile;
        try (InputStream inputStream = file.getInputStream(config)) {
            descriptionFile = new PluginDescriptionFile(inputStream);
        } catch (IOException | YAMLException ex) {
            throw new InvalidDescriptionException(ex);
        }

        return descriptionFile;
    }
}

