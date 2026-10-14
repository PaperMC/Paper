package io.papermc.paper.util;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;

public final class PaperCacheDir {

    private static final Path PATH = Path.of(".paper");
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Path get() {
        if (!Files.exists(PATH)) {
            try {
                Files.createDirectories(PATH);
            } catch (final IOException e) {
                throw new RuntimeException("Error creating .paper cache dir", e);
            }
        }
        if (!Files.isDirectory(PATH)) {
            throw new RuntimeException(".paper cache dir is not a directory");
        }
        return PATH;
    }

    public static Path get(final String child) {
        return get().resolve(child);
    }

    public static Path moveFromServerRootAndGet(final String child, final String newName) {
        // Keep this for individual use until a more unified migration place is made for larger config migrations
        final Path path = Path.of(child);
        final Path target = get(newName);
        if (!Files.isRegularFile(path)) {
            return target;
        }

        if (Files.exists(target)) {
            // Delete the one in the server root
            try {
                Files.deleteIfExists(path);
            } catch (final IOException e) {
                LOGGER.error("Error deleting file: {}", child, e);
            }
        } else {
            // Move into .paper
            try {
                Files.move(path, target);
            } catch (final IOException e) {
                LOGGER.error("Error moving file: {}", child, e);
            }
        }
        return target;
    }
}
