package io.papermc.paper.configuration;

import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.configuration.mapping.Definition;
import io.papermc.paper.configuration.mapping.FieldProcessor;
import io.papermc.paper.configuration.mapping.InnerClassFieldDiscoverer;
import io.papermc.paper.configuration.mapping.LoadEarly;
import io.papermc.paper.configuration.mapping.MergeMap;
import io.papermc.paper.configuration.serializer.ComponentSerializer;
import io.papermc.paper.configuration.serializer.EnumValueSerializer;
import io.papermc.paper.configuration.serializer.IdentifierSerializer;
import io.papermc.paper.configuration.serializer.NbtPathSerializer;
import io.papermc.paper.configuration.serializer.ServerboundPacketClassSerializer;
import io.papermc.paper.configuration.serializer.StringRepresentableSerializer;
import io.papermc.paper.configuration.serializer.collection.TableSerializer;
import io.papermc.paper.configuration.serializer.collection.map.FastutilMapSerializer;
import io.papermc.paper.configuration.serializer.collection.map.MapSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryHolderSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryValueSerializer;
import io.papermc.paper.configuration.transformation.ContextualTransformation;
import io.papermc.paper.configuration.transformation.Transformations;
import io.papermc.paper.configuration.transformation.global.versioned.V29_LogIPs;
import io.papermc.paper.configuration.transformation.global.versioned.V30_PacketIds;
import io.papermc.paper.configuration.transformation.global.versioned.V31_AllowNetherPropertiesToConfig;
import io.papermc.paper.configuration.transformation.global.versioned.V32_SpigotConfigCleanup;
import io.papermc.paper.configuration.transformation.world.FeatureSeedsGeneration;
import io.papermc.paper.configuration.transformation.world.versioned.V29_ZeroWorldHeight;
import io.papermc.paper.configuration.transformation.world.versioned.V30_RenameFilterNbtFromSpawnEgg;
import io.papermc.paper.configuration.transformation.world.versioned.V32_WorldConfigCleanup;
import io.papermc.paper.configuration.type.BooleanOrDefault;
import io.papermc.paper.configuration.type.DespawnRange;
import io.papermc.paper.configuration.type.Duration;
import io.papermc.paper.configuration.type.DurationOrDisabled;
import io.papermc.paper.configuration.type.EngineMode;
import io.papermc.paper.configuration.type.fallback.FallbackValueSerializer;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.configuration.type.number.LongOr;
import io.papermc.paper.configuration.yaml.YamlConfigurationLoader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.FieldDiscoverer;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;
import org.spongepowered.configurate.util.NamingSchemes;

import static io.leangen.geantyref.GenericTypeReflector.erase;

@SuppressWarnings("Convert2Diamond")
public class PaperConfigurations extends Configurations<GlobalConfiguration, WorldConfiguration> {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final String GLOBAL_CONFIG_FILE_NAME = "paper-global.yml";
    static final String WORLD_DEFAULTS_CONFIG_FILE_NAME = "paper-world-defaults.yml";
    static final String WORLD_CONFIG_FILE_NAME = "paper-world.yml";
    public static final String CONFIG_DIR = "config";

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

