package org.bukkit.configuration.file;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.MemoryConfigurationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public abstract class FileConfigurationTest extends MemoryConfigurationTest {
    @TempDir
    public File testFolder;

    @Override
    public abstract FileConfiguration getConfig();

    public abstract String getTestValuesString();

    public abstract List<String> getTestCommentInput();

    public abstract String getTestCommentResult();

    public abstract List<String> getTestHeaderComments();

    public abstract String getTestHeaderCommentsResult();

    public abstract List<String> getTestKeyComments();

    public abstract String getTestHeaderKeyCommentResult();

    @Test
    public void testSave_File() throws Exception {
        FileConfiguration config = getConfig();
        File file = new File(testFolder, "test.config");
        file.createNewFile();

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file);

        assertTrue(file.isFile());
    }

    @Test
    public void testSave_String() throws Exception {
        FileConfiguration config = getConfig();
        File file = new File(testFolder, "test.config");
        file.createNewFile();

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file.getAbsolutePath());

        assertTrue(file.isFile());
    }

    @Test
    public void testSaveToString() {
        FileConfiguration config = getConfig();

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        String result = config.saveToString();
        String expected = getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoad_File() throws Exception {
        FileConfiguration config = getConfig();
        File file = new File(testFolder, "test.config");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String saved = getTestValuesString();
        Map<String, Object> values = getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoad_String() throws Exception {
        FileConfiguration config = getConfig();
        File file = new File(testFolder, "test.config");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String saved = getTestValuesString();
        Map<String, Object> values = getTestValues();

        try {
            writer.write(saved);
        } finally {
            writer.close();
        }

        config.load(file.getAbsolutePath());

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
    }

    @Test
    public void testLoadFromString() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();

        config.loadFromString(saved);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved, config.saveToString());
    }

    @Test
    public void testReloadEmptyConfig() throws Exception {
        FileConfiguration config = getConfig();

        assertEquals("", config.saveToString());

        config = getConfig();
        config.loadFromString("");

        assertEquals("", config.saveToString());

        config = getConfig();
        config.loadFromString("\n\n"); // Should trim the first newlines of a header

        assertEquals("", config.saveToString());
    }

    @Test
    public void testReloadClear() throws Exception {
        // Test for SPIGOT-6274 - load does not clear values
        FileConfiguration config = getConfig();

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
        FileConfiguration config = getConfig();

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
        FileConfiguration config = getConfig();

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
        FileConfiguration config = getConfig();
        config.options().parseComments(true);

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        String key = getTestValues().keySet().iterator().next();
        config.setComments(key, getTestCommentInput());

        String result = config.saveToString();
        String expected = getTestCommentResult() + "\n" + getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testSaveWithoutComments() {
        FileConfiguration config = getConfig();
        config.options().parseComments(false);

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        String key = getTestValues().keySet().iterator().next();
        config.setComments(key, getTestCommentInput());

        String result = config.saveToString();
        String expected = getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithComments() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String comments = getTestCommentResult();

        config.options().parseComments(true);
        config.loadFromString(comments + "\n" + saved);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(comments + "\n" + saved, config.saveToString());
    }

    @Test
    public void testLoadWithoutComments() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String comments = getTestCommentResult();

        config.options().parseComments(false);
        config.loadFromString(comments + "\n" + saved);
        config.options().parseComments(true);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved, config.saveToString());
    }

    @Test
    public void testSaveWithCommentsHeader() {
        FileConfiguration config = getConfig();
        config.options().parseComments(true);

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        String key = getTestValues().keySet().iterator().next();
        config.options().setHeader(getTestHeaderComments());
        config.setComments(key, getTestKeyComments());

        String result = config.saveToString();
        String expected = getTestHeaderKeyCommentResult() + getTestValuesString();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithCommentsHeader() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String comments = getTestHeaderKeyCommentResult();

        config.options().parseComments(true);
        config.loadFromString(comments + saved);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        String key = getTestValues().keySet().iterator().next();
        assertEquals(getTestHeaderComments(), config.options().getHeader());
        assertEquals(getTestKeyComments(), config.getComments(key));

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(comments + saved, config.saveToString());
    }

    @Test
    public void testSaveWithCommentsFooter() {
        FileConfiguration config = getConfig();
        config.options().parseComments(true);

        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        config.options().setFooter(getTestHeaderComments());

        String result = config.saveToString();
        String expected = getTestValuesString() + getTestHeaderCommentsResult();

        assertEquals(expected, result);
    }

    @Test
    public void testLoadWithCommentsFooter() throws Exception {
        FileConfiguration config = getConfig();
        Map<String, Object> values = getTestValues();
        String saved = getTestValuesString();
        String comments = getTestHeaderCommentsResult();

        config.options().parseComments(true);
        config.loadFromString(saved + comments);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            assertEquals(entry.getValue(), config.get(entry.getKey()));
        }

        assertEquals(getTestHeaderComments(), config.options().getFooter());

        assertEquals(values.keySet(), config.getKeys(true));
        assertEquals(saved + comments, config.saveToString());
    }

    @Test
    public void testLoadWithCommentsInline() throws Exception {
        FileConfiguration config = getConfig();

        config.options().parseComments(true);
        config.loadFromString("key1: value1\nkey2: value2 # Test inline\nkey3: value3");

        assertEquals(Arrays.asList("Test inline"), config.getInlineComments("key2"));
    }

    @Test
    public void testSaveWithCommentsInline() {
        FileConfiguration config = getConfig();

        config.options().parseComments(true);
        config.set("key1", "value1");
        config.set("key2", "value2");
        config.set("key3", "value3");
        config.setInlineComments("key2", Arrays.asList("Test inline"));

        String result = config.saveToString();
        String expected = "key1: value1\nkey2: value2 # Test inline\nkey3: value3\n";

        assertEquals(expected, result);
    }

}
