package org.bukkit.configuration;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

public abstract class ConfigurationSectionTest {
    public abstract ConfigurationSection getConfigurationSection();

    @Test
    public void testGetKeys() {
        ConfigurationSection section = getConfigurationSection();

        section.set("key", true);
        section.set("subsection.subkey", true);
        section.set("subsection.subkey2", true);
        section.set("subsection.subsubsection.key", true);
        section.set("key2", true);
        section.set("42", true);

        assertArrayEquals(new String[]{"key", "subsection", "key2", "42"}, section.getKeys(false).toArray());
        assertArrayEquals(new String[]{"key", "subsection", "subsection.subkey", "subsection.subkey2", "subsection.subsubsection", "subsection.subsubsection.key", "key2", "42"}, section.getKeys(true).toArray());
        assertArrayEquals(new String[]{"subkey", "subkey2", "subsubsection", "subsubsection.key"}, section.getConfigurationSection("subsection").getKeys(true).toArray());
    }

    @Test
    public void testGetKeysWithDefaults() {
        ConfigurationSection section = getConfigurationSection();
        section.getRoot().options().copyDefaults(true);

        section.set("key", true);
        section.addDefault("subsection.subkey", true);
        section.addDefault("subsection.subkey2", true);
        section.addDefault("subsection.subsubsection.key", true);
        section.addDefault("key2", true);

        assertArrayEquals(new String[]{"subsection", "key2", "key"}, section.getKeys(false).toArray());
        assertArrayEquals(new String[]{"subsection", "subsection.subkey", "subsection.subkey2", "subsection.subsubsection", "subsection.subsubsection.key", "key2", "key"}, section.getKeys(true).toArray());
        assertArrayEquals(new String[]{"subkey", "subkey2", "subsubsection", "subsubsection.key"}, section.getConfigurationSection("subsection").getKeys(true).toArray());
    }

    @Test
    public void testGetValues() {
        ConfigurationSection section = getConfigurationSection();

        section.set("bool", true);
        section.set("subsection.string", "test");
        section.set("subsection.long", Long.MAX_VALUE);
        section.set("int", 42);

        Map<String, Object> shallowValues = section.getValues(false);
        assertArrayEquals(new String[]{"bool", "subsection", "int"}, shallowValues.keySet().toArray());
        assertArrayEquals(new Object[]{true, section.getConfigurationSection("subsection"), 42}, shallowValues.values().toArray());

        Map<String, Object> deepValues = section.getValues(true);
        assertArrayEquals(new String[]{"bool", "subsection", "subsection.string", "subsection.long", "int"}, deepValues.keySet().toArray());
        assertArrayEquals(new Object[]{true, section.getConfigurationSection("subsection"), "test", Long.MAX_VALUE, 42}, deepValues.values().toArray());
    }

    @Test
    public void testGetValuesWithDefaults() {
        ConfigurationSection section = getConfigurationSection();
        section.getRoot().options().copyDefaults(true);

        // Fix for SPIGOT-4558 means that defaults will always be first
        // This is a little bit unintuitive for section defaults when deep iterating keys / values as shown below
        // But the API doesn't guarantee order & when serialized (using shallow getters) all is well
        section.set("bool", true);
        section.set("subsection.string", "test");
        section.addDefault("subsection.long", Long.MAX_VALUE);
        section.addDefault("int", 42);

        Map<String, Object> shallowValues = section.getValues(false);
        assertArrayEquals(new String[]{"int", "bool", "subsection"}, shallowValues.keySet().toArray());
        assertArrayEquals(new Object[]{42, true, section.getConfigurationSection("subsection")}, shallowValues.values().toArray());

        Map<String, Object> deepValues = section.getValues(true);
        assertArrayEquals(new String[]{"subsection.long", "int", "bool", "subsection", "subsection.string"}, deepValues.keySet().toArray());
        assertArrayEquals(new Object[]{Long.MAX_VALUE, 42, true, section.getConfigurationSection("subsection"), "test"}, deepValues.values().toArray());
    }

