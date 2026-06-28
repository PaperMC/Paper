package io.papermc.paper.configuration;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * Utility enabling overriding configuration files like paper-global or world files
 * via yaml files merged in later.
 * <p>
 * Overriding yaml files are under more restrictions than the main configuration files of the server, specifically,
 * overriding yaml files will not be automatically migrated to new configuration versions.
 * It is up to the administrators to keep their contents up to date.
 * <p>
 * As a consequence of this restriction, overriding configuration files are not written back to and can hence be read-only if required.
 */
public class ConfigurationOverrides {

    /**
     * Applies overrides found in the servers context to the read in root node.
     * The method will additionally attempt to map the node into the target type after each override to
     * give more specific error messages for potentially outdated overrides.
     *
     * @param rootNode           the parsed and migrated root node.
     * @param overrideIdentifier the identifier of the overrides to apply.
     * @param targetType         the type to parse the configuration into for validation.
     * @return the finally parsed target type.
     * @throws ConfigurateException if any of the overrides produces an invalid configuration that could not be parsed.
     */
    public static ConfigurationNode applyOverrides(
        final ConfigurationNode rootNode,
        final OverrideIdentifier overrideIdentifier,
        final Class<?> targetType
    ) throws ConfigurateException {
        final String overridePaths = System.getenv(overrideIdentifier.environmentVariableName());
        if (overridePaths == null || overridePaths.isBlank()) return rootNode;

        final ConfigurationNode resultNode = rootNode.copy();
        final String[] overridePathsSplit = overridePaths.split(File.pathSeparator);

        for (int i = 0; i < overridePathsSplit.length; i++) {
            final String path = overridePathsSplit[i];
            final CommentedConfigurationNode node = YamlConfigurationLoader.builder()
                .path(Path.of(path))
                .build()
                .load();

            try {
                resultNode.from(node).require(targetType);
            } catch (final SerializationException serializationException) {
                throw new ConfigurateException(
                    serializationException.path(),
                    "Override " + path + " lead to invalid configuration",
                    serializationException
                );
            }
        }

        return resultNode;
    }

    public sealed interface OverrideIdentifier permits OverrideIdentifier.WorldSpecific, OverrideIdentifier.Unit {

        String ENV_VARIABLE_PREFIX = "PAPER_CONFIG_";

        OverrideIdentifier GLOBAL = new Unit("GLOBAL");
        OverrideIdentifier WORLD_DEFAULT = new Unit("WORLD_DEFAULTS");

        record Unit(String suffix) implements OverrideIdentifier {
            @Override
            public String environmentVariableName() {
                return ENV_VARIABLE_PREFIX + suffix;
            }
        }

        record WorldSpecific(String worldPath) implements OverrideIdentifier {
            @Override
            public String environmentVariableName() {
                return ENV_VARIABLE_PREFIX + "WORLD_OVERRIDE_" + worldPath.toUpperCase(Locale.ROOT).replace(' ', '_');
            }
        }

        /**
         * {@return the environment variable name used to lookup the override paths}
         */
        String environmentVariableName();
    }

}
