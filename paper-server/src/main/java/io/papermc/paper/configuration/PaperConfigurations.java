package io.papermc.paper.configuration;

import com.google.common.base.Suppliers;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.configuration.legacy.RequiresSpigotInitialization;
import io.papermc.paper.configuration.mapping.Definition;
import io.papermc.paper.configuration.mapping.FieldProcessor;
import io.papermc.paper.configuration.mapping.InnerClassFieldDiscoverer;
import io.papermc.paper.configuration.mapping.MergeMap;
import io.papermc.paper.configuration.serializer.ComponentSerializer;
import io.papermc.paper.configuration.serializer.EnumValueSerializer;
import io.papermc.paper.configuration.serializer.NbtPathSerializer;
import io.papermc.paper.configuration.serializer.ServerboundPacketClassSerializer;
import io.papermc.paper.configuration.serializer.ResourceLocationSerializer;
import io.papermc.paper.configuration.serializer.StringRepresentableSerializer;
import io.papermc.paper.configuration.serializer.collection.TableSerializer;
import io.papermc.paper.configuration.serializer.collection.map.FastutilMapSerializer;
import io.papermc.paper.configuration.serializer.collection.map.MapSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryHolderSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryValueSerializer;
import io.papermc.paper.configuration.transformation.Transformations;
import io.papermc.paper.configuration.transformation.global.LegacyPaperConfig;
import io.papermc.paper.configuration.transformation.global.versioned.V29_LogIPs;
import io.papermc.paper.configuration.transformation.global.versioned.V30_PacketIds;
import io.papermc.paper.configuration.transformation.global.versioned.V31_AllowNetherPropertiesToConfig;
import io.papermc.paper.configuration.transformation.world.FeatureSeedsGeneration;
import io.papermc.paper.configuration.transformation.world.LegacyPaperWorldConfig;
import io.papermc.paper.configuration.transformation.world.versioned.V29_ZeroWorldHeight;
import io.papermc.paper.configuration.transformation.world.versioned.V30_RenameFilterNbtFromSpawnEgg;
import io.papermc.paper.configuration.type.BooleanOrDefault;
import io.papermc.paper.configuration.type.DespawnRange;
import io.papermc.paper.configuration.type.Duration;
import io.papermc.paper.configuration.type.DurationOrDisabled;
import io.papermc.paper.configuration.type.EngineMode;
import io.papermc.paper.configuration.type.fallback.FallbackValueSerializer;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spigotmc.SpigotConfig;
import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static com.google.common.base.Preconditions.checkState;
import static io.leangen.geantyref.GenericTypeReflector.erase;

@SuppressWarnings("Convert2Diamond")
public class PaperConfigurations extends Configurations<GlobalConfiguration, WorldConfiguration> {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final String GLOBAL_CONFIG_FILE_NAME = "paper-global.yml";
    static final String WORLD_DEFAULTS_CONFIG_FILE_NAME = "paper-world-defaults.yml";
    static final String WORLD_CONFIG_FILE_NAME = "paper-world.yml";
    public static final String CONFIG_DIR = "config";
    private static final String BACKUP_DIR ="legacy-backup";

    private static final String GLOBAL_HEADER = String.format("""
            This is the global configuration file for Paper.
            As you can see, there's a lot to configure. Some options may impact gameplay, so use
            with caution, and make sure you know what each option does before configuring.

            If you need help with the configuration or have any questions related to Paper,
            join us in our Discord or check the docs page.

            The world configuration options have been moved inside
            their respective world folder. The files are named %s

            File Reference: https://docs.papermc.io/paper/reference/global-configuration/
            Docs: https://docs.papermc.io/
            Discord: https://discord.gg/papermc
            Website: https://papermc.io/""", WORLD_CONFIG_FILE_NAME);

    private static final String WORLD_DEFAULTS_HEADER = """
            This is the world defaults configuration file for Paper.
            As you can see, there's a lot to configure. Some options may impact gameplay, so use
            with caution, and make sure you know what each option does before configuring.

            If you need help with the configuration or have any questions related to Paper,
            join us in our Discord or check the docs page.

            Configuration options here apply to all worlds, unless you specify overrides inside
            the world-specific config file inside each world folder.

            File Reference: https://docs.papermc.io/paper/reference/world-configuration/
            Docs: https://docs.papermc.io/
            Discord: https://discord.gg/papermc
            Website: https://papermc.io/""";

