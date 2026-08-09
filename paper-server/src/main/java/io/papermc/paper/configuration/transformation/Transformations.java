package io.papermc.paper.configuration.transformation;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.Configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static com.google.common.base.Preconditions.checkState;
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

    public static ConfigurationTransformation single(final NodePath path, final TransformAction action) {
        return ConfigurationTransformation.builder().addAction(path, action).build();
    }

    public static TransformAction setRaw(final Object value) {
        return (path, node) -> {
            node.raw(value);
            return null;
        };
    }

    public static NodePath prefix(final String prefix, final String... other) {
        final List<String> list = new ArrayList<>(other.length + 1);
        list.add(prefix);
        list.addAll(Arrays.asList(other));
        return NodePath.of(list);
    }

    public static TransformAction move(final NodePath to) {
        return (path, node) -> to.array();
    }

    // replaces the last path element with all the arguments
    public static TransformAction renameDeep(final String first, final String... other) {
        return (path, node) -> {
            final Object[] current = path.array();
            final Object[] newPath = new Object[current.length + other.length];
            System.arraycopy(current, 0, newPath, 0, current.length - 1);
            newPath[current.length - 1] = first;
            System.arraycopy(other, 0, newPath, current.length, other.length);
            return newPath;
        };
    }

    public static TransformAction move(final TransformAction action, final NodePath newPath) {
        return (path, node) -> {
            action.visitPath(path, node);
            return newPath.array();
        };
    }

    public static TransformAction moveParent(final String... parents) {
        return (path, value) -> {
            checkState(path.size() > 0, "can't move root");
            final NodePath newPath = path((Object[]) parents);
            return newPath.withAppendedChild(path.get(path.size() - 1)).array();
        };
    }
}
