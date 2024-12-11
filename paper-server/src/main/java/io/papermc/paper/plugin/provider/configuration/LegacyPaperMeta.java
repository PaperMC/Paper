package io.papermc.paper.plugin.provider.configuration;

import com.google.gson.reflect.TypeToken;
import io.papermc.paper.plugin.provider.configuration.type.DependencyConfiguration;
import io.papermc.paper.plugin.provider.configuration.type.PluginDependencyLifeCycle;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class LegacyPaperMeta {


    private static final TypeToken<Map<PluginDependencyLifeCycle, Map<String, DependencyConfiguration>>> TYPE_TOKEN = new TypeToken<>() {
    };

    public static void migrate(CommentedConfigurationNode node) throws ConfigurateException {
        ConfigurationTransformation.chain(notVersioned()).apply(node);
    }

    private static ConfigurationTransformation notVersioned() {
        return ConfigurationTransformation.builder()
            .addAction(NodePath.path(), (path, value) -> {
                boolean bootstrapSubSection = value.hasChild("bootstrap");
                boolean serverSubSection = value.hasChild("server");

                // Ignore if using newer format
                if (bootstrapSubSection || serverSubSection) {
                    return null;
                }

                // First collect all load before elements
                LegacyConfiguration legacyConfiguration;
                try {
                    legacyConfiguration = value.require(LegacyConfiguration.class);
                } catch (SerializationException exception) {
                    // Ignore if not present
                    return null;
                }

                Map<PluginDependencyLifeCycle, Map<String, DependencyConfiguration>> dependencies = new EnumMap<>(PluginDependencyLifeCycle.class);
                dependencies.put(PluginDependencyLifeCycle.BOOTSTRAP, new HashMap<>());
                dependencies.put(PluginDependencyLifeCycle.SERVER, new HashMap<>());

                Map<PluginDependencyLifeCycle, Map<String, Set<DependencyFlag>>> dependencyConfigurationMap = new HashMap<>();
                dependencyConfigurationMap.put(PluginDependencyLifeCycle.BOOTSTRAP, new HashMap<>());
                dependencyConfigurationMap.put(PluginDependencyLifeCycle.SERVER, new HashMap<>());

                // Migrate loadafter
                for (LegacyLoadConfiguration legacyConfig : legacyConfiguration.loadAfter) {
                    Set<DependencyFlag> dependencyFlags = dependencyConfigurationMap
                        .get(legacyConfig.bootstrap ? PluginDependencyLifeCycle.BOOTSTRAP : PluginDependencyLifeCycle.SERVER)
                        .computeIfAbsent(legacyConfig.name, s -> EnumSet.noneOf(DependencyFlag.class));

                    dependencyFlags.add(DependencyFlag.LOAD_AFTER);
                }

                // Migrate loadbefore
                for (LegacyLoadConfiguration legacyConfig : legacyConfiguration.loadBefore) {
                    Set<DependencyFlag> dependencyFlags = dependencyConfigurationMap
                        .get(legacyConfig.bootstrap ? PluginDependencyLifeCycle.BOOTSTRAP : PluginDependencyLifeCycle.SERVER)
                        .computeIfAbsent(legacyConfig.name, s -> EnumSet.noneOf(DependencyFlag.class));

                    dependencyFlags.add(DependencyFlag.LOAD_BEFORE);
                }

                // Migrate dependencies
                for (LegacyDependencyConfiguration legacyConfig : legacyConfiguration.dependencies) {
                    Set<DependencyFlag> dependencyFlags = dependencyConfigurationMap
                        .get(legacyConfig.bootstrap ? PluginDependencyLifeCycle.BOOTSTRAP : PluginDependencyLifeCycle.SERVER)
                        .computeIfAbsent(legacyConfig.name, s -> EnumSet.noneOf(DependencyFlag.class));

                    dependencyFlags.add(DependencyFlag.DEPENDENCY);
                    if (legacyConfig.required) {
                        dependencyFlags.add(DependencyFlag.REQUIRED);
                    }
                }
                for (Map.Entry<PluginDependencyLifeCycle, Map<String, Set<DependencyFlag>>> legacyTypes : dependencyConfigurationMap.entrySet()) {
                    Map<String, DependencyConfiguration> flagMap = dependencies.get(legacyTypes.getKey());
                    for (Map.Entry<String, Set<DependencyFlag>> entry : legacyTypes.getValue().entrySet()) {
                        Set<DependencyFlag> flags = entry.getValue();


                        DependencyConfiguration.LoadOrder loadOrder = DependencyConfiguration.LoadOrder.OMIT;
                        // These meanings are now swapped
                        if (flags.contains(DependencyFlag.LOAD_BEFORE)) {
                            loadOrder = DependencyConfiguration.LoadOrder.AFTER;
                        } else if (flags.contains(DependencyFlag.LOAD_AFTER)) {
                            loadOrder = DependencyConfiguration.LoadOrder.BEFORE;
                        }

                        flagMap.put(entry.getKey(), new DependencyConfiguration(
                            loadOrder,
                            flags.contains(DependencyFlag.REQUIRED),
                            flags.contains(DependencyFlag.DEPENDENCY)
                        ));
                    }
                }

                value.node("dependencies").set(TYPE_TOKEN.getType(), dependencies);
                return null;
            })
            .build();
    }

    @ConfigSerializable
    record LegacyLoadConfiguration(
        @Required String name,
        boolean bootstrap
    ) {
    }

    @ConfigSerializable
    private static class LegacyConfiguration {

        private List<LegacyLoadConfiguration> loadAfter = List.of();
        private List<LegacyLoadConfiguration> loadBefore = List.of();
        private List<LegacyDependencyConfiguration> dependencies = List.of();
    }


    @ConfigSerializable
    public record LegacyDependencyConfiguration(
        @Required String name,
        boolean required,
        boolean bootstrap
    ) {
    }

    enum DependencyFlag {
        LOAD_AFTER,
        LOAD_BEFORE,
        REQUIRED,
        DEPENDENCY
    }

}
