package io.papermc.paper.plugin.provider.type.spigot;

import com.destroystokyo.paper.util.SneakyThrow;
import com.destroystokyo.paper.utils.PaperPluginLogger;
import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.DependencyContextHolder;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.type.PluginTypeFactory;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.LibraryLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpigotPluginProvider implements PluginProvider<JavaPlugin>, ProviderStatusHolder, DependencyContextHolder {

    public static final PluginTypeFactory<SpigotPluginProvider, PluginDescriptionFile> FACTORY = new SpigotPluginProviderFactory();
    private static final LibraryLoader LIBRARY_LOADER = new LibraryLoader(Logger.getLogger("SpigotLibraryLoader"));
    private final Path path;
    private final PluginDescriptionFile description;
    private final JarFile jarFile;
    private final Logger logger;
    private final List<Path> paperLibraryPaths;
    private final ComponentLogger componentLogger;
    private ProviderStatus status;
    private DependencyContext dependencyContext;

    SpigotPluginProvider(Path path, JarFile file, PluginDescriptionFile description, List<Path> paperLibraryPaths) {
        this.path = path;
        this.jarFile = file;
        this.description = description;
        this.logger = PaperPluginLogger.getLogger(description);
        this.paperLibraryPaths = paperLibraryPaths;
        this.componentLogger = ComponentLogger.logger(this.logger.getName());
    }

    @Override
    public @NotNull Path getSource() {
        return this.path;
    }

    @Override
    public JarFile file() {
        return this.jarFile;
    }

    @Override
    public JavaPlugin createInstance() {
        Server server = Bukkit.getServer();
        try {

            final File parentFile = server.getPluginsFolder(); // Paper
            final File dataFolder = new File(parentFile, this.description.getName());
            @SuppressWarnings("deprecation") final File oldDataFolder = new File(parentFile, this.description.getRawName());

            // Found old data folder
            if (dataFolder.equals(oldDataFolder)) {
                // They are equal -- nothing needs to be done!
            } else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
                server.getLogger().warning(String.format(
                    "While loading %s (%s) found old-data folder: `%s' next to the new one `%s'",
                    this.description.getFullName(),
                    this.path,
                    oldDataFolder,
                    dataFolder
                ));
            } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
                if (!oldDataFolder.renameTo(dataFolder)) {
                    throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
                }
                server.getLogger().log(Level.INFO, String.format(
                    "While loading %s (%s) renamed data folder: `%s' to `%s'",
                    this.description.getFullName(),
                    this.path,
                    oldDataFolder,
                    dataFolder
                ));
            }

            if (dataFolder.exists() && !dataFolder.isDirectory()) {
                throw new InvalidPluginException(String.format(
                    "Projected datafolder: `%s' for %s (%s) exists and is not a directory",
                    dataFolder,
                    this.description.getFullName(),
                    this.path
                ));
            }

            Set<String> missingHardDependencies = new HashSet<>(this.description.getDepend().size()); // Paper - list all missing hard depends
            for (final String pluginName : this.description.getDepend()) {
                if (!this.dependencyContext.hasDependency(pluginName)) {
                    missingHardDependencies.add(pluginName); // Paper - list all missing hard depends
                }
            }
            // Paper start - list all missing hard depends
            if (!missingHardDependencies.isEmpty()) {
                throw new UnknownDependencyException(missingHardDependencies, this.description.getFullName());
            }
            // Paper end

            server.getUnsafe().checkSupported(this.description);

            final PluginClassLoader loader;
            try {
                loader = new PluginClassLoader(this.getClass().getClassLoader(), this.description, dataFolder, this.path.toFile(), LIBRARY_LOADER.createLoader(this.description, this.paperLibraryPaths), this.jarFile, this.dependencyContext); // Paper
            } catch (InvalidPluginException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new InvalidPluginException(ex);
            }

            // Override dependency context.
            // We must provide a temporary context in order to properly handle dependencies on the plugin classloader constructor.
            loader.dependencyContext = PaperPluginManagerImpl.getInstance();


            this.status = ProviderStatus.INITIALIZED;
            return loader.getPlugin();
        } catch (Throwable ex) {
            this.status = ProviderStatus.ERRORED;
            SneakyThrow.sneaky(ex);
        }

        throw new AssertionError(); // Shouldn't happen
    }

    @Override
    public PluginDescriptionFile getMeta() {
        return this.description;
    }

    @Override
    public ComponentLogger getLogger() {
        return this.componentLogger;
    }

    @Override
    public LoadOrderConfiguration createConfiguration(@NotNull Map<String, PluginProvider<?>> toLoad) {
        return new SpigotLoadOrderConfiguration(this, toLoad);
    }

    @Override
    public List<String> validateDependencies(@NotNull DependencyContext context) {
        List<String> missingDependencies = new ArrayList<>();
        for (String hardDependency : this.getMeta().getPluginDependencies()) {
            if (!context.hasDependency(hardDependency)) {
                missingDependencies.add(hardDependency);
            }
        }

        return missingDependencies;
    }

    @Override
    public ProviderStatus getLastProvidedStatus() {
        return this.status;
    }

    @Override
    public void setStatus(ProviderStatus status) {
        this.status = status;
    }

    @Override
    public void setContext(DependencyContext context) {
        this.dependencyContext = context;
    }

    @Override
    public String toString() {
        return "SpigotPluginProvider{" +
            "path=" + path +
            ", description=" + description +
            ", jarFile=" + jarFile +
            ", status=" + status +
            ", dependencyContext=" + dependencyContext +
            '}';
    }
}
