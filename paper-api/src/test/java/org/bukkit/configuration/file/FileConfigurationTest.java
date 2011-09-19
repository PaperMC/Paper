package org.bukkit.configuration.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.Scanner;
import org.bukkit.configuration.MemoryConfigurationTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public abstract class FileConfigurationTest extends MemoryConfigurationTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Override
    public abstract FileConfiguration getConfig();
    
    public abstract String getTestValuesString();
    
    @Test
    public void testSave_File() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");
        
        for (Map.Entry<String, Object> entry : getTestValues().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        
        config.save(file);
        
        assertTrue(file.isFile());
    }

    @Test
    public void testSave_String() throws Exception {
        FileConfiguration config = getConfig();
        File file = testFolder.newFile("test.config");
        
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
        File file = testFolder.newFile("test.config");
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
        File file = testFolder.newFile("test.config");
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
    }
}
