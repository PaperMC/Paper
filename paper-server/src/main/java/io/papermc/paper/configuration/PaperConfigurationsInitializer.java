package io.papermc.paper.configuration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.transformation.global.LegacyPaperConfig;
import io.papermc.paper.configuration.transformation.global.LegacySpigotConfig;
import io.papermc.paper.configuration.transformation.world.LegacyPaperWorldConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import joptsimple.OptionSet;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static com.google.common.base.Preconditions.checkState;
import static io.papermc.paper.configuration.PaperConfigurations.GLOBAL_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.WORLD_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.WORLD_DEFAULTS_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.createDirectoriesSymlinkAware;

public final class PaperConfigurationsInitializer {

    static final String WORLD_CONTAINER_SETTING = "settings.world-container";
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private static final String BACKUP_DIR ="legacy-backup";

    private static final String MOVED_NOTICE = """
        The global and world default configuration files have moved to %s
        and the world-specific configuration file has been moved inside
        the respective world folder.
        
        See https://docs.papermc.io/paper/configuration for more information.
        """;

    public static PaperConfigurations create(final OptionSet options) throws Exception {
        final Path legacyBukkitConfigPath = ((File) options.valueOf("bukkit-settings")).toPath();
        final Path legacySpigotConfigPath = ((File) options.valueOf("spigot-settings")).toPath();
        final Path legacyPaperConfig = ((File) options.valueOf("paper-settings")).toPath();
        final Path configDirPath = ((File) options.valueOf("paper-settings-directory")).toPath();
        final Path worldFolder;
        if (options.has("universe")) {
            worldFolder = ((File) options.valueOf("universe")).toPath();
        } else if (Files.exists(legacyBukkitConfigPath)) {
            final YamlConfiguration legacyConfig = loadLegacyConfigFile(legacyBukkitConfigPath);
            worldFolder = new File(legacyConfig.getString("settings.world-container", ".")).toPath();
        } else {
            final YamlConfiguration preloadedPaperGlobalConfig = loadLegacyConfigFile(configDirPath.resolve(GLOBAL_CONFIG_FILE_NAME));
            worldFolder = new File(preloadedPaperGlobalConfig.getString("initialization.world-container", ".")).toPath();
        }
        convertLegacy(legacyPaperConfig, configDirPath, worldFolder, legacySpigotConfigPath, legacyBukkitConfigPath);
        return PaperConfigurations.setup(configDirPath);
    }