    private static final Function<ContextMap, String> WORLD_HEADER = map -> String.format("""
        This is a world configuration file for Paper.
        This file may start empty but can be filled with settings to override ones in the %s/%s
        
        For more information, see https://docs.papermc.io/paper/reference/configuration/#per-world-configuration
        
        World: %s (%s)""",
        PaperConfigurations.CONFIG_DIR,
        PaperConfigurations.WORLD_DEFAULTS_CONFIG_FILE_NAME,
        map.require(WORLD_NAME),
        map.require(WORLD_KEY)
    );

    private static final String MOVED_NOTICE = """
        The global and world default configuration files have moved to %s
        and the world-specific configuration file has been moved inside
        the respective world folder.
        
        See https://docs.papermc.io/paper/configuration for more information.
        """;

    @VisibleForTesting
    public static final Supplier<SpigotWorldConfig> SPIGOT_WORLD_DEFAULTS = Suppliers.memoize(() -> new SpigotWorldConfig(RandomStringUtils.randomAlphabetic(255)) {
        @Override // override to ensure "verbose" is false
        public void init() {
            SpigotConfig.readConfig(SpigotWorldConfig.class, this);
        }
    });
    public static final ContextKey<Supplier<SpigotWorldConfig>> SPIGOT_WORLD_CONFIG_CONTEXT_KEY = new ContextKey<>(new TypeToken<Supplier<SpigotWorldConfig>>() {}, "spigot world config");


    public PaperConfigurations(final Path globalFolder) {
        super(globalFolder, GlobalConfiguration.class, WorldConfiguration.class, GLOBAL_CONFIG_FILE_NAME, WORLD_DEFAULTS_CONFIG_FILE_NAME, WORLD_CONFIG_FILE_NAME);
    }

    @Override
    protected int globalConfigVersion() {
        return GlobalConfiguration.CURRENT_VERSION;
    }

    @Override
    protected int worldConfigVersion() {
        return WorldConfiguration.CURRENT_VERSION;
    }

    @Override
    protected YamlConfigurationLoader.Builder createLoaderBuilder() {
        return super.createLoaderBuilder()
            .defaultOptions(PaperConfigurations::defaultOptions);
    }

    private static ConfigurationOptions defaultOptions(ConfigurationOptions options) {
        return options.serializers(builder -> builder
            .register(MapSerializer.TYPE, new MapSerializer(false))
            .register(new EnumValueSerializer())
            .register(new ComponentSerializer())
            .register(IntOr.Default.SERIALIZER)
            .register(IntOr.Disabled.SERIALIZER)
            .register(DoubleOr.Default.SERIALIZER)
            .register(DoubleOr.Disabled.SERIALIZER)
            .register(BooleanOrDefault.SERIALIZER)
            .register(Duration.SERIALIZER)
            .register(DurationOrDisabled.SERIALIZER)
            .register(NbtPathSerializer.SERIALIZER)
            .register(ResourceLocationSerializer.INSTANCE)
        );
    }

    @Override
    protected ObjectMapper.Factory.Builder createGlobalObjectMapperFactoryBuilder() {
        return defaultGlobalFactoryBuilder(super.createGlobalObjectMapperFactoryBuilder());
    }

    private static ObjectMapper.Factory.Builder defaultGlobalFactoryBuilder(ObjectMapper.Factory.Builder builder) {
        return builder.addDiscoverer(InnerClassFieldDiscoverer.globalConfig(defaultFieldProcessors()));
    }

    @Override
    protected YamlConfigurationLoader.Builder createGlobalLoaderBuilder(RegistryAccess registryAccess) {
        return super.createGlobalLoaderBuilder(registryAccess)
            .defaultOptions((options) -> defaultGlobalOptions(registryAccess, options));
    }

