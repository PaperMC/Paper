package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.SparksFly;
import io.papermc.paper.plugin.PluginInitializerManager;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import io.papermc.paper.plugin.provider.type.PluginFileType;
import org.bukkit.plugin.InvalidPluginException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarFile;
import org.slf4j.Logger;

/**
 * Loads a plugin provider at the given plugin jar file path.
 */
public class FileProviderSource implements ProviderSource<Path, Path> {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private final Function<Path, String> contextChecker;
    private final boolean applyRemap;

    public FileProviderSource(Function<Path, String> contextChecker, boolean applyRemap) {
        this.contextChecker = contextChecker;
        this.applyRemap = applyRemap;
    }

    public FileProviderSource(Function<Path, String> contextChecker) {
        this(contextChecker, true);
    }

    @Override
    public Path prepareContext(Path context) throws IOException {
        String source = this.contextChecker.apply(context);

        if (Files.notExists(context)) {
            throw new IllegalArgumentException(source + " does not exist, cannot load a plugin from it!");
        }

        if (!Files.isRegularFile(context)) {
            throw new IllegalArgumentException(source + " is not a file, cannot load a plugin from it!");
        }

        if (!context.getFileName().toString().endsWith(".jar")) {
            throw new IllegalArgumentException(source + " is not a jar file, cannot load a plugin from it!");
        }

        try {
            context = this.checkUpdate(context);
        } catch (Exception exception) {
            throw new RuntimeException(source + " failed to update!", exception);
        }
        // Paper start - Remap plugins
        if (this.applyRemap && io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper != null) {
            context = io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper.rewritePlugin(context);
        }
        // Paper end - Remap plugins
        return context;
    }

    @Override
    public void registerProviders(EntrypointHandler entrypointHandler, Path context) throws Exception {
        String source = this.contextChecker.apply(context);

        JarFile file = new JarFile(context.toFile(), true, JarFile.OPEN_READ, JarFile.runtimeVersion());
        PluginFileType<?, ?> type = PluginFileType.guessType(file);
        if (type == null) {
            // Throw IAE wrapped in RE to prevent callers from considering this a "invalid parameter" as caller ignores IAE.
            // TODO: This needs some heavy rework, using illegal argument exception to signal an actual failure is less than ideal.
            if (file.getEntry("META-INF/versions.list") != null) {
                throw new RuntimeException(new IllegalArgumentException(context + " appears to be a server jar! Server jars do not belong in the plugin folder."));
            }

            throw new RuntimeException(
                new IllegalArgumentException(source + " does not contain a " + String.join(" or ", PluginFileType.getConfigTypes()) + "! Could not determine plugin type, cannot load a plugin from it!")
            );
        }

        final PluginMeta config = type.getConfig(file);
        if ((config.getName().equals("spark") && config.getMainClass().equals("me.lucko.spark.bukkit.BukkitSparkPlugin")) && !SparksFly.isPluginPreferred()) {
            LOGGER.info("The spark plugin will not be loaded as this server bundles the spark profiler.");
            return;
        }

        type.register(entrypointHandler, file, context);
    }

    /**
     * Replaces a plugin with a plugin of the same plugin name in the update folder.
     *
     * @param file The plugin jar file to look for updates for.
     */
    private Path checkUpdate(Path file) throws InvalidPluginException {
        PluginInitializerManager pluginSystem = PluginInitializerManager.instance();
        Path updateDirectory = pluginSystem.pluginUpdatePath();
        if (updateDirectory == null || !Files.isDirectory(updateDirectory)) {
            return file;
        }

        try {
            String pluginName = this.getPluginName(file);
            UpdateFileVisitor visitor = new UpdateFileVisitor(pluginName);
            Files.walkFileTree(updateDirectory, Set.of(), 1, visitor);
            if (visitor.getValidPlugin() != null) {
                Path updateLocation = visitor.getValidPlugin();

                try {
                    Files.copy(updateLocation, file, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException exception) {
                    throw new RuntimeException("Could not copy '" + updateLocation + "' to '" + file + "' in update plugin process", exception);
                }

                // Rename the plugin file to the update file's name.
                final Path renamedFile = file.resolveSibling(updateLocation.getFileName());
                try {
                    Files.move(file, renamedFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException exception) {
                    throw new RuntimeException("Could not rename '" + file + "' to '" + renamedFile + "' in update plugin process", exception);
                }

                // Delete the file from the update folder now that it's copied over successfully
                try {
                    Files.delete(updateLocation);
                } catch (IOException exception) {
                    throw new RuntimeException("Could not delete '" + updateLocation + "' from update folder in update plugin process", exception);
                }

                return renamedFile;
            }
        } catch (Exception e) {
            throw new InvalidPluginException(e);
        }
        return file;
    }

    private String getPluginName(Path path) throws Exception {
        try (JarFile file = new JarFile(path.toFile())) {
            PluginFileType<?, ?> type = PluginFileType.guessType(file);
            if (type == null) {
                throw new IllegalArgumentException(path + " does not contain a " + String.join(" or ", PluginFileType.getConfigTypes()) + "! Could not determine plugin type, cannot load a plugin from it!");
            }

            return type.getConfig(file).getName();
        }
    }

    private class UpdateFileVisitor implements FileVisitor<Path> {

        private final String targetName;
        @Nullable
        private Path validPlugin;

        private UpdateFileVisitor(String targetName) {
            this.targetName = targetName;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            try {
                String updatePluginName = FileProviderSource.this.getPluginName(file);
                if (this.targetName.equals(updatePluginName)) {
                    this.validPlugin = file;
                    return FileVisitResult.TERMINATE;
                }
            } catch (Exception e) {
                // We failed to load this data for some reason, so, we'll skip over this
            }


            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Nullable
        public Path getValidPlugin() {
            return validPlugin;
        }
    }
}