        World: %s""",
        PaperConfigurations.CONFIG_DIR,
        PaperConfigurations.WORLD_DEFAULTS_CONFIG_FILE_NAME,
        map.require(WORLD_KEY)
    );




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
            .register(LongOr.Default.SERIALIZER)
            .register(DoubleOr.Default.SERIALIZER)
            .register(DoubleOr.Disabled.SERIALIZER)
            .register(BooleanOrDefault.SERIALIZER)
            .register(Duration.SERIALIZER)
            .register(DurationOrDisabled.SERIALIZER)
            .register(NbtPathSerializer.SERIALIZER)
            .register(IdentifierSerializer.INSTANCE)
        );
    }

    @Override
    protected ObjectMapper.Factory.Builder createGlobalObjectMapperFactoryBuilder() {
        // populate the instance the early load created, so it isn't replaced
        return globalObjectMapperFactoryBuilder(super.createGlobalObjectMapperFactoryBuilder(), PaperConfigurationsInitializer.earlyGlobalConfig());
    }

    private static ObjectMapper.Factory.Builder globalObjectMapperFactoryBuilder(final ObjectMapper.Factory.Builder builder, final GlobalConfiguration instance) {
        return builder.addDiscoverer(InnerClassFieldDiscoverer.globalConfig(instance, defaultFieldProcessors()));
    }

    private static ObjectMapper.Factory.Builder earlyGlobalObjectMapperFactoryBuilder(final GlobalConfiguration instance) {
        // not based on the default builder, it always adds the @PostProcess post-processor
        return addDefaultConstraints(ObjectMapper.emptyFactoryBuilder())
            .defaultNamingScheme(NamingSchemes.LOWER_CASE_DASHED)
            .addNodeResolver(NodeResolver.nodeKey())
            .addNodeResolver(NodeResolver.keyFromSetting())
            .addNodeResolver(NodeResolver.nodeFromParent())
            .addDiscoverer(FieldDiscoverer.emptyConstructorObject())
            .addDiscoverer(FieldDiscoverer.record())
            .addDiscoverer(InnerClassFieldDiscoverer.earlyGlobalConfig(instance, defaultFieldProcessors())); // added last, so it's checked first
    }

    @Override
    protected YamlConfigurationLoader.Builder createGlobalLoaderBuilder(final ContextMap contextMap) {
        final RegistryAccess access = contextMap.require(REGISTRY_ACCESS);
        return super.createGlobalLoaderBuilder(contextMap)
            .defaultOptions((options) -> defaultGlobalOptions(options, access));
    }

    private static ConfigurationOptions defaultGlobalOptions(ConfigurationOptions options, RegistryAccess registryAccess) {
        return options
            .header(GLOBAL_HEADER)
            .serializers(builder -> builder
                .register(new ServerboundPacketClassSerializer())
                .register(new RegistryValueSerializer<>(new TypeToken<DataComponentType<?>>() {}, registryAccess, Registries.DATA_COMPONENT_TYPE, false))
                // stats.forced-custom-stat-values
                .register(new TypeToken<Object2IntMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Object2IntMap<?>>(Object2IntOpenHashMap::new, Integer.TYPE))
                .register(new RegistryHolderSerializer<>(new TypeToken<Identifier>() {}, registryAccess, Registries.CUSTOM_STAT, false))
                // attributes.overrides
                .register(new RegistryHolderSerializer<>(new TypeToken<Attribute>() {}, registryAccess, Registries.ATTRIBUTE, false))
                // spawning.spawn-limits / spawning.ticks-per-spawn
                .register(new TypeToken<Reference2IntMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2IntMap<?>>(Reference2IntOpenHashMap::new, Integer.TYPE))
                .register(StringRepresentableSerializer::isValidFor, new StringRepresentableSerializer())
                .register(new EnumValueSerializer())
            );
    }

    @Override
    public GlobalConfiguration initializeGlobalConfiguration(final DedicatedServer server) throws ConfigurateException {
        GlobalConfiguration configuration = super.initializeGlobalConfiguration(server);
        GlobalConfiguration.set(configuration);
        return configuration;
    }

    @Override
    protected ObjectMapper.Factory.Builder createWorldObjectMapperFactoryBuilder(final ContextMap contextMap) {
        return super.createWorldObjectMapperFactoryBuilder(contextMap)
            .addNodeResolver(new NestedSetting.Factory())
            .addDiscoverer(InnerClassFieldDiscoverer.worldConfig(createWorldConfigInstance(contextMap), defaultFieldProcessors()));
    }

    private static WorldConfiguration createWorldConfigInstance(ContextMap contextMap) {
        return new WorldConfiguration(contextMap.require(Configurations.WORLD_KEY));
    }

    @Override
    protected YamlConfigurationLoader.Builder createWorldConfigLoaderBuilder(final ContextMap contextMap) {
        final RegistryAccess access = contextMap.require(REGISTRY_ACCESS);
        return super.createWorldConfigLoaderBuilder(contextMap)
            .defaultOptions(options -> options
                .header(contextMap.require(WORLD_KEY).equals(WORLD_DEFAULTS_KEY) ? WORLD_DEFAULTS_HEADER : WORLD_HEADER.apply(contextMap))
                .serializers(serializers -> serializers
                    .register(new TypeToken<Reference2IntMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2IntMap<?>>(Reference2IntOpenHashMap::new, Integer.TYPE))
                    .register(new TypeToken<Reference2LongMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2LongMap<?>>(Reference2LongOpenHashMap::new, Long.TYPE))
                    .register(new TypeToken<Reference2ObjectMap<?, ?>>() {}, new FastutilMapSerializer.SomethingToSomething<Reference2ObjectMap<?, ?>>(Reference2ObjectOpenHashMap::new))
                    .register(new TypeToken<Table<?, ?, ?>>() {}, new TableSerializer())
                    .register(DespawnRange.class, DespawnRange.SERIALIZER)
                    .register(StringRepresentableSerializer::isValidFor, new StringRepresentableSerializer())
                    .register(EngineMode.SERIALIZER)
                    .register(FallbackValueSerializer.create(createWorldConfigInstance(contextMap), MinecraftServer::getServer))
                    .register(new RegistryValueSerializer<>(new TypeToken<EntityType<?>>() {}, access, Registries.ENTITY_TYPE, true))
                    .register(new RegistryValueSerializer<>(Item.class, access, Registries.ITEM, true))
                    .register(new RegistryValueSerializer<>(Block.class, access, Registries.BLOCK, true))
                    .register(new RegistryHolderSerializer<>(new TypeToken<Feature>() {}, access, Registries.FEATURE, false))
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
        V32_WorldConfigCleanup.apply(versionedBuilder);
        // ADD FUTURE VERSIONED TRANSFORMS TO versionedBuilder HERE
        versionedBuilder.build().apply(node);
    }

    @Override
    protected void applyGlobalConfigTransformations(final ContextMap contextMap, final ConfigurationNode node) throws ConfigurateException {
        applyGlobalTransformations(contextMap, node);
    }

    /**
     * Applies removals and versioned transformations for the global config.
     *
     * @param contextMap context for the transformations, which must contain everything each of them requires
     * @param node configuration node
     */
    static void applyGlobalTransformations(final ContextMap contextMap, final ConfigurationNode node) throws ConfigurateException {
        applyGlobalTransformations(contextMap, node, false);
    }

    /**
     * Applies the global transformations that can run before the server exists, for reading {@link LoadEarly} fields.
     * Every transformation requiring context that isn't available yet is skipped, so the node must never be saved.
     *
     * @param node configuration node
     * @see PaperConfigurationsInitializer#loadEarlyGlobalConfig(Path)
     */
    static void applyEarlyGlobalTransformations(final ConfigurationNode node) throws ConfigurateException {
        applyGlobalTransformations(ContextMap.builder().build(), node, true);
    }

    private static List<ContextualTransformation> versionedGlobalTransformations() {
        return List.of(
            V29_LogIPs.INSTANCE,
            V30_PacketIds.INSTANCE,
            V31_AllowNetherPropertiesToConfig.INSTANCE,
            V32_SpigotConfigCleanup.INSTANCE
            // ADD FUTURE VERSIONED TRANSFORMS HERE
        );
    }

    private static void applyGlobalTransformations(final ContextMap contextMap, final ConfigurationNode node, final boolean skipUnavailable) throws ConfigurateException {
        ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        for (NodePath path : RemovedConfigurations.REMOVED_GLOBAL_PATHS) {
            builder.addAction(path, TransformAction.remove());
        }
        builder.build().apply(node);

        final ConfigurationTransformation.VersionedBuilder versionedBuilder = Transformations.versionedBuilder();
        for (final ContextualTransformation transformation : versionedGlobalTransformations()) {
            final List<String> missing = transformation.requiredContext().stream()
                .filter(key -> !contextMap.has(key))
                .map(ContextKey::name)
                .toList();
            if (missing.isEmpty()) {
                versionedBuilder.addVersion(transformation.version(), transformation.create(contextMap));
            } else if (!skipUnavailable) {
                // the version is bumped and saved regardless, so the skipped migration would be lost for good
                throw new IllegalStateException("Global config transformation to version " + transformation.version() + " requires " + String.join(", ", missing));
            }
        }
        versionedBuilder.build().apply(node);
    }

    /**
     * Options for loading the global config file before {@code Bootstrap} has run and {@code RegistryAccess} exists.
     * Only {@link LoadEarly} fields are deserialized, into the given instance, and no post-processors are run.
     *
     * @param options base options
     * @param instance global config instance to populate
     * @return early global config options
     */
    static ConfigurationOptions earlyGlobalOptions(final ConfigurationOptions options, final GlobalConfiguration instance) {
        return applyObjectMapperFactory(defaultOptions(options), PaperConfigurations::isConfigPart, earlyGlobalObjectMapperFactoryBuilder(instance).build());
    }

    @Override
    protected void applyDefaultsAwareWorldConfigTransformations(final ContextMap contextMap, final ConfigurationNode worldNode, final ConfigurationNode defaultsNode) throws ConfigurateException {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        // ADD FUTURE TRANSFORMS HERE (these transforms run after the defaults have been merged into the node)
        FeatureSeedsGeneration.apply(builder, contextMap, defaultsNode);
        builder.build().apply(worldNode);
    }

    @Override
    public WorldConfiguration createWorldConfig(final ContextMap contextMap) {
        final String levelKey = contextMap.require(WORLD_KEY).toString();
        try {
            return super.createWorldConfig(contextMap);
        } catch (IOException exception) {
            throw new RuntimeException("Could not create world config for " + levelKey, exception);
        }
    }

    @Override
    protected boolean isConfigType(final Type type) {
        return isConfigPart(type);
    }

    private static boolean isConfigPart(final Type type) {
        return ConfigurationPart.class.isAssignableFrom(erase(type));
    }

    public void reloadConfigs(final DedicatedServer server) {
        try {
            this.initializeGlobalConfiguration(this.createGlobalContextMap(server).build(), reloader(this.globalConfigClass, GlobalConfiguration.get()));
            this.initializeWorldDefaultsConfiguration(server.registryAccess());
            for (final ServerLevel level : server.getAllLevels()) {
                this.createWorldConfig(createWorldContextMap(level), reloader(this.worldConfigClass, level.paperConfig()));
            }
        } catch (final Exception ex) {
            throw new RuntimeException("Could not reload paper configuration files", ex);
        }
    }

    private static List<Definition<? extends Annotation, ?, ? extends FieldProcessor.Factory<?, ?>>> defaultFieldProcessors() {
        return List.of(
            MergeMap.DEFINITION
        );
    }

    private static ContextMap createWorldContextMap(ServerLevel level) {
        return createWorldContextMap(level.getServer().storageSource.getDimensionPath(level.dimension()), level.dimension().identifier(), level.registryAccess(), level.getGameRules());
    }

    public static ContextMap createWorldContextMap(final Path dir, final Identifier worldKey, final RegistryAccess registryAccess, final GameRules gameRules) {
        return ContextMap.builder()
            .put(WORLD_DIRECTORY, dir)
            .put(WORLD_KEY, worldKey)
            .put(REGISTRY_ACCESS, registryAccess)
            .put(GAME_RULES, gameRules)
            .build();
    }

    public static PaperConfigurations setup(final Path configDir) throws Exception {
        try {
            createDirectoriesSymlinkAware(configDir);
            return new PaperConfigurations(configDir);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not setup PaperConfigurations", ex);
        }
    }


    @Deprecated
    public YamlConfiguration createLegacyObject(final MinecraftServer server) {
        final YamlConfiguration global = YamlConfiguration.loadConfiguration(this.globalFolder.resolve(this.globalConfigFileName).toFile());
        final ConfigurationSection worlds = global.createSection("__________WORLDS__________");
        worlds.set("__defaults__", YamlConfiguration.loadConfiguration(this.globalFolder.resolve(this.defaultWorldConfigFileName).toFile()));
        for (final ServerLevel level : server.getAllLevels()) {
            worlds.set(level.getWorld().getName(), YamlConfiguration.loadConfiguration(this.getWorldConfigFile(level).toFile()));
        }
        return global;
    }


    /**
     * Creates the global config with all defaults, the same way {@link #initializeGlobalConfiguration(DedicatedServer)} would.
     *
     * @param registryAccess registry access
     * @return default global config
     */
    @VisibleForTesting
    static GlobalConfiguration createGlobalForTesting(final RegistryAccess registryAccess) throws SerializationException {
        final GlobalConfiguration instance = new GlobalConfiguration();
        final ObjectMapper.Factory factory = globalObjectMapperFactoryBuilder(defaultObjectMapperFactoryBuilder(), instance).build();
        final ConfigurationOptions options = applyObjectMapperFactory(defaultGlobalOptions(defaultOptions(ConfigurationOptions.defaults()), registryAccess), PaperConfigurations::isConfigPart, factory);
        BasicConfigurationNode.root(options).require(GlobalConfiguration.class); // populates the instance passed to the factory
        return instance;
    }

    @VisibleForTesting
    public static WorldConfiguration createWorldDefaultsForTesting() {
        return new WorldConfiguration(Configurations.WORLD_DEFAULTS_KEY);
    }

    // Symlinks are not correctly checked in createDirectories
    static void createDirectoriesSymlinkAware(final Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }
}
