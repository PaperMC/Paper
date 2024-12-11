package io.papermc.paper.pluginremap;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * {@link PrintWriter}-backed logger implementation for use with {@link net.neoforged.art.api.Renamer} which
 * only opens the backing writer and logs messages when the {@link PluginRemapper#DEBUG_LOGGING} system property
 * is set to true.
 */
@DefaultQualifier(NonNull.class)
final class DebugLogger implements Consumer<String>, AutoCloseable {
    private final @Nullable PrintWriter writer;

    DebugLogger(final Path logFile) {
        try {
            this.writer = createWriter(logFile);
        } catch (final IOException ex) {
            throw new RuntimeException("Failed to initialize DebugLogger for file '" + logFile + "'", ex);
        }
    }

    @Override
    public void accept(final String line) {
        this.useWriter(writer -> writer.println(line));
    }

    @Override
    public void close() {
        this.useWriter(PrintWriter::close);
    }

    private void useWriter(final Consumer<PrintWriter> op) {
        final @Nullable PrintWriter writer = this.writer;
        if (writer != null) {
            op.accept(writer);
        }
    }

    Consumer<String> debug() {
        return line -> this.accept("[debug]: " + line);
    }

    static DebugLogger forOutputFile(final Path outputFile) {
        return new DebugLogger(outputFile.resolveSibling(outputFile.getFileName() + ".log"));
    }

    private static @Nullable PrintWriter createWriter(final Path logFile) throws IOException {
        if (!PluginRemapper.DEBUG_LOGGING) {
            return null;
        }
        if (!Files.exists(logFile.getParent())) {
            Files.createDirectories(logFile.getParent());
        }
        return new PrintWriter(logFile.toFile());
    }
}