    private static ConfigurationOptions defaultGlobalOptions(RegistryAccess registryAccess, ConfigurationOptions options) {
        return options
            .header(GLOBAL_HEADER)
            .serializers(builder -> builder
                .register(new ServerboundPacketClassSerializer())
                .register(new RegistryValueSerializer<>(new TypeToken<DataComponentType<?>>() {}, registryAccess, Registries.DATA_COMPONENT_TYPE, false))
            );
    }

    @Override
    public GlobalConfiguration initializeGlobalConfiguration(final RegistryAccess registryAccess) throws ConfigurateException {
        GlobalConfiguration configuration = super.initializeGlobalConfiguration(registryAccess);
        GlobalConfiguration.set(configuration);
        return configuration;
    }

    @Override
    protected ContextMap.Builder createDefaultContextMap(final RegistryAccess registryAccess) {
        return super.createDefaultContextMap(registryAccess)
            .put(SPIGOT_WORLD_CONFIG_CONTEXT_KEY, SPIGOT_WORLD_DEFAULTS);
    }

    @Override
    protected ObjectMapper.Factory.Builder createWorldObjectMapperFactoryBuilder(final ContextMap contextMap) {
        return super.createWorldObjectMapperFactoryBuilder(contextMap)
            .addNodeResolver(new RequiresSpigotInitialization.Factory(contextMap.require(SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get()))
            .addNodeResolver(new NestedSetting.Factory())
            .addDiscoverer(InnerClassFieldDiscoverer.worldConfig(createWorldConfigInstance(contextMap), defaultFieldProcessors()));
    }

    private static WorldConfiguration createWorldConfigInstance(ContextMap contextMap) {
        return new WorldConfiguration(
            contextMap.require(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get(),
            contextMap.require(Configurations.WORLD_KEY)
        );
    }

    @Override
    protected YamlConfigurationLoader.Builder createWorldConfigLoaderBuilder(final ContextMap contextMap) {
        final RegistryAccess access = contextMap.require(REGISTRY_ACCESS);
        return super.createWorldConfigLoaderBuilder(contextMap)
            .defaultOptions(options -> options
                .header(contextMap.require(WORLD_NAME).equals(WORLD_DEFAULTS) ? WORLD_DEFAULTS_HEADER : WORLD_HEADER.apply(contextMap))
                .serializers(serializers -> serializers
                    .register(new TypeToken<Reference2IntMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2IntMap<?>>(Reference2IntOpenHashMap::new, Integer.TYPE))
                    .register(new TypeToken<Reference2LongMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2LongMap<?>>(Reference2LongOpenHashMap::new, Long.TYPE))
                    .register(new TypeToken<Reference2ObjectMap<?, ?>>() {}, new FastutilMapSerializer.SomethingToSomething<Reference2ObjectMap<?, ?>>(Reference2ObjectOpenHashMap::new))
                    .register(new TypeToken<Table<?, ?, ?>>() {}, new TableSerializer())
                    .register(DespawnRange.class, DespawnRange.SERIALIZER)
                    .register(StringRepresentableSerializer::isValidFor, new StringRepresentableSerializer())
                    .register(EngineMode.SERIALIZER)
                    .register(FallbackValueSerializer.create(contextMap.require(SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get(), MinecraftServer::getServer))
                    .register(new RegistryValueSerializer<>(new TypeToken<EntityType<?>>() {}, access, Registries.ENTITY_TYPE, true))
                    .register(new RegistryValueSerializer<>(Item.class, access, Registries.ITEM, true))
                    .register(new RegistryValueSerializer<>(Block.class, access, Registries.BLOCK, true))
                    .register(new RegistryHolderSerializer<>(new TypeToken<ConfiguredFeature<?, ?>>() {}, access, Registries.CONFIGURED_FEATURE, false))
                )
            );
    }

    @Override
    protected void applyWorldConfigTransformations(final ContextMap contextMap, final ConfigurationNode node, final @Nullable ConfigurationNode defaultsNode) throws ConfigurateException {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        for (final NodePath path : RemovedConfigurations.REMOVED_WORLD_PATHS) {
            builder.addAction(path, TransformAction.remove());
        }
        builder.build().apply(node);

        final ConfigurationTransformation.VersionedBuilder versionedBuilder = Transformations.versionedBuilder();
        V29_ZeroWorldHeight.apply(versionedBuilder);
        V30_RenameFilterNbtFromSpawnEgg.apply(versionedBuilder);
        // ADD FUTURE VERSIONED TRANSFORMS TO versionedBuilder HERE
        versionedBuilder.build().apply(node);
    }

    @Override
    protected void applyGlobalConfigTransformations(ConfigurationNode node) throws ConfigurateException {
        ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        for (NodePath path : RemovedConfigurations.REMOVED_GLOBAL_PATHS) {
            builder.addAction(path, TransformAction.remove());
        }
        builder.build().apply(node);

        final ConfigurationTransformation.VersionedBuilder versionedBuilder = Transformations.versionedBuilder();
        V29_LogIPs.apply(versionedBuilder);
        V30_PacketIds.apply(versionedBuilder);
        V31_AllowNetherPropertiesToConfig.apply(versionedBuilder);
        // ADD FUTURE VERSIONED TRANSFORMS TO versionedBuilder HERE
        versionedBuilder.build().apply(node);
    }

    private static final List<Transformations.DefaultsAware> DEFAULT_AWARE_TRANSFORMATIONS = List.of(
        FeatureSeedsGeneration::apply
    );

    @Override
    protected void applyDefaultsAwareWorldConfigTransformations(final ContextMap contextMap, final ConfigurationNode worldNode, final ConfigurationNode defaultsNode) throws ConfigurateException {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        // ADD FUTURE TRANSFORMS HERE (these transforms run after the defaults have been merged into the node)
        DEFAULT_AWARE_TRANSFORMATIONS.forEach(transform -> transform.apply(builder, contextMap, defaultsNode));
        builder.build().apply(worldNode);
    }

    @Override
    public WorldConfiguration createWorldConfig(final ContextMap contextMap) {
        final String levelName = contextMap.require(WORLD_NAME);
        try {
            return super.createWorldConfig(contextMap);
        } catch (IOException exception) {
            throw new RuntimeException("Could not create world config for " + levelName, exception);
        }
    }

    @Override
    protected boolean isConfigType(final Type type) {
        return ConfigurationPart.class.isAssignableFrom(erase(type));
    }

    public void reloadConfigs(MinecraftServer server) {
        try {
            this.initializeGlobalConfiguration(server.registryAccess(), reloader(this.globalConfigClass, GlobalConfiguration.get()));
            this.initializeWorldDefaultsConfiguration(server.registryAccess());
            for (ServerLevel level : server.getAllLevels()) {
                this.createWorldConfig(createWorldContextMap(level), reloader(this.worldConfigClass, level.paperConfig()));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not reload paper configuration files", ex);
        }
    }

    private static List<Definition<? extends Annotation, ?, ? extends FieldProcessor.Factory<?, ?>>> defaultFieldProcessors() {
        return List.of(
            MergeMap.DEFINITION
        );
    }

    private static ContextMap createWorldContextMap(ServerLevel level) {
        return createWorldContextMap(level.levelStorageAccess.levelDirectory.path(), level.serverLevelData.getLevelName(), level.dimension().location(), level.spigotConfig, level.registryAccess(), level.getGameRules());
    }

    public static ContextMap createWorldContextMap(final Path dir, final String levelName, final ResourceLocation worldKey, final SpigotWorldConfig spigotConfig, final RegistryAccess registryAccess, final GameRules gameRules) {
        return ContextMap.builder()
            .put(WORLD_DIRECTORY, dir)
            .put(WORLD_NAME, levelName)
            .put(WORLD_KEY, worldKey)
            .put(SPIGOT_WORLD_CONFIG_CONTEXT_KEY, Suppliers.ofInstance(spigotConfig))
            .put(REGISTRY_ACCESS, registryAccess)
            .put(GAME_RULES, gameRules)
            .build();
    }

    public static PaperConfigurations setup(final Path legacyConfig, final Path configDir, final Path worldFolder, final File spigotConfig) throws Exception {
        final Path legacy = Files.isSymbolicLink(legacyConfig) ? Files.readSymbolicLink(legacyConfig) : legacyConfig;
        if (needsConverting(legacyConfig)) {
            final String legacyFileName = legacyConfig.getFileName().toString();
            try {
                if (Files.exists(configDir) && !Files.isDirectory(configDir)) {
                    throw new RuntimeException("Paper needs to create a '" + configDir.toAbsolutePath() + "' folder. You already have a non-directory named '" + configDir.toAbsolutePath() + "'. Please remove it and restart the server.");
                }
                final Path backupDir = configDir.resolve(BACKUP_DIR);
                if (Files.exists(backupDir) && !Files.isDirectory(backupDir)) {
                    throw new RuntimeException("Paper needs to create a '" + BACKUP_DIR + "' directory in the '" + configDir.toAbsolutePath() + "' folder. You already have a non-directory named '" + BACKUP_DIR + "'. Please remove it and restart the server.");
                }
                createDirectoriesSymlinkAware(backupDir);
                final String backupFileName = legacyFileName + ".old";
                final Path legacyConfigBackup = backupDir.resolve(backupFileName);
                if (Files.exists(legacyConfigBackup) && !Files.isRegularFile(legacyConfigBackup)) {
                    throw new RuntimeException("Paper needs to create a '" + backupFileName + "' file in the '" + backupDir.toAbsolutePath() + "' folder. You already have a non-file named '" + backupFileName + "'. Please remove it and restart the server.");
                }
                Files.move(legacyConfig.toRealPath(), legacyConfigBackup, StandardCopyOption.REPLACE_EXISTING); // make backup
                if (Files.isSymbolicLink(legacyConfig)) {
                    Files.delete(legacyConfig);
                }
                final Path replacementFile = legacy.resolveSibling(legacyFileName + "-README.txt");
                if (Files.notExists(replacementFile)) {
                    Files.createFile(replacementFile);
                    Files.writeString(replacementFile, String.format(MOVED_NOTICE, configDir.toAbsolutePath()));
                }
                convert(legacyConfigBackup, configDir, worldFolder, spigotConfig);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not convert '" + legacyFileName + "' to the new configuration format", ex);
            }
        }
        try {
            createDirectoriesSymlinkAware(configDir);
            return new PaperConfigurations(configDir);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not setup PaperConfigurations", ex);
        }
    }

    private static void convert(final Path legacyConfig, final Path configDir, final Path worldFolder, final File spigotConfig) throws Exception {
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
        spigotConfiguration.save(spigotConfig);
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
                ex.printStackTrace();
            }
        });
    }

    private static boolean needsConverting(final Path legacyConfig) {
        return Files.exists(legacyConfig) && Files.isRegularFile(legacyConfig);
    }

    @Deprecated
    public YamlConfiguration createLegacyObject(final MinecraftServer server) {
        YamlConfiguration global = YamlConfiguration.loadConfiguration(this.globalFolder.resolve(this.globalConfigFileName).toFile());
        ConfigurationSection worlds = global.createSection("__________WORLDS__________");
        worlds.set("__defaults__", YamlConfiguration.loadConfiguration(this.globalFolder.resolve(this.defaultWorldConfigFileName).toFile()));
        for (ServerLevel level : server.getAllLevels()) {
            worlds.set(level.getWorld().getName(), YamlConfiguration.loadConfiguration(getWorldConfigFile(level).toFile()));
        }
        return global;
    }

    @Deprecated
    public static YamlConfiguration loadLegacyConfigFile(File configFile) throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        if (configFile.exists()) {
            try {
                config.load(configFile);
            } catch (Exception ex) {
                throw new Exception("Failed to load configuration file: " + configFile.getName(), ex);
            }
        }
        return config;
    }

    @VisibleForTesting
    static ConfigurationNode createForTesting(RegistryAccess registryAccess) {
        ObjectMapper.Factory factory = defaultGlobalFactoryBuilder(ObjectMapper.factoryBuilder()).build();
        ConfigurationOptions options = defaultGlobalOptions(registryAccess, defaultOptions(ConfigurationOptions.defaults()))
            .serializers(builder -> builder.register(type -> ConfigurationPart.class.isAssignableFrom(erase(type)), factory.asTypeSerializer()));
        return BasicConfigurationNode.root(options);
    }

    // Symlinks are not correctly checked in createDirectories
    static void createDirectoriesSymlinkAware(Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }
}
