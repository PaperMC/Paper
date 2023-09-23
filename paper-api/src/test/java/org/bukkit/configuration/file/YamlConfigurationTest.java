package org.bukkit.configuration.file;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class YamlConfigurationTest extends FileConfigurationTest {

    @Override
    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Override
    public List<String> getTestCommentInput() {
        List<String> comments = new ArrayList<>();
        comments.add("This is a sample");
        comments.add("header.");
        comments.add("Newline above should be commented.");
        comments.add("");
        comments.add("");
        comments.add(null);
        comments.add(null);
        comments.add("Comment of first Key");
        comments.add("and a second line.");
        return comments;
    }

    @Override
    public String getTestCommentResult() {
        return "# This is a sample\n# header.\n# Newline above should be commented.\n#\n#\n\n\n# Comment of first Key\n# and a second line.";
    }

    @Override
    public List<String> getTestHeaderComments() {
        return Arrays.asList("Header", "Second Line");
    }

    @Override
    public String getTestHeaderCommentsResult() {
        return "# Header\n# Second Line\n";
    }

    @Override
    public List<String> getTestKeyComments() {
        return Arrays.asList("First key Comment", "Second Line");
    }

    @Override
    public String getTestHeaderKeyCommentResult() {
        return "# Header\n# Second Line\n\n# First key Comment\n# Second Line\n";
    }

    @Override
    public String getTestValuesString() {
        return "integer: -2147483648\n"
                + "string: String Value\n"
                + "long: 9223372036854775807\n"
                + "true-boolean: true\n"
                + "false-boolean: false\n"
                + "vector:\n"
                + "  ==: Vector\n"
                + "  x: 12345.67\n"
                + "  y: 64.0\n"
                + "  z: -12345.6789\n"
                + "list:\n"
                + "- 1\n"
                + "- 2\n"
                + "- 3\n"
                + "- 4\n"
                + "- 5\n"
                + "'42': The Answer\n"
                + "enum:\n"
                + "  ==: org.bukkit.configuration.ConfigurationSectionTest$TestEnum\n"
                + "  variant: BANANAS\n";
    }

    @Test
    public void testSaveToStringWithIndent() {
        YamlConfiguration config = getConfig();
        config.options().indent(9);

        config.set("section.key", 1);

        String result = config.saveToString();
        String expected = "section:\n         key: 1\n";

        assertEquals(expected, result);
    }

    @Test
    public void testAnchorNode() throws InvalidConfigurationException {
        YamlConfiguration config = getConfig();
        String content = "effects:\n"
                + "  eff1: &novaEarth\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "  eff2: *novaEarth\n"
                + "  eff3: *novaEarth";
        config.loadFromString(content);

        String expected = "effects:\n"
                + "  eff1:\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "  eff2:\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "  eff3:\n"
                + "    position: special\n"
                + "    effect: nova\n";
        assertEquals(expected, config.saveToString());
    }

    @Test
    public void testMergeNode() throws InvalidConfigurationException {
        YamlConfiguration config = getConfig();
        String content = "effects:\n"
                + "  eff1: &novaEarth\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "  eff2: \n"
                + "    <<: *novaEarth\n"
                + "    height-offset: 0\n"
                + "  eff3: \n"
                + "    <<: *novaEarth\n"
                + "    height-offset: 2";
        config.loadFromString(content);

        String expected = "effects:\n"
                + "  eff1:\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "  eff2:\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "    height-offset: 0\n"
                + "  eff3:\n"
                + "    position: special\n"
                + "    effect: nova\n"
                + "    height-offset: 2\n";
        assertEquals(expected, config.saveToString());
    }

    @Test
    public void test100Comments() throws InvalidConfigurationException {
        StringBuilder commentBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            commentBuilder.append("# Comment ").append(i).append("\n");
        }

        final String data = ""
                + commentBuilder
                + "simpleKey: simpleValue\n"
                + "\n";

        YamlConfiguration config = getConfig();
        config.loadFromString(data);

        String result = config.saveToString();
        assertEquals(data, result);
    }

    // SPIGOT-6885
    @Test
    public void testReference() throws InvalidConfigurationException {
        String okYAML = "dummy: test\n"
                + "conf:\n"
                + "  - test #comment ok\n";
        assertNotNull(toConfig(okYAML, false));
        assertNotNull(toConfig(okYAML, true));

        String badYAML = "dummy: &a test\n"
                + "conf:\n"
                + "  - *a #comment not ok here\n";
        assertNotNull(toConfig(badYAML, false));
        assertNotNull(toConfig(badYAML, true));
    }

    private YamlConfiguration toConfig(@NotNull String contents, boolean parseComments) throws InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();
        config.options().parseComments(parseComments);
        config.loadFromString(contents);
        return config;
    }

    @Test
    public void testOnlyHeader() {
        YamlConfiguration config = getConfig();
        config.options().header("# Test");

        String result = config.saveToString();
        String expected = "# # Test\n\n{}\n";

        assertEquals(expected, result);
    }

    @Test
    public void testUnusualMappingKeys() throws InvalidConfigurationException {
        YamlConfiguration config = getConfig();
        String content = "[1]: odd\n"
                + "{2}: choice\n"
                + "\"3\": of\n"
                + "4: keys\n"
                + "null: A\n"
                + "'null': B\n"
                + "!!null null: C\n"
                + "'!!null': X\n";
        // The keys are parsed as arbitrary Yaml types and then converted to their String representation during config loading.
        // Consequently, the String representation used by the loaded config might be different to how these keys were originally represented.
        // Since SnakeYaml does not preserve the original representation in its parsed node tree, we are also not able to reconstruct the original keys during config loading.
        config.loadFromString(content);

        // This difference in representations also becomes apparent when the config is written back.
        // Also note: All keys that are parsed as null overwrite each other's values.
        String expected = "'[1]': odd\n"
                + "'{2=null}': choice\n"
                + "'3': of\n"
                + "'4': keys\n"
                + "'null': C\n"
                + "'!!null': X\n";
        assertEquals(expected, config.saveToString());
    }

    // SPIGOT-6949
    @Test
    public void testNestedConfigSections() throws InvalidConfigurationException {
        YamlConfiguration config = getConfig();
        List<Object> configList = new ArrayList<>();

        MemoryConfiguration nestedSection = new MemoryConfiguration();
        nestedSection.set("something", "value");
        configList.add(nestedSection);

        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("scalar", 10);
        nestedMap.put("string", "something");

        MemoryConfiguration nestedSection2 = new MemoryConfiguration();
        nestedSection2.set("embedded", "value");
        nestedMap.put("section", nestedSection2);
        configList.add(nestedMap);

        config.set("list", configList);

        String serialized = config.saveToString();
        YamlConfiguration deserialized = new YamlConfiguration();
        deserialized.loadFromString(serialized);

        // The types of nested maps or configuration sections or configs might not be preserved, but
        // their contents should be preserved:
        assertEquals(convertSectionsToMaps(config), convertSectionsToMaps(deserialized));
    }

    // Recursively converts all configuration sections to Maps, including within any nested data
    // structures such as Maps and Lists.
    private Object convertSectionsToMaps(Object object) {
        if (object instanceof ConfigurationSection) {
            ConfigurationSection section = (ConfigurationSection) object;
            Map<String, Object> values = section.getValues(false);
            return convertSectionsToMaps(values);
        } else if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object; // Might be immutable
            Map<Object, Object> newMap = new LinkedHashMap<>();
            for (Entry<?, ?> entry : map.entrySet()) {
                newMap.put(entry.getKey(), convertSectionsToMaps(entry.getValue()));
            }
            return newMap;
        } else if (object instanceof Iterable) {
            // Any other type of Collection is converted to a list:
            Iterable<?> iterable = (Iterable<?>) object; // Might be immutable
            List<Object> newList = new ArrayList<>();
            for (Object element : iterable) {
                newList.add(convertSectionsToMaps(element));
            }
            return newList;
        } else {
            return object;
        }
    }
}
