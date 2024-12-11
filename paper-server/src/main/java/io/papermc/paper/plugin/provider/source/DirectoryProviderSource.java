package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 * Loads all plugin providers in the given directory.
 */
public class DirectoryProviderSource implements ProviderSource<Path, List<Path>> {

    public static final DirectoryProviderSource INSTANCE = new DirectoryProviderSource();
    private static final FileProviderSource FILE_PROVIDER_SOURCE = new FileProviderSource("Directory '%s'"::formatted, false); // Paper - Remap plugins
    private static final Logger LOGGER = LogUtils.getClassLogger();

    @Override
    public List<Path> prepareContext(Path context) throws IOException {
        // Symlink happy, create file if missing.
        if (!Files.isDirectory(context)) {
            Files.createDirectories(context);
        }

        final List<Path> files = new ArrayList<>();
        this.walkFiles(context, path -> {
            try {
                files.add(FILE_PROVIDER_SOURCE.prepareContext(path));
            } catch (IllegalArgumentException ignored) {
                // Ignore illegal argument exceptions from jar checking
            } catch (final Exception e) {
                LOGGER.error("Error preparing plugin context: " + e.getMessage(), e);
            }
        });
        // Paper start - Remap plugins
        if (io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper != null) {
            return io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper.rewritePluginDirectory(files);
        }
        // Paper end - Remap plugins
        return files;
    }

    @Override
    public void registerProviders(EntrypointHandler entrypointHandler, List<Path> context) {
        for (Path path : context) {
            try {
                FILE_PROVIDER_SOURCE.registerProviders(entrypointHandler, path);
            } catch (IllegalArgumentException ignored) {
                // Ignore illegal argument exceptions from jar checking
            } catch (Exception e) {
                LOGGER.error("Error loading plugin: " + e.getMessage(), e);
            }
        }
    }

    private void walkFiles(Path context, Consumer<Path> consumer) throws IOException {
        Files.walk(context, 1, FileVisitOption.FOLLOW_LINKS)
            .filter(this::isValidFile)
            .forEach(consumer);
    }

    public boolean isValidFile(Path path) {
        // Avoid loading plugins that start with a dot
        return Files.isRegularFile(path) && !path.startsWith(".");
    }
}