    @Test
    public void testContains() {
        ConfigurationSection section = getConfigurationSection();

        section.set("exists", true);

        assertTrue(section.contains("exists"));
        assertFalse(section.contains("doesnt-exist"));

        assertTrue(section.contains("exists", true));
        assertTrue(section.contains("exists", false));

        assertFalse(section.contains("doesnt-exist", true));
        assertFalse(section.contains("doesnt-exist", false));

        section.addDefault("doenst-exist-two", true);
        section.set("doenst-exist-two", null);

        assertFalse(section.contains("doenst-exist-two", true));
        assertTrue(section.contains("doenst-exist-two", false));
    }

    @Test
    public void testContainsDoesNotCreateSection() {
        ConfigurationSection section = getConfigurationSection();
        section.addDefault("notExistingSection.Value", "Test String");

        assertFalse(section.contains("notExistingSection", true));
        assertFalse(section.contains("notExistingSection.Value", true));
        assertFalse(section.contains("notExistingSection", true));
    }

    @Test
    public void testIsSet() {
        ConfigurationSection section = getConfigurationSection();

        section.set("notDefault", true);
        section.getRoot().addDefault("default", true);
        section.getRoot().addDefault("defaultAndSet", true);
        section.set("defaultAndSet", true);

        assertTrue(section.isSet("notDefault"));
        assertFalse(section.isSet("default"));
        assertTrue(section.isSet("defaultAndSet"));
    }

    @Test
    public void testGetCurrentPath() {
        ConfigurationSection section = getConfigurationSection();

        assertEquals(section.getName(), section.getCurrentPath());
    }

    @Test
    public void testGetName() {
        ConfigurationSection section = getConfigurationSection().createSection("subsection");

        assertEquals("subsection", section.getName());
        assertEquals("", section.getRoot().getName());
    }

    @Test
    public void testGetRoot() {
        ConfigurationSection section = getConfigurationSection();

        assertNotNull(section.getRoot());
        assertTrue(section.getRoot().contains(section.getCurrentPath()));
    }

    @Test
    public void testGetParent() {
        ConfigurationSection section = getConfigurationSection();
        ConfigurationSection subsection = section.createSection("subsection");

        assertEquals(section.getRoot(), section.getParent());
        assertEquals(section, subsection.getParent());
    }

