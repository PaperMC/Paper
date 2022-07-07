package io.papermc.paper.plugin.entrypoint.classloader;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.entrypoint.classloader.group.PaperPluginClassLoaderStorage;
import io.papermc.paper.plugin.provider.classloader.PaperClassLoaderStorage;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * This is similar to a {@link org.bukkit.plugin.java.PluginClassLoader} but is completely kept hidden from the api.
 * This is only used with Paper plugins.
 *
 * @see PaperPluginClassLoaderStorage
 */
public class PaperPluginClassLoader extends PaperSimplePluginClassLoader implements ConfiguredPluginClassLoader {

    static {
        registerAsParallelCapable();
    }

    private final URLClassLoader libraryLoader;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Logger logger;
    @Nullable
    private JavaPlugin loadedJavaPlugin;
    @Nullable
    private PluginClassLoaderGroup group;

    public PaperPluginClassLoader(Logger logger, Path source, JarFile file, PaperPluginMeta configuration, ClassLoader parentLoader, URLClassLoader libraryLoader) throws IOException {
        super(source, file, configuration, parentLoader);
        this.libraryLoader = libraryLoader;

        this.logger = logger;
        if (this.configuration().hasOpenClassloader()) {
            this.group = PaperClassLoaderStorage.instance().registerOpenGroup(this);
        }
    }

    private PaperPluginMeta configuration() {
        return (PaperPluginMeta) this.configuration;
    }

    public void refreshClassloaderDependencyTree(DependencyContext dependencyContext) {
         if (this.configuration().hasOpenClassloader()) {
             return;
         }
         if (this.group != null) {
             // We need to unregister the classloader inorder to allow for dependencies
             // to be recalculated
             PaperClassLoaderStorage.instance().unregisterClassloader(this);
         }

        this.group = PaperClassLoaderStorage.instance().registerAccessBackedGroup(this, (classLoader) -> {
            return dependencyContext.isTransitiveDependency(PaperPluginClassLoader.this.configuration, classLoader.getConfiguration());
        });
    }

    @Override
    public URL getResource(String name) {
        URL resource = findResource(name);
        if (resource == null && this.libraryLoader != null) {
            return this.libraryLoader.getResource(name);
        }
        return resource;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> resources = new ArrayList<>();
        this.addEnumeration(resources, this.findResources(name));
        if (this.libraryLoader != null) {
            addEnumeration(resources, this.libraryLoader.getResources(name));
        }
        return Collections.enumeration(resources);
    }

    private <T> void addEnumeration(List<T> list, Enumeration<T> enumeration) {
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return this.loadClass(name, resolve, true, true);
    }

    @Override
    public PluginMeta getConfiguration() {
        return this.configuration;
    }

    @Override
    public Class<?> loadClass(@NotNull String name, boolean resolve, boolean checkGroup, boolean checkLibraries) throws ClassNotFoundException {
        try {
            Class<?> result = super.loadClass(name, resolve);

            // SPIGOT-6749: Library classes will appear in the above, but we don't want to return them to other plugins
            if (checkGroup || result.getClassLoader() == this) {
                return result;
            }
        } catch (ClassNotFoundException ignored) {
        }

        if (checkLibraries) {
            try {
                return this.libraryLoader.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }

        if (checkGroup) {
            // This ignores the libraries of other plugins, unless they are transitive dependencies.
            if (this.group == null) {
                throw new IllegalStateException("Tried to resolve class while group was not yet initialized");
            }

            Class<?> clazz = this.group.getClassByName(name, resolve, this);
            if (clazz != null) {
                return clazz;
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    public void init(JavaPlugin plugin) {
        PluginMeta config = this.configuration;
        PluginDescriptionFile pluginDescriptionFile = new PluginDescriptionFile(
            config.getName(),
            config.getName(),
            config.getProvidedPlugins(),
            config.getMainClass(),
            "", // Classloader load order api
            config.getPluginDependencies(), // Dependencies
            config.getPluginSoftDependencies(), // Soft Depends
            config.getLoadBeforePlugins(), // Load Before
            config.getVersion(),
            Map.of(), // Commands, we use a separate system
            config.getDescription(),
            config.getAuthors(),
            config.getContributors(),
            config.getWebsite(),
            config.getLoggerPrefix(),
            config.getLoadOrder(),
            config.getPermissions(),
            config.getPermissionDefault(),
            Set.of(), // Aware api
            config.getAPIVersion(),
            List.of() // Libraries
        );

        File dataFolder = new File(Bukkit.getPluginsFolder(), pluginDescriptionFile.getName());

        plugin.init(Bukkit.getServer(), pluginDescriptionFile, dataFolder, this.source.toFile(), this, config, this.logger);

        this.loadedJavaPlugin = plugin;
    }

    @Nullable
    @Override
    public JavaPlugin getPlugin() {
        return this.loadedJavaPlugin;
    }

    @Override
    public String toString() {
        return "PaperPluginClassLoader{" +
            "libraryLoader=" + this.libraryLoader +
            ", seenIllegalAccess=" + this.seenIllegalAccess +
            ", loadedJavaPlugin=" + this.loadedJavaPlugin +
            '}';
    }

    @Override
    public void close() throws IOException {
        try (this.jar; this.libraryLoader) {
            super.close();
        }
    }

    @Override
    public @Nullable PluginClassLoaderGroup getGroup() {
        return this.group;
    }
}
