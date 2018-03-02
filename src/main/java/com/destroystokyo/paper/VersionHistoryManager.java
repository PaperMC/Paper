package com.destroystokyo.paper;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum VersionHistoryManager {
    INSTANCE;

    private final Gson gson = new Gson();

    private final Logger logger = Bukkit.getLogger();

    private VersionData currentData = null;

    VersionHistoryManager() {
        final Path path = Paths.get("version_history.json");

        if (Files.exists(path)) {
            // Basic file santiy checks
            if (!Files.isRegularFile(path)) {
                if (Files.isDirectory(path)) {
                    logger.severe(path + " is a directory, cannot be used for version history");
                } else {
                    logger.severe(path + " is not a regular file, cannot be used for version history");
                }
                // We can't continue
                return;
            }

            try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                currentData = gson.fromJson(reader, VersionData.class);
            } catch (final IOException e) {
                logger.log(Level.SEVERE, "Failed to read version history file '" + path + "'", e);
                return;
            } catch (final JsonSyntaxException e) {
                logger.log(Level.SEVERE, "Invalid json syntax for file '" + path + "'", e);
                return;
            }

            final String version = Bukkit.getVersion();
            if (version == null) {
                logger.severe("Failed to retrieve current version");
                return;
            }

            if (!version.equals(currentData.getCurrentVersion())) {
                // The version appears to have changed
                currentData.setOldVersion(currentData.getCurrentVersion());
                currentData.setCurrentVersion(version);
                writeFile(path);
            }
        } else {
            // File doesn't exist, start fresh
            currentData = new VersionData();
            // oldVersion is null
            currentData.setCurrentVersion(Bukkit.getVersion());
            writeFile(path);
        }
    }

    private void writeFile(@Nonnull final Path path) {
        try (final BufferedWriter writer = Files.newBufferedWriter(
            path,
            StandardCharsets.UTF_8,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )) {
            gson.toJson(currentData, writer);
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Failed to write to version history file", e);
        }
    }

    @Nullable
    public VersionData getVersionData() {
        return currentData;
    }

    public static class VersionData {
        private String oldVersion;

        private String currentVersion;

        @Nullable
        public String getOldVersion() {
            return oldVersion;
        }

        public void setOldVersion(@Nullable String oldVersion) {
            this.oldVersion = oldVersion;
        }

        @Nullable
        public String getCurrentVersion() {
            return currentVersion;
        }

        public void setCurrentVersion(@Nullable String currentVersion) {
            this.currentVersion = currentVersion;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("oldVersion", oldVersion)
                .add("currentVersion", currentVersion)
                .toString();
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final VersionData versionData = (VersionData) o;
            return Objects.equals(oldVersion, versionData.oldVersion) &&
                Objects.equals(currentVersion, versionData.currentVersion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(oldVersion, currentVersion);
        }
    }
}