    @Test
    public void testGet_String() {
        ConfigurationSection section = getConfigurationSection();

        section.set("exists", "hello world");

        assertEquals("hello world", section.getString("exists"));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGet_String_Object() {
        ConfigurationSection section = getConfigurationSection();

        section.set("exists", "Set Value");

        assertEquals("Set Value", section.get("exists", "Default Value"));
        assertEquals("Default Value", section.get("doesntExist", "Default Value"));
    }

    @Test
    public void testSet() {
        ConfigurationSection section = getConfigurationSection();

        section.set("exists", "hello world");

        assertTrue(section.contains("exists"));
        assertTrue(section.isSet("exists"));
        assertEquals("hello world", section.get("exists"));

        section.set("exists", null);

        assertFalse(section.contains("exists"));
        assertFalse(section.isSet("exists"));
    }

    @Test
    public void testCreateSection() {
        ConfigurationSection section = getConfigurationSection();
        ConfigurationSection subsection = section.createSection("subsection");

        assertEquals("subsection", subsection.getName());
    }

    @Test
    public void testSectionMap() {
        ConfigurationSection config = getConfigurationSection();
        Map<String, Object> testMap = new LinkedHashMap<String, Object>();

        testMap.put("string", "Hello World");
        testMap.put("integer", 15);

        config.createSection("test.path", testMap);

        assertEquals(testMap, config.getConfigurationSection("test.path").getValues(false));
    }

    @Test
    public void testGetString_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        String value = "Hello World";

        section.set(key, value);

        assertEquals(value, section.getString(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetString_String_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        String value = "Hello World";
        String def = "Default Value";

        section.set(key, value);

        assertEquals(value, section.getString(key, def));
        assertEquals(def, section.getString("doesntExist", def));
    }

    @Test
    public void testIsString() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        String value = "Hello World";

        section.set(key, value);

        assertTrue(section.isString(key));
        assertFalse(section.isString("doesntExist"));
    }

    @Test
    public void testGetInt_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        int value = Integer.MAX_VALUE;

        section.set(key, value);

        assertEquals(value, section.getInt(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetInt_String_Int() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        int value = Integer.MAX_VALUE;
        int def = Integer.MIN_VALUE;

        section.set(key, value);

        assertEquals(value, section.getInt(key, def));
        assertEquals(def, section.getInt("doesntExist", def));
    }

    @Test
    public void testIsInt() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        int value = Integer.MAX_VALUE;

        section.set(key, value);

        assertTrue(section.isInt(key));
        assertFalse(section.isInt("doesntExist"));
    }

    @Test
    public void testGetBoolean_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        boolean value = true;

        section.set(key, value);

        assertEquals(value, section.getBoolean(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetBoolean_String_Boolean() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        boolean value = true;
        boolean def = false;

        section.set(key, value);

        assertEquals(value, section.getBoolean(key, def));
        assertEquals(def, section.getBoolean("doesntExist", def));
    }

    @Test
    public void testIsBoolean() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        boolean value = true;

        section.set(key, value);

        assertTrue(section.isBoolean(key));
        assertFalse(section.isBoolean("doesntExist"));
    }

    @Test
    public void testGetDouble_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        double value = Double.MAX_VALUE;

        section.set(key, value);

        assertEquals(value, section.getDouble(key), 1);
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetDoubleFromInt() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        double value = 123;

        section.set(key, (int) value);

        assertEquals(value, section.getDouble(key), 1);
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetDouble_String_Double() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        double value = Double.MAX_VALUE;
        double def = Double.MIN_VALUE;

        section.set(key, value);

        assertEquals(value, section.getDouble(key, def), 1);
        assertEquals(def, section.getDouble("doesntExist", def), 1);
    }

    @Test
    public void testIsDouble() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        double value = Double.MAX_VALUE;

        section.set(key, value);

