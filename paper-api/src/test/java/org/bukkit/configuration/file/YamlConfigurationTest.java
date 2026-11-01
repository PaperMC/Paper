package org.bukkit.configuration.file;

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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@NullMarked
public class YamlConfigurationTest extends FileConfigurationTest {

    @Override
    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Override
    public List<@Nullable String> getTestCommentInput() {
        final List<@Nullable String> comments = new ArrayList<>();
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
        return """
            integer: -2147483648
            string: String Value
            long: 9223372036854775807
            true-boolean: true
            false-boolean: false
            vector:
              ==: Vector
              x: 12345.67
              y: 64.0
              z: -12345.6789
            list:
            - 1
            - 2
            - 3
            - 4
            - 5
            '42': The Answer
            enum:
              ==: org.bukkit.configuration.ConfigurationSectionTest$TestEnum
              variant: BANANAS
            """;
    }

    @Test
    public void testSaveToStringWithIndent() {
        final YamlConfiguration config = this.getConfig();
        config.options().indent(9);

        config.set("section.key", 1);

        final String result = config.saveToString();
        final String expected = "section:\n         key: 1\n";

        assertEquals(expected, result);
    }

    @Test
    public void testAnchorNode() throws InvalidConfigurationException {
        final YamlConfiguration config = this.getConfig();
        final String content = """
            effects:
              eff1: &novaEarth
                position: special
                effect: nova
              eff2: *novaEarth
              eff3: *novaEarth""";
        config.loadFromString(content);

        final String expected = """
            effects:
              eff1:
                position: special
                effect: nova
              eff2:
                position: special
                effect: nova
              eff3:
                position: special
                effect: nova
            """;
        assertEquals(expected, config.saveToString());
    }

    @Test
    public void testMergeNode() throws InvalidConfigurationException {
        final YamlConfiguration config = this.getConfig();
        final String content = """
            effects:
              eff1: &novaEarth
                position: special
                effect: nova
              eff2:\s
                <<: *novaEarth
                height-offset: 0
              eff3:\s
                <<: *novaEarth
                height-offset: 2""";
        config.loadFromString(content);

        final String expected = """
            effects:
              eff1:
                position: special
                effect: nova
              eff2:
                position: special
                effect: nova
                height-offset: 0
              eff3:
                position: special
                effect: nova
                height-offset: 2
            """;
        assertEquals(expected, config.saveToString());
    }

    @Test
    public void test100Comments() throws InvalidConfigurationException {
        final StringBuilder commentBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            commentBuilder.append("# Comment ").append(i).append("\n");
        }

        final String data = commentBuilder
            + "simpleKey: simpleValue\n"
            + "\n";

        final YamlConfiguration config = this.getConfig();
        config.loadFromString(data);

        final String result = config.saveToString();
        assertEquals(data, result);
    }

    // SPIGOT-6885
    @Test
    public void testReference() throws InvalidConfigurationException {
        final String okYAML = """
            dummy: test
            conf:
              - test #comment ok
            """;
        assertNotNull(this.toConfig(okYAML, false));
        assertNotNull(this.toConfig(okYAML, true));

        final String badYAML = """
            dummy: &a test
            conf:
              - *a #comment not ok here
            """;
        assertNotNull(this.toConfig(badYAML, false));
        assertNotNull(this.toConfig(badYAML, true));
    }

    private YamlConfiguration toConfig(final String contents, final boolean parseComments) throws InvalidConfigurationException {
        final YamlConfiguration config = new YamlConfiguration();
        config.options().parseComments(parseComments);
        config.loadFromString(contents);
        return config;
    }

    @Test
    public void testOnlyHeader() {
        final YamlConfiguration config = this.getConfig();
        config.options().setHeader(List.of("# Test"));

        final String result = config.saveToString();
        final String expected = "# # Test\n\n{}\n";

        assertEquals(expected, result);
    }

    @Test
    public void testUnusualMappingKeys() throws InvalidConfigurationException {
        final YamlConfiguration config = this.getConfig();
        final String content = """
            [1]: odd
            {2}: choice
            "3": of
            4: keys
            null: A
            'null': B
            !!null null: C
            '!!null': X
            """;
        // The keys are parsed as arbitrary Yaml types and then converted to their String representation during config loading.
        // Consequently, the String representation used by the loaded config might be different to how these keys were originally represented.
        // Since SnakeYaml does not preserve the original representation in its parsed node tree, we are also not able to reconstruct the original keys during config loading.
        config.loadFromString(content);

        // This difference in representations also becomes apparent when the config is written back.
        // Also note: All keys that are parsed as null overwrite each other's values.
        final String expected = """
            '[1]': odd
            '{2=null}': choice
            '3': of
            '4': keys
            'null': C
            '!!null': X
            """;
        assertEquals(expected, config.saveToString());
    }

    // SPIGOT-6949
    @Test
    public void testNestedConfigSections() throws InvalidConfigurationException {
        final YamlConfiguration config = this.getConfig();
        final List<Object> configList = new ArrayList<>();

        final MemoryConfiguration nestedSection = new MemoryConfiguration();
        nestedSection.set("something", "value");
        configList.add(nestedSection);

        final Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("scalar", 10);
        nestedMap.put("string", "something");

        final MemoryConfiguration nestedSection2 = new MemoryConfiguration();
        nestedSection2.set("embedded", "value");
        nestedMap.put("section", nestedSection2);
        configList.add(nestedMap);

        config.set("list", configList);

        final String serialized = config.saveToString();
        final YamlConfiguration deserialized = new YamlConfiguration();
        deserialized.loadFromString(serialized);

        // The types of nested maps or configuration sections or configs might not be preserved, but
        // their contents should be preserved:
        assertEquals(this.convertSectionsToMaps(config), this.convertSectionsToMaps(deserialized));
    }

    // Recursively converts all configuration sections to Maps, including within any nested data
    // structures such as Maps and Lists.
    private Object convertSectionsToMaps(final Object object) {
        switch (object) {
            case final ConfigurationSection section -> {
                final Map<String, Object> values = section.getValues(false);
                return this.convertSectionsToMaps(values);
            }
            case final Map<?, ?> map -> {
                final Map<Object, Object> newMap = new LinkedHashMap<>();
                for (final Entry<?, ?> entry : map.entrySet()) {
                    newMap.put(entry.getKey(), this.convertSectionsToMaps(entry.getValue()));
                }
                return newMap;
            }
            case final Iterable<?> iterable -> {
                // Any other type of Collection is converted to a list:
                final List<Object> newList = new ArrayList<>();
                for (final Object element : iterable) {
                    newList.add(this.convertSectionsToMaps(element));
                }
                return newList;
            }
            default -> {
                return object;
            }
        }
    }
}
