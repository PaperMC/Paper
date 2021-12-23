package org.bukkit.configuration.file;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

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
}