        assertTrue(section.isDouble(key));
        assertFalse(section.isDouble("doesntExist"));
    }

    @Test
    public void testGetLong_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        long value = Long.MAX_VALUE;

        section.set(key, value);

        assertEquals(value, section.getLong(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetLong_String_Long() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        long value = Long.MAX_VALUE;
        long def = Long.MIN_VALUE;

        section.set(key, value);

        assertEquals(value, section.getLong(key, def));
        assertEquals(def, section.getLong("doesntExist", def));
    }

    @Test
    public void testIsLong() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        long value = Long.MAX_VALUE;

        section.set(key, value);

        assertTrue(section.isLong(key));
        assertFalse(section.isLong("doesntExist"));
    }

    @Test
    public void testGetList_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("one", 1);
        map.put("two", "two");
        map.put("three", 3.14);

        List<Object> value = Arrays.asList("One", "Two", "Three", 4, "5", 6.0, true, "false", map);

        section.set(key, value);

        assertEquals(value, section.getList(key));
        assertEquals(Arrays.asList((Object) "One", "Two", "Three", "4", "5", "6.0", "true", "false"), section.getStringList(key));
        assertEquals(Arrays.asList((Object) 4, 5, 6), section.getIntegerList(key));
        assertEquals(Arrays.asList((Object) true, false), section.getBooleanList(key));
        assertEquals(Arrays.asList((Object) 4.0, 5.0, 6.0), section.getDoubleList(key));
        assertEquals(Arrays.asList((Object) 4.0f, 5.0f, 6.0f), section.getFloatList(key));
        assertEquals(Arrays.asList((Object) 4L, 5L, 6L), section.getLongList(key));
        assertEquals(Arrays.asList((Object) (byte) 4, (byte) 5, (byte) 6), section.getByteList(key));
        assertEquals(Arrays.asList((Object) (short) 4, (short) 5, (short) 6), section.getShortList(key));
        assertEquals(map, section.getMapList(key).get(0));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetList_String_List() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        List<String> value = Arrays.asList("One", "Two", "Three");
        List<String> def = Arrays.asList("A", "B", "C");

        section.set(key, value);

        assertEquals(value, section.getList(key, def));
        assertEquals(def, section.getList("doesntExist", def));
    }

    @Test
    public void testIsList() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        List<String> value = Arrays.asList("One", "Two", "Three");

        section.set(key, value);

        assertTrue(section.isList(key));
        assertFalse(section.isList("doesntExist"));
    }

    @Test
    public void testGetObject_String_Class() {
        ConfigurationSection section = getConfigurationSection();

        section.set("set", Integer.valueOf(1));
        section.addDefault("default", Integer.valueOf(2));
        section.addDefault("defaultAndSet", Boolean.TRUE);
        section.set("defaultAndSet", Integer.valueOf(3));

        assertEquals(Integer.valueOf(1), section.getObject("set", Integer.class));
        assertNull(section.getObject("set", Boolean.class));
        assertEquals(Integer.valueOf(2), section.getObject("default", Number.class));
        assertNull(section.getObject("default", Boolean.class));
        assertEquals(Integer.valueOf(3), section.getObject("defaultAndSet", Integer.class));
        assertEquals(Boolean.TRUE, section.getObject("defaultAndSet", Boolean.class));
        assertEquals(Integer.valueOf(3), section.getObject("defaultAndSet", Object.class));
        assertNull(section.getObject("defaultAndSet", String.class));
        assertNull(section.getObject("doesntExist", Boolean.class));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetObject_String_Class_T() {
        ConfigurationSection section = getConfigurationSection();

        section.set("set", Integer.valueOf(1));
        section.addDefault("default", Integer.valueOf(2));

        assertEquals(Integer.valueOf(1), section.getObject("set", Integer.class, null));
        assertEquals(Integer.valueOf(1), section.getObject("set", Integer.class, Integer.valueOf(4)));
        assertNull(section.getObject("set", Boolean.class, null));
        assertNull(section.getObject("default", Integer.class, null));
        assertNull(section.getObject("doesntExist", Boolean.class, null));
        assertEquals(Boolean.TRUE, section.getObject("doesntExist", Boolean.class, Boolean.TRUE));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetVector_String() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        Vector value = new Vector(Double.MIN_VALUE, Double.MAX_VALUE, 5);

        section.set(key, value);

        assertEquals(value, section.getVector(key));
        assertNull(section.getString("doesntExist"));
    }

    @Test
    public void testGetVector_String_Vector() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        Vector value = new Vector(Double.MIN_VALUE, Double.MAX_VALUE, 5);
        Vector def = new Vector(100, Double.MIN_VALUE, Double.MAX_VALUE);

        section.set(key, value);

        assertEquals(value, section.getVector(key, def));
        assertEquals(def, section.getVector("doesntExist", def));
    }

    @Test
    public void testIsVector() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";
        Vector value = new Vector(Double.MIN_VALUE, Double.MAX_VALUE, 5);

        section.set(key, value);

        assertTrue(section.isVector(key));
        assertFalse(section.isVector("doesntExist"));
    }

    @Test
    public void testGetConfigurationSection() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";

        ConfigurationSection subsection = section.createSection(key);

        assertEquals(subsection, section.getConfigurationSection(key));
    }

    @Test
    public void testIsConfigurationSection() {
        ConfigurationSection section = getConfigurationSection();
        String key = "exists";

        section.createSection(key);

        assertTrue(section.isConfigurationSection(key));
        assertFalse(section.isConfigurationSection("doesntExist"));
    }

    public enum TestEnum implements ConfigurationSerializable {
        HELLO,
        WORLD,
        BANANAS;

        @Override
        public Map<String, Object> serialize() {
            return Collections.singletonMap("variant", this.name());
        }

        public static TestEnum deserialize(Map<String, Object> map) {
            return TestEnum.valueOf((String) map.get("variant"));
        }
    }
}
