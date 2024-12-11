package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import java.nio.file.Path;
import java.util.ArrayList;
import org.slf4j.Logger;

import java.util.List;

/**
 * Registers providers at the provided files in the add-plugin argument.
 */
public class PluginFlagProviderSource implements ProviderSource<List<Path>, List<Path>> {

    public static final PluginFlagProviderSource INSTANCE = new PluginFlagProviderSource();
    private static final FileProviderSource FILE_PROVIDER_SOURCE = new FileProviderSource("File '%s' specified through 'add-plugin' argument"::formatted, false);
    private static final Logger LOGGER = LogUtils.getClassLogger();

    @Override
    public List<Path> prepareContext(List<Path> context) {
        final List<Path> files = new ArrayList<>();
        for (Path path : context) {
            try {
                files.add(FILE_PROVIDER_SOURCE.prepareContext(path));
            } catch (Exception e) {
                LOGGER.error("Error preparing plugin context: " + e.getMessage(), e);
            }
        }
        // Paper start - Remap plugins
        if (io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper != null && !files.isEmpty()) {
            return io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper.rewriteExtraPlugins(files);
        }
        // Paper end - Remap plugins
        return files;
    }

    @Override
    public void registerProviders(EntrypointHandler entrypointHandler, List<Path> context) {
        for (Path path : context) {
            try {
                FILE_PROVIDER_SOURCE.registerProviders(entrypointHandler, path);
            } catch (Exception e) {
                LOGGER.error("Error loading plugin: " + e.getMessage(), e);
            }
        }
    }
}
