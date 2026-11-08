package org.bukkit.configuration.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.MemoryConfigurationTest;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class FileConfigurationTest extends MemoryConfigurationTest {
    @TempDir
    File testFolder;

    @Override
    public abstract FileConfiguration getConfig();

    public abstract String getTestValuesString();

    public abstract List<@Nullable String> getTestCommentInput();

    public abstract String getTestCommentResult();

    public abstract List<String> getTestHeaderComments();

    public abstract String getTestHeaderCommentsResult();

    public abstract List<String> getTestKeyComments();

    public abstract String getTestHeaderKeyCommentResult();

    @Test
    public void testSaveFile() throws Exception {
        final FileConfiguration config = this.getConfig();
        final File file = new File(this.testFolder, "test.config");
        file.createNewFile();

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file);

        assertTrue(file.isFile());
    }

    @Test
    public void testSaveString() throws Exception {
        final FileConfiguration config = this.getConfig();
        final File file = new File(this.testFolder, "test.config");
        file.createNewFile();

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file.getAbsolutePath());

        assertTrue(file.isFile());
    }

    @Test
    public void testSaveToString() {
        final FileConfiguration config = this.getConfig();

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        final String result = config.saveToString();
        final String expected = this.getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadFile() throws Exception {
        final FileConfiguration config = this.getConfig();
        final File file = new File(this.testFolder, "test.config");
        file.createNewFile();
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        final String saved = this.getTestValuesString();
        final Map<String, Object> values = this.getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoadString() throws Exception {
        final FileConfiguration config = this.getConfig();
        final File file = new File(this.testFolder, "test.config");
        file.createNewFile();
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        final String saved = this.getTestValuesString();
        final Map<String, Object> values = this.getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file.getAbsolutePath());

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoadFromString() throws Exception {
        final FileConfiguration config = this.getConfig();
        final Map<String, Object> values = this.getTestValues();
        final String saved = this.getTestValuesString();

        config.loadFromString(saved);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved, config.saveToString());
    }

    @Test
    public void testReloadEmptyConfig() throws Exception {
        FileConfiguration config = this.getConfig();

        assertEquals("", config.saveToString());

        config = this.getConfig();
        config.loadFromString("");

        assertEquals("", config.saveToString());

        config = this.getConfig();
        config.loadFromString("\n\n"); // Should trim the first newlines of a header

        assertEquals("", config.saveToString());
    }

    @Test
    public void testReloadClear() throws Exception {
        // Test for SPIGOT-6274 - load does not clear values
        final FileConfiguration config = this.getConfig();

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        config.set("test", true);
        assertTrue(config.contains("test"));
        assertTrue(config.getBoolean("test"));

        config.loadFromString("");
        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));
    }

    @Test
    public void testReloadClear2() throws Exception {
        // Test for SPIGOT-6274 - load does not clear values
        final FileConfiguration config = this.getConfig();

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        config.set("test", true);
        assertTrue(config.contains("test"));
        assertTrue(config.getBoolean("test"));

        config.loadFromString("other: false"); // Test both null and non-null code paths
        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));
    }

    @Test
    public void testReloadClear3() throws Exception {
        // Test for SPIGOT-6274 - load does not clear values
        final FileConfiguration config = this.getConfig();

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        config.set("test", true);
        assertTrue(config.contains("test"));
        assertTrue(config.getBoolean("test"));

        config.loadFromString("other: false\nsection:\n  value: true"); // SPIGOT-6313: Test with section
        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));

        assertTrue(config.contains("other"));
        assertTrue(config.contains("section"));
        assertTrue(config.contains("section.value"));
        assertTrue(config.getBoolean("section.value"));

        assertFalse(config.contains("test"));
        assertFalse(config.getBoolean("test"));
    }

    @Test
    public void testSaveWithComments() {
        final FileConfiguration config = this.getConfig();
        config.options().parseComments(true);

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        final String key = this.getTestValues().keySet().iterator().next();
        config.setComments(key, this.getTestCommentInput());

        final String result = config.saveToString();
        final String expected = this.getTestCommentResult() + "\n" + this.getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testSaveWithoutComments() {
        final FileConfiguration config = this.getConfig();
        config.options().parseComments(false);

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        final String key = this.getTestValues().keySet().iterator().next();
        config.setComments(key, this.getTestCommentInput());

        final String result = config.saveToString();
        final String expected = this.getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithComments() throws Exception {
        final FileConfiguration config = this.getConfig();
        final Map<String, Object> values = this.getTestValues();
        final String saved = this.getTestValuesString();
        final String comments = this.getTestCommentResult();

        config.options().parseComments(true);
        config.loadFromString(comments + "\n" + saved);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(comments + "\n" + saved, config.saveToString());
    }

    @Test
    public void testLoadWithoutComments() throws Exception {
        final FileConfiguration config = this.getConfig();
        final Map<String, Object> values = this.getTestValues();
        final String saved = this.getTestValuesString();
        final String comments = this.getTestCommentResult();

        config.options().parseComments(false);
        config.loadFromString(comments + "\n" + saved);
        config.options().parseComments(true);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved, config.saveToString());
    }

    @Test
    public void testSaveWithCommentsHeader() {
        final FileConfiguration config = this.getConfig();
        config.options().parseComments(true);

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        final String key = this.getTestValues().keySet().iterator().next();
        config.options().setHeader(this.getTestHeaderComments());
        config.setComments(key, this.getTestKeyComments());

        final String result = config.saveToString();
        final String expected = this.getTestHeaderKeyCommentResult() + this.getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithCommentsHeader() throws Exception {
        final FileConfiguration config = this.getConfig();
        final Map<String, Object> values = this.getTestValues();
        final String saved = this.getTestValuesString();
        final String comments = this.getTestHeaderKeyCommentResult();

        config.options().parseComments(true);
        config.loadFromString(comments + saved);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        final String key = this.getTestValues().keySet().iterator().next();
        assertEquals(this.getTestHeaderComments(), config.options().getHeader());
        assertEquals(this.getTestKeyComments(), config.getComments(key));

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(comments + saved, config.saveToString());
    }

    @Test
    public void testSaveWithCommentsFooter() {
        final FileConfiguration config = this.getConfig();
        config.options().parseComments(true);

        for (final Map.Entry<String, Object> entry : this.getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        config.options().setFooter(this.getTestHeaderComments());

        final String result = config.saveToString();
        final String expected = this.getTestValuesString() + this.getTestHeaderCommentsResult();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithCommentsFooter() throws Exception {
        final FileConfiguration config = this.getConfig();
        final Map<String, Object> values = this.getTestValues();
        final String saved = this.getTestValuesString();
        final String comments = this.getTestHeaderCommentsResult();

        config.options().parseComments(true);
        config.loadFromString(saved + comments);

        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(this.getTestHeaderComments(), config.options().getFooter());

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved + comments, config.saveToString());
    }

    @Test
    public void testLoadWithCommentsInline() throws Exception {
        final FileConfiguration config = this.getConfig();

        config.options().parseComments(true);
        config.loadFromString("key1: value1\nkey2: value2 # Test inline\nkey3: value3");

        assertEquals(List.of("Test inline"), config.getInlineComments("key2"));
    }

    @Test
    public void testSaveWithCommentsInline() {
        final FileConfiguration config = this.getConfig();

        config.options().parseComments(true);
        config.set("key1", "value1");
        config.set("key2", "value2");
        config.set("key3", "value3");
        config.setInlineComments("key2", List.of("Test inline"));

        final String result = config.saveToString();
        final String expected = "key1: value1\nkey2: value2 # Test inline\nkey3: value3\n";

        assertEquals(expected, result);
    }

}
