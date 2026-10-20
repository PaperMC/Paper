package io.papermc.paper.configuration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.transformation.global.LegacyBukkitConfig;
import io.papermc.paper.configuration.transformation.global.LegacyPaperConfig;
import io.papermc.paper.configuration.transformation.global.LegacySpigotConfig;
import io.papermc.paper.configuration.transformation.world.LegacyPaperWorldConfig;
import io.papermc.paper.configuration.transformation.world.LegacySpigotWorldConfig;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import joptsimple.OptionSet;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static com.google.common.base.Preconditions.checkState;
import static io.papermc.paper.configuration.PaperConfigurations.GLOBAL_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.WORLD_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.WORLD_DEFAULTS_CONFIG_FILE_NAME;
import static io.papermc.paper.configuration.PaperConfigurations.createDirectoriesSymlinkAware;

public final class PaperConfigurationsInitializer {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private static final String BACKUP_DIR ="legacy-backup";

    private static final String MOVED_NOTICE = """
        The global and world default configuration files have moved to %s
        and the world-specific configuration file has been moved inside
        the respective world folder.

        See https://docs.papermc.io/paper/configuration for more information.
        """;

    // Also read from contexts that never actually boot a server
    private static InitializationConfiguration earlyInitialization = new InitializationConfiguration();

    /**
     * Returns the {@code initialization} section of the global config.
     * <p>
     * Before {@code DedicatedServer#initServer} deserializes the full {@link GlobalConfiguration}
     * this returns the temporary instance loaded by {@link #load(OptionSet)}.
     * After that, it returns the section from the fully loaded global config.
     *
     * @return initialization section of the global config
     */
    public static InitializationConfiguration initialization() {
        final GlobalConfiguration global = GlobalConfiguration.get();
        if (global != null && global.initialization != null) {
            return global.initialization;
        }
        return earlyInitialization;
    }

    /**
     * Runs the early configuration load for the {@link #initialization()} data.
     *
     * @param options startup options
     * @return init config
     */
    public static InitializationConfiguration load(final OptionSet options) throws IOException {
        final Path configDirPath = ((File) options.valueOf("paper-settings-directory")).toPath();
        final InitializationConfiguration modernConfig = loadInitializationConfiguration(configDirPath.resolve(GLOBAL_CONFIG_FILE_NAME));
        final InitializationConfiguration configuration = modernConfig != null ? modernConfig : new InitializationConfiguration();
        if (modernConfig == null) {
            // Migration only comes later, so we need to manually look into legacy files
            final Path legacyBukkitConfigPath = ((File) options.valueOf("bukkit-settings")).toPath();
            if (Files.isRegularFile(legacyBukkitConfigPath)) {
                final YamlConfiguration bukkit = loadLegacyConfigFile(legacyBukkitConfigPath);
                configuration.worldContainer = bukkit.getString("settings.world-container", configuration.worldContainer);
                configuration.updateFolder = bukkit.getString("settings.update-folder", configuration.updateFolder);
            }

            final Path legacySpigotConfigPath = ((File) options.valueOf("spigot-settings")).toPath();
            if (Files.isRegularFile(legacySpigotConfigPath)) {
                final YamlConfiguration spigot = loadLegacyConfigFile(legacySpigotConfigPath);
                if (spigot.contains("advancements.disabled")) {
                    configuration.disabledAdvancements = Set.copyOf(spigot.getStringList("advancements.disabled"));
                }
            }
        }

        if (options.has("universe")) { // the launch argument always wins
            configuration.worldContainer = ((File) options.valueOf("universe")).getPath();
        }

        earlyInitialization = configuration;
        return configuration;
    }

    static @Nullable InitializationConfiguration loadInitializationConfiguration(final Path globalConfigFile) throws ConfigurateException {
        if (Files.notExists(globalConfigFile)) {
            return null;
        }

        final ConfigurationNode node = PaperConfigurations.createRegistryFreeGlobalLoader(globalConfigFile).load();
        final ConfigurationNode version = node.node(Configuration.VERSION_FIELD);
        if (version.virtual()) { // See Configurations#verifyGlobalConfigVersion
            version.raw(GlobalConfiguration.CURRENT_VERSION);
        }

        PaperConfigurations.applyGlobalTransformations(node);

        final ConfigurationNode initNode = node.node("initialization");
        return initNode.virtual() ? null : initNode.require(InitializationConfiguration.class);
    }

