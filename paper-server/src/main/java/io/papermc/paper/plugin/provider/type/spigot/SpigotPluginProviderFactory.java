package io.papermc.paper.plugin.provider.type.spigot;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import io.papermc.paper.plugin.bootstrap.PluginProviderContextImpl;
import io.papermc.paper.plugin.entrypoint.classloader.BytecodeModifyingURLClassLoader;
import io.papermc.paper.plugin.entrypoint.classloader.PaperSimplePluginClassLoader;
import io.papermc.paper.plugin.loader.PaperClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.provider.configuration.serializer.constraints.PluginConfigConstraints;
import io.papermc.paper.plugin.provider.type.PluginTypeFactory;
import io.papermc.paper.plugin.provider.util.ProviderUtil;
import io.papermc.paper.util.MappingEnvironment;
import java.util.List;
import java.util.logging.Logger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.LibraryLoader;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class SpigotPluginProviderFactory implements PluginTypeFactory<SpigotPluginProvider, PluginDescriptionFile> {

    static {
        if (!MappingEnvironment.DISABLE_PLUGIN_REMAPPING) {
			LibraryLoader.LIBRARY_LOADER_FACTORY = BytecodeModifyingURLClassLoader::new;
		}
    }

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

        final List<Path> paperLibraryPaths;
        if (configuration.getPaperPluginLoader() != null) {
            final Logger logger = PaperPluginLogger.getLogger(configuration);
            PaperClasspathBuilder builder = new PaperClasspathBuilder(PluginProviderContextImpl.create(
                configuration, ComponentLogger.logger(logger.getName()), source
            ));

            try (
                PaperSimplePluginClassLoader simplePluginClassLoader = new PaperSimplePluginClassLoader(source, file, configuration, this.getClass().getClassLoader())
            ) {
                PluginLoader loader = ProviderUtil.loadClass(configuration.getPaperPluginLoader(), PluginLoader.class, simplePluginClassLoader);
                loader.classloader(builder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            paperLibraryPaths = builder.buildLibraryPaths(false);
        } else {
            paperLibraryPaths = null;
        }

        return new SpigotPluginProvider(source, file, configuration, paperLibraryPaths);
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

