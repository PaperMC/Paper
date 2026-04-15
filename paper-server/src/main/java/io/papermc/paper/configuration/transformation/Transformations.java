package io.papermc.paper.configuration.transformation;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.Configurations;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public final class Transformations {
    private Transformations() {
    }

    public static void moveFromRoot(final ConfigurationTransformation.Builder builder, final String key, final String... parents) {
        moveFromRootAndRename(builder, key, key, parents);
    }

    public static void moveFromRootAndRename(final ConfigurationTransformation.Builder builder, final String oldKey, final String newKey, final String... parents) {
        moveFromRootAndRename(builder, path(oldKey), newKey, parents);
    }

    public static void moveFromRootAndRename(final ConfigurationTransformation.Builder builder, final NodePath oldKey, final String newKey, final String... parents) {
        builder.addAction(oldKey, (path, value) -> {
            final Object[] newPath = new Object[parents.length + 1];
            newPath[parents.length] = newKey;
            System.arraycopy(parents, 0, newPath, 0, parents.length);
            return newPath;
        });
    }

    public static ConfigurationTransformation.VersionedBuilder versionedBuilder() {
        return ConfigurationTransformation.versionedBuilder().versionKey(Configuration.VERSION_FIELD);
    }

    @FunctionalInterface
    public interface DefaultsAware {
        void apply(final ConfigurationTransformation.Builder builder, final Configurations.ContextMap contextMap, final ConfigurationNode defaultsNode);
    }
}
