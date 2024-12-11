package io.papermc.paper.plugin.provider.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.papermc.paper.configuration.constraint.Constraint;
import io.papermc.paper.configuration.serializer.ComponentSerializer;
import io.papermc.paper.configuration.serializer.EnumValueSerializer;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.configuration.serializer.PermissionConfigurationSerializer;
import io.papermc.paper.plugin.provider.configuration.serializer.constraints.PluginConfigConstraints;
import io.papermc.paper.plugin.provider.configuration.type.DependencyConfiguration;
import io.papermc.paper.plugin.provider.configuration.type.PermissionConfiguration;
import io.papermc.paper.plugin.provider.configuration.type.PluginDependencyLifeCycle;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
@ConfigSerializable
public class PaperPluginMeta implements PluginMeta {

    @PluginConfigConstraints.PluginName
    @Required
    private String name;
    @Required
    @PluginConfigConstraints.PluginNameSpace
    private String main;
    @PluginConfigConstraints.PluginNameSpace
    private String bootstrapper;
    @PluginConfigConstraints.PluginNameSpace
    private String loader;
    private List<String> provides = List.of();
    private boolean hasOpenClassloader = false;
    @Required
    private String version;
    private String description;
    private List<String> authors = List.of();
    private List<String> contributors = List.of();
    private String website;
    private String prefix;
    private PluginLoadOrder load = PluginLoadOrder.POSTWORLD;
    @FlattenedResolver
    private PermissionConfiguration permissionConfiguration = new PermissionConfiguration(PermissionDefault.OP, List.of());
    @Required
    private ApiVersion apiVersion;

    private Map<PluginDependencyLifeCycle, Map<String, DependencyConfiguration>> dependencies = new EnumMap<>(PluginDependencyLifeCycle.class);

    public PaperPluginMeta() {
    }

    static final ApiVersion MINIMUM = ApiVersion.getOrCreateVersion("1.19");
    public static PaperPluginMeta create(BufferedReader reader) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
            .headerMode(HeaderMode.NONE)
            .source(() -> reader)
            .defaultOptions((options) -> {

                return options.serializers((serializers) -> {
                    serializers
                        .register(new ScalarSerializer<>(ApiVersion.class) {
                            @Override
                            public ApiVersion deserialize(final Type type, final Object obj) throws SerializationException {
                                try {
                                    final ApiVersion version = ApiVersion.getOrCreateVersion(obj.toString());
                                    if (version.isOlderThan(MINIMUM)) {
                                        throw new SerializationException(version + " is too old for a paper plugin!");
                                    }
                                    return version;
                                } catch (final IllegalArgumentException e) {
                                    throw new SerializationException(e);
                                }
                            }

                            @Override
                            protected Object serialize(final ApiVersion item, final Predicate<Class<?>> typeSupported) {
                                return item.getVersionString();
                            }
                        })
                        .register(new EnumValueSerializer())
                        .register(PermissionConfiguration.class, PermissionConfigurationSerializer.SERIALIZER)
                        .register(new ComponentSerializer())
                        .registerAnnotatedObjects(
                            ObjectMapper.factoryBuilder()
                                .addConstraint(Constraint.class, new Constraint.Factory())
                                .addConstraint(PluginConfigConstraints.PluginName.class, String.class, new PluginConfigConstraints.PluginName.Factory())
                                .addConstraint(PluginConfigConstraints.PluginNameSpace.class, String.class, new PluginConfigConstraints.PluginNameSpace.Factory())
                                .addNodeResolver(new FlattenedResolver.Factory())
                                .build()
                        );

                });
            })
            .build();
        CommentedConfigurationNode node = loader.load();
        LegacyPaperMeta.migrate(node);
        PaperPluginMeta pluginConfiguration = node.require(PaperPluginMeta.class);

        if (!node.node("author").virtual()) {
            pluginConfiguration.authors = ImmutableList.<String>builder()
                .addAll(pluginConfiguration.authors)
                .add(node.node("author").getString())
                .build();
        }

        return pluginConfiguration;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @TestOnly
    public void setName(@NotNull String name) {
        Preconditions.checkNotNull(name, "name");
        this.name = name;
    }

    @Override
    public @NotNull String getMainClass() {
        return this.main;
    }

    @Override
    public @NotNull String getVersion() {
        return this.version;
    }

    @TestOnly
    public void setVersion(@NotNull String version) {
        Preconditions.checkNotNull(version, "version");
        this.version = version;
    }

    @Override
    public @Nullable String getLoggerPrefix() {
        return this.prefix;
    }

    @Override
    public @NotNull List<String> getPluginDependencies() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.SERVER, Map.of())
            .entrySet()
            .stream()
            .filter((entry) -> entry.getValue().required() && entry.getValue().joinClasspath())
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    public @NotNull List<String> getPluginSoftDependencies() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.SERVER, Map.of())
            .entrySet()
            .stream()
            .filter((entry) -> !entry.getValue().required() && entry.getValue().joinClasspath())
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    public @NotNull List<String> getLoadBeforePlugins() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.SERVER, Map.of())
            .entrySet()
            .stream()
            // This plugin will load BEFORE all dependencies (so dependencies will load AFTER plugin)
            .filter((entry) -> entry.getValue().load() == DependencyConfiguration.LoadOrder.AFTER)
            .map(Map.Entry::getKey)
            .toList();
    }

    public @NotNull List<String> getLoadAfterPlugins() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.SERVER, Map.of())
            .entrySet()
            .stream()
            // This plugin will load AFTER all dependencies (so dependencies will load BEFORE plugin)
            .filter((entry) -> entry.getValue().load() == DependencyConfiguration.LoadOrder.BEFORE)
            .map(Map.Entry::getKey)
            .toList();
    }


    public Map<String, DependencyConfiguration> getServerDependencies() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.SERVER, Map.of());
    }

    public Map<String, DependencyConfiguration> getBootstrapDependencies() {
        return this.dependencies.getOrDefault(PluginDependencyLifeCycle.BOOTSTRAP, Map.of());
    }

    @Override
    public @NotNull PluginLoadOrder getLoadOrder() {
        return this.load;
    }

    @Override
    public @NotNull String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public @NotNull List<String> getContributors() {
        return this.contributors;
    }

    @Override
    public String getWebsite() {
        return this.website;
    }

    @Override
    public @NotNull List<Permission> getPermissions() {
        return this.permissionConfiguration.permissions();
    }

    @Override
    public @NotNull PermissionDefault getPermissionDefault() {
        return this.permissionConfiguration.defaultPerm();
    }

    @Override
    public @NotNull String getAPIVersion() {
        return this.apiVersion.getVersionString();
    }

    @Override
    public @NotNull List<String> getProvidedPlugins() {
        return this.provides;
    }

    public String getBootstrapper() {
        return this.bootstrapper;
    }

    public String getLoader() {
        return this.loader;
    }

    public boolean hasOpenClassloader() {
        return this.hasOpenClassloader;
    }

}
