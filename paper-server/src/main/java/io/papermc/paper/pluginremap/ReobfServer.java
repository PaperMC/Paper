package io.papermc.paper.pluginremap;

import com.mojang.logging.LogUtils;
import io.papermc.paper.util.AtomicFiles;
import io.papermc.paper.util.MappingEnvironment;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.neoforged.art.api.Renamer;
import net.neoforged.art.api.Transformer;
import net.neoforged.art.internal.RenamerImpl;
import net.neoforged.srgutils.IMappingFile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.slf4j.Logger;

import static io.papermc.paper.pluginremap.InsertManifestAttribute.addNamespaceManifestAttribute;

@DefaultQualifier(NonNull.class)
final class ReobfServer {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final Path remapClasspathDir;
    private final CompletableFuture<Void> load;

    ReobfServer(final Path remapClasspathDir, final CompletableFuture<IMappingFile> mappings, final Executor executor) {
        this.remapClasspathDir = remapClasspathDir;
        if (this.mappingsChanged()) {
            this.load = mappings.thenAcceptAsync(this::remap, executor);
        } else {
            if (PluginRemapper.DEBUG_LOGGING) {
				LOGGER.info("Have cached reobf server for current mappings.");
			}
            this.load = CompletableFuture.completedFuture(null);
        }
    }

    CompletableFuture<Path> remapped() {
        return this.load.thenApply($ -> this.remappedPath());
    }

    private Path remappedPath() {
        return this.remapClasspathDir.resolve(MappingEnvironment.mappingsHash() + ".jar");
    }

    private boolean mappingsChanged() {
        return !Files.exists(this.remappedPath());
    }

    private void remap(final IMappingFile mappings) {
        try {
            if (!Files.exists(this.remapClasspathDir)) {
                Files.createDirectories(this.remapClasspathDir);
            }
            for (final Path file : PluginRemapper.list(this.remapClasspathDir, Files::isRegularFile)) {
                Files.delete(file);
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

        LOGGER.info("Remapping server...");
        final long startRemap = System.currentTimeMillis();
        try (final DebugLogger log = DebugLogger.forOutputFile(this.remappedPath())) {
            AtomicFiles.atomicWrite(this.remappedPath(), writeTo -> {
                try (final RenamerImpl renamer = (RenamerImpl) Renamer.builder()
                    .logger(log)
                    .debug(log.debug())
                    .threads(1)
                    .add(Transformer.renamerFactory(mappings, false))
                    .add(addNamespaceManifestAttribute(InsertManifestAttribute.SPIGOT_NAMESPACE))
                    .build()) {
                    renamer.run(serverJar().toFile(), writeTo.toFile(), true);
                }
            });
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to remap server jar", ex);
        }
        LOGGER.info("Done remapping server in {}ms.", System.currentTimeMillis() - startRemap);
    }

    private static Path serverJar() {
        try {
            return Path.of(ReobfServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (final URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
