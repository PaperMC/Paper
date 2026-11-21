package org.bukkit.configuration.file;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConstructor extends SafeConstructor {

    /**
     * @deprecated options required
     */
    @Deprecated(since = "1.19.4")
    public YamlConstructor() {
        this(new LoaderOptions());
    }

    public YamlConstructor(@NotNull LoaderOptions loaderOptions) {
        super(loaderOptions);
        this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
    }

    @Override
    public void flattenMapping(@NotNull final MappingNode node) {
        super.flattenMapping(node);
    }

    @Nullable
    public Object construct(@NotNull Node node) {
        return constructObject(node);
    }

    // Paper start - drop the node tree after loading
    private static final Node EMPTY_NODE = new ScalarNode(Tag.NULL, "", null, null, DumperOptions.ScalarStyle.PLAIN);

    /**
     * Clears the node caches filled by {@link #construct(Node)}.
     * Without this the constructor keeps every parsed node (and its marks)
     * alive for as long as the configuration is loaded.
     * <p>
     * The caches are private to SnakeYAML, and {@link #constructDocument(Node)}
     * is the only way to clear them, so construct an empty document.
     */
    void clearCache() {
        this.constructDocument(EMPTY_NODE);
    }
    // Paper end - drop the node tree after loading

    private class ConstructCustomObject extends ConstructYamlMap {

        @Nullable
        @Override
        public Object construct(@NotNull Node node) {
            if (node.isTwoStepsConstruction()) {
                throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
            }

            Map<?, ?> raw = (Map<?, ?>) super.construct(node);

            if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                Map<String, Object> typed = new LinkedHashMap<String, Object>(raw.size());
                for (Map.Entry<?, ?> entry : raw.entrySet()) {
                    typed.put(entry.getKey().toString(), entry.getValue());
                }

                try {
                    return ConfigurationSerialization.deserializeObject(typed);
                } catch (IllegalArgumentException ex) {
                    throw new YAMLException("Could not deserialize object", ex);
                }
            }

            return raw;
        }

        @Override
        public void construct2ndStep(@NotNull Node node, @NotNull Object object) {
            throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
        }
    }
}
