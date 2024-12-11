package io.papermc.paper.plugin.provider.source;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

public class FileArrayProviderSource implements ProviderSource<File[], List<Path>> {

    public static final FileArrayProviderSource INSTANCE = new FileArrayProviderSource();
    private static final FileProviderSource FILE_PROVIDER_SOURCE = new FileProviderSource("File '%s'"::formatted);
    private static final Logger LOGGER = LogUtils.getClassLogger();

    @Override
    public List<Path> prepareContext(File[] context) {
        final List<Path> files = new ArrayList<>();
        for (File file : context) {
            try {
                files.add(FILE_PROVIDER_SOURCE.prepareContext(file.toPath()));
            } catch (IllegalArgumentException ignored) {
                // Ignore illegal argument exceptions from jar checking
            } catch (final Exception e) {
                LOGGER.error("Error preparing plugin context: " + e.getMessage(), e);
            }
        }
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
}
