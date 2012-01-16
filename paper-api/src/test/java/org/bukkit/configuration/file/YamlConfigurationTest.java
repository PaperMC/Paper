package org.bukkit.configuration.file;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import org.junit.Test;
import static org.junit.Assert.*;

public class YamlConfigurationTest extends FileConfigurationTest {
    @Override
    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Override
    public String getTestHeaderInput() {
        return "This is a sample\nheader.\n\nNewline above should be commented.\n\n";
    }

    @Override
    public String getTestHeaderResult() {
        return "# This is a sample\n# header.\n# \n# Newline above should be commented.\n\n";
    }

    @Override
    public String getTestValuesString() {
        return "integer: -2147483648\n" +
            "string: String Value\n" +
            "long: 9223372036854775807\n" +
            "true-boolean: true\n" +
            "false-boolean: false\n" +
            "vector:\n" +
            "  ==: Vector\n" +
            "  x: 12345.67\n" +
            "  y: 64.0\n" +
            "  z: -12345.6789\n" +
            "list:\n" +
            "- 1\n" +
            "- 2\n" +
            "- 3\n" +
            "- 4\n" +
            "- 5\n" +
            "'42': The Answer\n";
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
    public void testSaveRestoreCompositeList() throws InvalidConfigurationException {
        YamlConfiguration out = getConfig();

        List<ItemStack> stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(1));
        stacks.add(new ItemStack(2));
        stacks.add(new ItemStack(3));

        out.set("composite-list.abc.def", stacks);
        String yaml = out.saveToString();

        YamlConfiguration in = new YamlConfiguration();
        in.loadFromString(yaml);
        List<Object> raw = in.getList("composite-list.abc.def");

        assertEquals(stacks.size(), raw.size());
        assertEquals(stacks.get(0), raw.get(0));
        assertEquals(stacks.get(1), raw.get(1));
        assertEquals(stacks.get(2), raw.get(2));
    }
}