    private static void convertLegacy(final Path legacyPaperConfig, final Path configDir, final Path worldFolder, final Path legacySpigotConfig, final Path legacyBukkitConfig) throws Exception {
        if (needsConverting(legacyPaperConfig)) {
            final String legacyFileName = legacyPaperConfig.getFileName().toString();
            try {
                final Path legacyConfigBackup = makeConfigFileBackup(legacyPaperConfig, configDir);
                convertLegacyPaperConfig(legacyConfigBackup, configDir, worldFolder, legacySpigotConfig);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyFileName + "' to the new configuration format", ex);
            }
        }
        if (needsConverting(legacySpigotConfig)) {
            final String legacyFileName = legacySpigotConfig.getFileName().toString();
            try {
                final Path legacyConfigBackup = makeConfigFileBackup(legacySpigotConfig, configDir);
                convertLegacySpigotConfig(legacyConfigBackup, configDir);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyFileName + "' to the new configuration format", ex);
            }

        }
    }

    private static Path makeConfigFileBackup(final Path legacyConfigPath, final Path configDir) throws IOException {
        final Path backupDir = configDir.resolve(BACKUP_DIR);
        if (Files.exists(backupDir) && !Files.isDirectory(backupDir)) {
            throw new RuntimeException("Paper needs to create a '" + BACKUP_DIR + "' directory in the '" + configDir.toAbsolutePath() + "' folder. You already have a non-directory named '" + BACKUP_DIR + "'. Please remove it and restart the server.");
        }
        final Path legacyFileName = legacyConfigPath.getFileName();
        final String backupFileName = legacyFileName + ".old";
        final Path legacyConfigBackup = backupDir.resolve(backupFileName);
        if (Files.exists(legacyConfigBackup) && !Files.isRegularFile(legacyConfigBackup)) {
            throw new RuntimeException("Paper needs to create a '" + backupFileName + "' file in the '" + backupDir.toAbsolutePath() + "' folder. You already have a non-file named '" + backupFileName + "'. Please remove it and restart the server.");
        }
        Files.move(legacyConfigPath.toRealPath(), legacyConfigBackup, StandardCopyOption.REPLACE_EXISTING); // make backup
        if (Files.isSymbolicLink(legacyConfigPath)) {
            Files.delete(legacyConfigPath);
        }
        final Path realLegacyPath = Files.isSymbolicLink(legacyConfigPath) ? Files.readSymbolicLink(legacyConfigPath) : legacyConfigPath;
        final Path replacementFile = realLegacyPath.resolveSibling(legacyFileName + "-README.txt");
        if (Files.notExists(replacementFile)) {
            Files.createFile(replacementFile);
            Files.writeString(replacementFile, String.format(MOVED_NOTICE, configDir.toAbsolutePath()));
        }
        return legacyConfigBackup;
    }

    private static void convertLegacyPaperConfig(final Path legacyConfig, final Path configDir, final Path worldFolder, final Path spigotConfig) throws Exception {
        createDirectoriesSymlinkAware(configDir);

        final YamlConfigurationLoader legacyLoader = ConfigurationLoaders.naturallySortedWithoutHeader(legacyConfig);
        final YamlConfigurationLoader globalLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(GLOBAL_CONFIG_FILE_NAME));
        final YamlConfigurationLoader worldDefaultsLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(WORLD_DEFAULTS_CONFIG_FILE_NAME));

        final ConfigurationNode legacy = legacyLoader.load();
        checkState(!legacy.virtual(), "can't be virtual");
        final int version = legacy.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).getInt();

        final ConfigurationNode legacyWorldSettings = legacy.node("world-settings").copy();
        checkState(!legacyWorldSettings.virtual(), "can't be virtual");
        legacy.removeChild("world-settings");

        // Apply legacy transformations before settings flatten
        final YamlConfiguration spigotConfiguration = loadLegacyConfigFile(spigotConfig); // needs to change spigot config values in this transformation
        LegacyPaperConfig.transformation(spigotConfiguration).apply(legacy);
        spigotConfiguration.save(Files.newBufferedWriter(spigotConfig, StandardCharsets.UTF_8));
        legacy.mergeFrom(legacy.node("settings")); // flatten "settings" to root
        legacy.removeChild("settings");
        LegacyPaperConfig.toNewFormat().apply(legacy);
        globalLoader.save(legacy); // save converted node to new global location

        final ConfigurationNode worldDefaults = legacyWorldSettings.node("default").copy();
        checkState(!worldDefaults.virtual());
        worldDefaults.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).raw(version);
        legacyWorldSettings.removeChild("default");
        LegacyPaperWorldConfig.transformation().apply(worldDefaults);
        LegacyPaperWorldConfig.toNewFormat().apply(worldDefaults);
        worldDefaultsLoader.save(worldDefaults);

        legacyWorldSettings.childrenMap().forEach((world, legacyWorldNode) -> {
            try {
                legacyWorldNode.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).raw(version);
                LegacyPaperWorldConfig.transformation().apply(legacyWorldNode);
                LegacyPaperWorldConfig.toNewFormat().apply(legacyWorldNode);
                ConfigurationLoaders.naturallySortedWithoutHeader(worldFolder.resolve(world.toString()).resolve(WORLD_CONFIG_FILE_NAME)).save(legacyWorldNode); // save converted node to new location
            } catch (final ConfigurateException ex) {
                LOGGER.error("Failed to convert legacy world config", ex);
            }
        });
    }

    private static void convertLegacySpigotConfig(final Path legacyConfig, final Path configDir) throws Exception {
        createDirectoriesSymlinkAware(configDir);

        final YamlConfigurationLoader legacyLoader = ConfigurationLoaders.naturallySortedWithoutHeader(legacyConfig);
        final YamlConfigurationLoader globalLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(GLOBAL_CONFIG_FILE_NAME));
        final YamlConfigurationLoader worldDefaultsLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(WORLD_DEFAULTS_CONFIG_FILE_NAME));

        final ConfigurationNode legacy = legacyLoader.load();
        checkState(!legacy.virtual(), "can't be virtual");
        final int version = legacy.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).getInt();

        final ConfigurationNode legacyWorldSettings = legacy.node("world-settings").copy();
        checkState(!legacyWorldSettings.virtual(), "can't be virtual");
        legacy.removeChild("world-settings");

        // Apply legacy transformations new format change
        LegacySpigotConfig.transformation().apply(legacy);
        LegacySpigotConfig.toNewFormat().apply(legacy);
        final ConfigurationNode global = globalLoader.load();
        global.mergeFrom(legacy); // merge converted spigot global into main global
        globalLoader.save(global); // save converted node to new global location
    }

    private static boolean needsConverting(final Path legacyConfig) {
        return Files.exists(legacyConfig) && Files.isRegularFile(legacyConfig);
    }

    private static YamlConfiguration loadLegacyConfigFile(final Path configFile) throws Exception {
        final YamlConfiguration config = new YamlConfiguration();
        if (Files.exists(configFile)) {
            try {
                config.load(Files.newBufferedReader(configFile, StandardCharsets.UTF_8));
            } catch (final Exception ex) {
                throw new Exception("Failed to load configuration file: " + configFile.toAbsolutePath(), ex);
            }
        }
        return config;
    }

    private PaperConfigurationsInitializer() {}
}
