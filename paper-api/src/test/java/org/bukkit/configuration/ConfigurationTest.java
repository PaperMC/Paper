package org.bukkit.configuration;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

public abstract class ConfigurationTest {

    static {
        ConfigurationSerialization.registerClass(ConfigurationSectionTest.TestEnum.class);
    }

    public abstract Configuration getConfig();

    public Map<String, Object> getTestValues() {
        HashMap<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("integer", Integer.MIN_VALUE);
        result.put("string", "String Value");
        result.put("long", Long.MAX_VALUE);
        result.put("true-boolean", true);
        result.put("false-boolean", false);
        result.put("vector", new Vector(12345.67, 64, -12345.6789));
        result.put("list", Arrays.asList(1, 2, 3, 4, 5));
        result.put("42", "The Answer");
        result.put("enum", ConfigurationSectionTest.TestEnum.BANANAS);

        return result;
    }

    /**
     * Test of addDefault method, of class Configuration.
     */
    @Test
    public void testAddDefault() {
        Configuration config = getConfig();
        Map<String, Object> values = getTestValues();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();

            config.addDefault(path, object);

            assertEquals(object, config.get(path));
            assertTrue(config.contains(path));
            assertFalse(config.isSet(path));
            assertTrue(config.getDefaults().isSet(path));
        }
    }

    /**
     * Test of addDefaults method, of class Configuration.
     */
    @Test
    public void testAddDefaults_Map() {
        Configuration config = getConfig();
        Map<String, Object> values = getTestValues();

        config.addDefaults(values);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();

            assertEquals(object, config.get(path));
            assertTrue(config.contains(path));
            assertFalse(config.isSet(path));
            assertTrue(config.getDefaults().isSet(path));
        }
    }

    /**
     * Test of addDefaults method, of class Configuration.
     */
    @Test
    public void testAddDefaults_Configuration() {
        Configuration config = getConfig();
        Map<String, Object> values = getTestValues();
        Configuration defaults = getConfig();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            defaults.set(entry.getKey(), entry.getValue());
        }

        config.addDefaults(defaults);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();

            assertEquals(object, config.get(path));
            assertTrue(config.contains(path));
            assertFalse(config.isSet(path));
            assertTrue(config.getDefaults().isSet(path));
        }
    }

    /**
     * Test of addDefaults method, of class Configuration but with existing
     * defaults in a child section.
     */
    @Test
    public void testAddDefaults_Configuration_WithExisting() {
        Configuration config = getConfig();
        Map<String, Object> values = getTestValues();
        values.put("default-section.string", "String Value");
        Configuration defaults = getConfig();
        Configuration defaultsAdditional = getConfig();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            defaults.set(entry.getKey(), entry.getValue());
        }
        config.addDefaults(defaults);

        Map<String, Object> additionalValues = new HashMap<>();
        additionalValues.put("default-section.additionalString", "Additional String");
        additionalValues.put("default-section.additionalInt", 42);
        for (Map.Entry<String, Object> entry : additionalValues.entrySet()) {
            defaultsAdditional.set(entry.getKey(), entry.getValue());
        }
        config.addDefaults(defaultsAdditional);
        values.putAll(additionalValues);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();

            assertEquals(object, config.get(path));
            assertTrue(config.contains(path));
            assertFalse(config.isSet(path));
            assertTrue(config.getDefaults().isSet(path));
        }
    }

    /**
     * Test of setDefaults method, of class Configuration.
     */
    @Test
    public void testSetDefaults() {
        Configuration config = getConfig();
        Map<String, Object> values = getTestValues();
        Configuration defaults = getConfig();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            defaults.set(entry.getKey(), entry.getValue());
        }

        config.setDefaults(defaults);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();

            assertEquals(object, config.get(path));
            assertTrue(config.contains(path));
            assertFalse(config.isSet(path));
            assertTrue(config.getDefaults().isSet(path));
        }
    }

    /**
     * Test creation of ConfigurationSection
     */
    @Test
    public void testCreateSection() {
        Configuration config = getConfig();

        Set<String> set = new HashSet<String>();
        set.add("this");
        set.add("this.test.sub");
        set.add("this.test");
        set.add("this.test.other");

        config.createSection("this.test.sub");
        config.createSection("this.test.other");

        assertEquals(set, config.getKeys(true));
    }

    /**
     * Test of getDefaults method, of class Configuration.
     */
    @Test
    public void testGetDefaults() {
        Configuration config = getConfig();
        Configuration defaults = getConfig();

        config.setDefaults(defaults);

        assertEquals(defaults, config.getDefaults());
    }
}
