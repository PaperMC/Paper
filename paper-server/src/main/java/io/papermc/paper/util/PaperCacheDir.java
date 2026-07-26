package io.papermc.paper.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PaperCacheDir {

    private static final Path PATH = Path.of(".paper");

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
        if (Files.isRegularFile(path) && !Files.exists(target)) {
            try {
                Files.move(path, target);
            } catch (final IOException e) {
                throw new RuntimeException("Error moving file: " + child, e);
            }
        }
        return target;
    }
}
