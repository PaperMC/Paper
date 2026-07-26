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
}
