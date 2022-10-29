package io.papermc.paper.pluginremap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import io.papermc.paper.util.Hashing;
import io.papermc.paper.util.MappingEnvironment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.slf4j.Logger;
import org.spongepowered.configurate.loader.AtomicFiles;

@DefaultQualifier(NonNull.class)
class RemappedPluginIndex {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    private static final String INDEX_FILE_NAME = "index.json";

    protected final State state;
    private final Path dir;
    private final Path indexFile;
    private final boolean handleDuplicateFileNames;

    // todo maybe hash remapped variants to ensure they haven't changed? probably unneeded
    static final class State {
        final Map<String, String> hashes = new HashMap<>();
        final Set<String> skippedHashes = new HashSet<>();
        private final String mappingsHash = MappingEnvironment.mappingsHash();
    }

    RemappedPluginIndex(final Path dir, final boolean handleDuplicateFileNames) {
        this.dir = dir;
        this.handleDuplicateFileNames = handleDuplicateFileNames;
        if (!Files.exists(this.dir)) {
            try {
                Files.createDirectories(this.dir);
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        this.indexFile = dir.resolve(INDEX_FILE_NAME);
        if (Files.isRegularFile(this.indexFile)) {
            try {
                this.state = this.readIndex();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.state = new State();
        }
    }

    private State readIndex() throws IOException {
        final State state;
        try (final BufferedReader reader = Files.newBufferedReader(this.indexFile)) {
            state = GSON.fromJson(reader, State.class);
        }

        // If mappings have changed, delete all cached files and create a new index
        if (!state.mappingsHash.equals(MappingEnvironment.mappingsHash())) {
            for (final String fileName : state.hashes.values()) {
                Files.deleteIfExists(this.dir.resolve(fileName));
            }
            return new State();
        }
        return state;
    }

    Path dir() {
        return this.dir;
    }

    /**
     * Returns a list of cached paths if all of the input paths are present in the cache.
     * The returned list may contain paths from different directories.
     *
     * @param paths plugin jar paths to check
     * @return null if any of the paths are not present in the cache, otherwise a list of the cached paths
     */
    @Nullable List<Path> getAllIfPresent(final List<Path> paths) {
        final Map<Path, String> hashCache = new HashMap<>();
        final Function<Path, String> inputFileHash = path -> hashCache.computeIfAbsent(path, Hashing::sha256);

        // Delete cached entries we no longer need
        final Iterator<Map.Entry<String, String>> iterator = this.state.hashes.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            final String inputHash = entry.getKey();
            final String fileName = entry.getValue();
            if (paths.stream().anyMatch(path -> inputFileHash.apply(path).equals(inputHash))) {
                // Hash is used, keep it
                continue;
            }

            iterator.remove();
            try {
                Files.deleteIfExists(this.dir.resolve(fileName));
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Also clear hashes of skipped files
        this.state.skippedHashes.removeIf(hash -> paths.stream().noneMatch(path -> inputFileHash.apply(path).equals(hash)));

        final List<Path> ret = new ArrayList<>();
        for (final Path path : paths) {
            final String inputHash = inputFileHash.apply(path);
            if (this.state.skippedHashes.contains(inputHash)) {
                // Add the original path
                ret.add(path);
                continue;
            }

            final @Nullable Path cached = this.getIfPresent(inputHash);
            if (cached == null) {
                // Missing the remapped file
                return null;
            }
            ret.add(cached);
        }
        return ret;
    }

    private String createCachedFileName(final Path in) {
        if (this.handleDuplicateFileNames) {
            final String fileName = in.getFileName().toString();
            final int i = fileName.lastIndexOf(".jar");
            return fileName.substring(0, i) + "-" + System.currentTimeMillis() + ".jar";
        }
        return in.getFileName().toString();
    }

    /**
     * Returns the given path if the file was previously skipped for being remapped, otherwise the cached path or null.
     *
     * @param in input file
     * @return {@code in} if already remapped, the cached path if present, otherwise null
     */
    @Nullable Path getIfPresent(final Path in) {
        final String inHash = Hashing.sha256(in);
        if (this.state.skippedHashes.contains(inHash)) {
            return in;
        }
        return this.getIfPresent(inHash);
    }

    /**
     * Returns the cached path if a remapped file is present for the given hash, otherwise null.
     *
     * @param inHash hash of the input file
     * @return the cached path if present, otherwise null
     * @see #getIfPresent(Path)
     */
    protected @Nullable Path getIfPresent(final String inHash) {
        final @Nullable String fileName = this.state.hashes.get(inHash);
        if (fileName == null) {
            return null;
        }

        final Path path = this.dir.resolve(fileName);
        if (Files.exists(path)) {
            return path;
        }
        return null;
    }

    Path input(final Path in) {
        return this.input(in, Hashing.sha256(in));
    }

    /**
     * Marks the given file as skipped for remapping.
     *
     * @param in input file
     */
    void skip(final Path in) {
        this.state.skippedHashes.add(Hashing.sha256(in));
    }

    protected Path input(final Path in, final String hashString) {
        final String name = this.createCachedFileName(in);
        this.state.hashes.put(hashString, name);
        return this.dir.resolve(name);
    }

    void write() {
        try (final BufferedWriter writer = AtomicFiles.atomicBufferedWriter(this.indexFile, StandardCharsets.UTF_8)) {
            GSON.toJson(this.state, writer);
        } catch (final IOException ex) {
            LOGGER.warn("Failed to write index file '{}'", this.indexFile, ex);
        }
    }
}