    public static PaperConfigurations create(final OptionSet options) throws Exception {
        final Path legacyBukkitConfigPath = ((File) options.valueOf("bukkit-settings")).toPath();
        final Path legacySpigotConfigPath = ((File) options.valueOf("spigot-settings")).toPath();
        final Path legacyPaperConfig = ((File) options.valueOf("paper-settings")).toPath();
        final Path configDirPath = ((File) options.valueOf("paper-settings-directory")).toPath();
        final Path worldFolder = new File(initialization().worldContainer).toPath();
        convertLegacy(legacyPaperConfig, configDirPath, worldFolder, legacySpigotConfigPath, legacyBukkitConfigPath,
            ((File) options.valueOf("commands-settings")).toPath(), worldFolder.resolve(levelName(options)));
        return PaperConfigurations.setup(configDirPath);
    }

    private static void convertLegacy(final Path legacyPaperConfig, final Path configDir, final Path worldFolder, final Path legacySpigotConfig, final Path legacyBukkitConfig, final Path legacyCommandsConfig, final Path levelFolder) throws Exception {
        if (needsConverting(legacyPaperConfig)) {
            final String legacyFileName = legacyPaperConfig.getFileName().toString();
            try {
                final Path legacyConfigBackup = makeConfigFileBackup(legacyPaperConfig, configDir);
                convertLegacyPaperConfig(legacyConfigBackup, configDir, worldFolder, legacySpigotConfig);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyFileName + "' to the new configuration format", ex);
            }
        }
        if (needsConverting(legacyBukkitConfig)) {
            try {
                final Path legacyConfigBackup = makeConfigFileBackup(legacyBukkitConfig, configDir);
                convertLegacyBukkitConfig(legacyConfigBackup, configDir);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyBukkitConfig.getFileName() + "' to the new configuration format", ex);
            }
        }
        if (needsConverting(legacySpigotConfig)) {
            final String legacyFileName = legacySpigotConfig.getFileName().toString();
            try {
                final Path legacyConfigBackup = makeConfigFileBackup(legacySpigotConfig, configDir);
                convertLegacySpigotConfig(legacyConfigBackup, configDir, levelFolder);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyFileName + "' to the new configuration format", ex);
            }

        }
        if (needsConverting(legacyCommandsConfig)) {
            try {
                convertLegacyCommandsConfig(makeConfigFileBackup(legacyCommandsConfig, configDir), configDir);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyCommandsConfig.getFileName() + "' to the new configuration format", ex);
            }
        }
    }

    private static Path makeConfigFileBackup(final Path legacyConfigPath, final Path configDir) throws IOException {
        if (Files.exists(configDir) && !Files.isDirectory(configDir)) {
            throw new RuntimeException("Paper needs to create a '" + configDir.toAbsolutePath() + "' folder. You already have a non-directory named '" + configDir.toAbsolutePath() + "'. Please remove it and restart the server.");
        }

        final Path backupDir = configDir.resolve(BACKUP_DIR);
        if (Files.exists(backupDir) && !Files.isDirectory(backupDir)) {
            throw new RuntimeException("Paper needs to create a '" + BACKUP_DIR + "' directory in the '" + configDir.toAbsolutePath() + "' folder. You already have a non-directory named '" + BACKUP_DIR + "'. Please remove it and restart the server.");
        }

        createDirectoriesSymlinkAware(backupDir);

        final Path legacyFileName = legacyConfigPath.getFileName();
        final String backupFileName = legacyFileName + ".old";
        final Path legacyConfigBackup = backupDir.resolve(backupFileName);
        if (Files.exists(legacyConfigBackup) && !Files.isRegularFile(legacyConfigBackup)) {
            throw new RuntimeException("Paper needs to create a '" + backupFileName + "' file in the '" + backupDir.toAbsolutePath() + "' folder. You already have a non-file named '" + backupFileName + "'. Please remove it and restart the server.");
        }
        // resolve the symlink before the move, the link is gone by the time the README is placed
        final boolean symlink = Files.isSymbolicLink(legacyConfigPath);
        final Path realLegacyPath = symlink ? Files.readSymbolicLink(legacyConfigPath) : legacyConfigPath;
        Files.move(legacyConfigPath.toRealPath(), legacyConfigBackup, StandardCopyOption.REPLACE_EXISTING); // make backup
        if (symlink) {
            Files.delete(legacyConfigPath);
        }
        final Path replacementFile = realLegacyPath.resolveSibling(legacyFileName + "-README.txt");
        if (Files.notExists(replacementFile)) {
            Files.createFile(replacementFile);
            Files.writeString(replacementFile, String.format(MOVED_NOTICE, configDir.toAbsolutePath()));
        }
        return legacyConfigBackup;
    }

    private static void convertLegacyBukkitConfig(final Path legacyConfig, final Path configDir) throws Exception {
        createDirectoriesSymlinkAware(configDir);

        final YamlConfigurationLoader globalLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(GLOBAL_CONFIG_FILE_NAME));
        final ConfigurationNode global = globalLoader.load();
        final ConfigurationNode legacy = ConfigurationLoaders.naturallySortedWithoutHeader(legacyConfig).load();

        // spawn limits, spawn intervals and the save interval are world settings now
        final ConfigurationNode worldBound = legacy.copy();
        LegacyBukkitConfig.migrateWorldDefaults(worldBound);
        if (!worldBound.empty()) {
            final YamlConfigurationLoader worldDefaultsLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(WORLD_DEFAULTS_CONFIG_FILE_NAME));
            final ConfigurationNode worldDefaults = worldDefaultsLoader.load();
            overwriteDeferredValues(worldBound, worldDefaults);
            worldDefaultsLoader.save(worldDefaults);
        }

        LegacyBukkitConfig.migrate(legacy);
        global.mergeFrom(legacy);
        globalLoader.save(global);
        LOGGER.info("Converted bukkit.yml into {}", GLOBAL_CONFIG_FILE_NAME);
    }

    private static void convertLegacyCommandsConfig(final Path legacyConfig, final Path configDir) throws Exception {
        final ConfigurationNode legacy = ConfigurationLoaders.naturallySortedWithoutHeader(legacyConfig).load();
        LegacyBukkitConfig.migrateCommands(legacy);

        final YamlConfigurationLoader globalLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(GLOBAL_CONFIG_FILE_NAME));
        final ConfigurationNode global = globalLoader.load();
        global.mergeFrom(legacy);
        globalLoader.save(global);
        LOGGER.info("Converted commands.yml into {}", GLOBAL_CONFIG_FILE_NAME);
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

    private static void convertLegacySpigotConfig(final Path legacyConfig, final Path configDir, final Path levelFolder) throws Exception {
        createDirectoriesSymlinkAware(configDir);

        final YamlConfigurationLoader legacyLoader = ConfigurationLoaders.naturallySortedWithoutHeader(legacyConfig);
        final YamlConfigurationLoader globalLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(GLOBAL_CONFIG_FILE_NAME));
        final YamlConfigurationLoader worldDefaultsLoader = ConfigurationLoaders.naturallySortedWithoutHeader(configDir.resolve(WORLD_DEFAULTS_CONFIG_FILE_NAME));

        final ConfigurationNode legacy = legacyLoader.load();
        checkState(!legacy.virtual(), "can't be virtual");
        final int version = legacy.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).getInt();

        final ConfigurationNode legacyWorldSettings = legacy.node("world-settings").copy();
        legacy.removeChild("world-settings");

        // Apply legacy transformations new format change
        LegacySpigotConfig.migrate(legacy);
        final ConfigurationNode global = globalLoader.load();
        global.mergeFrom(legacy); // merge converted spigot global into main global
        liftBelowZeroGeneration(legacyWorldSettings, global);
        globalLoader.save(global); // save converted node to new global location

        if (legacyWorldSettings.virtual()) {
            return;
        }

        final ConfigurationNode worldDefaults = legacyWorldSettings.node("default").copy();
        legacyWorldSettings.removeChild("default");
        if (!worldDefaults.virtual()) {
            worldDefaults.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).raw(version);
            LegacySpigotWorldConfig.migrate(worldDefaults);
            final ConfigurationNode defaults = worldDefaultsLoader.load();
            defaults.mergeFrom(worldDefaults);
            worldDefaultsLoader.save(defaults);
        }

        legacyWorldSettings.childrenMap().forEach((world, legacyWorldNode) -> {
            final String key = world.toString();
            try {
                final Path worldConfigDir = resolveWorldConfigDirectory(levelFolder, key);
                legacyWorldNode.node(Configuration.LEGACY_CONFIG_VERSION_FIELD).raw(version);
                LegacySpigotWorldConfig.migrate(legacyWorldNode);
                final YamlConfigurationLoader worldLoader = ConfigurationLoaders.naturallySortedWithoutHeader(worldConfigDir.resolve(WORLD_CONFIG_FILE_NAME));
                final ConfigurationNode worldNode = worldLoader.load();
                worldNode.mergeFrom(legacyWorldNode);
                createDirectoriesSymlinkAware(worldConfigDir);
                worldLoader.save(worldNode);
            } catch (final Exception ex) {
                LOGGER.error("Failed to convert the spigot.yml world settings for '{}'. They have been left in {} for you to move over by hand.", key, legacyConfig, ex);
            }
        });
    }

    private static void liftBelowZeroGeneration(final ConfigurationNode legacyWorldSettings, final ConfigurationNode global) throws ConfigurateException {
        if (legacyWorldSettings.virtual()) {
            return;
        }

        // Moved into the global config
        final String key = "below-zero-generation-in-existing-chunks";
        final ConfigurationNode target = global.node("misc", key);
        final ConfigurationNode fromDefault = legacyWorldSettings.node("default", key);
        if (!fromDefault.virtual() && target.virtual()) {
            target.set(fromDefault.raw());
        }
        for (final java.util.Map.Entry<Object, ? extends ConfigurationNode> entry : legacyWorldSettings.childrenMap().entrySet()) {
            entry.getValue().removeChild(key);
        }
    }

    private static Path resolveWorldConfigDirectory(final Path levelFolder, final String worldKey) {
        final int separator = worldKey.indexOf(':');
        if (separator < 0) {
            return levelFolder.resolveSibling(worldKey);
        }
        return levelFolder.resolve("dimensions").resolve(worldKey.substring(0, separator)).resolve(worldKey.substring(separator + 1));
    }

    private static String levelName(final OptionSet options) {
        final Object fromArgs = options.valueOf("world");
        if (fromArgs instanceof final String name && !name.isEmpty()) {
            return name;
        }
        final Path propertiesFile = ((File) options.valueOf("config")).toPath();
        if (Files.isRegularFile(propertiesFile)) {
            final java.util.Properties properties = new java.util.Properties();
            try (final java.io.Reader reader = Files.newBufferedReader(propertiesFile, StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (final IOException ex) {
                LOGGER.warn("Could not read {} while locating world config files, assuming the default level name", propertiesFile, ex);
            }
            return properties.getProperty("level-name", "world");
        }
        return "world";
    }

    /**
     * Copies every leaf of {@code source} into {@code target}, replacing values that are still the
     * sentinel meaning "defer to bukkit.yml" ({@code -1}, or {@code default}). A world value the admin
     * had already set to something real keeps winning, as it did before.
     */
    private static void overwriteDeferredValues(final ConfigurationNode source, final ConfigurationNode target) throws SerializationException {
        if (source.isMap()) {
            for (final java.util.Map.Entry<Object, ? extends ConfigurationNode> entry : source.childrenMap().entrySet()) {
                overwriteDeferredValues(entry.getValue(), target.node(entry.getKey()));
            }
            return;
        }
        if (target.virtual() || target.getInt(0) < 0 || "default".equals(target.getString())) {
            target.set(source.raw());
        }
    }

    private static boolean needsConverting(final Path legacyConfig) {
        return Files.exists(legacyConfig) && Files.isRegularFile(legacyConfig);
    }

    private static YamlConfiguration loadLegacyConfigFile(final Path configFile) throws IOException {
        final YamlConfiguration config = new YamlConfiguration();
        if (Files.exists(configFile)) {
            try {
                config.load(Files.newBufferedReader(configFile, StandardCharsets.UTF_8));
            } catch (final Exception ex) {
                throw new IOException("Failed to load configuration file: " + configFile.toAbsolutePath(), ex);
            }
        }
        return config;
    }

    private PaperConfigurationsInitializer() {}
}
