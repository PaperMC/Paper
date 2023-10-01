package org.bukkit.craftbukkit.legacy;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Charsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class PersistentDataContainerLegacyTest extends AbstractTestingBase {

    @Test
    public void ensureLegacyParsing() {
        CraftItemFactory.instance(); // Initialize craft item factory to register craft item meta serializers

        YamlConfiguration legacyConfig = null;
        try (final InputStream input = getClass().getClassLoader().getResourceAsStream("pdc/legacy_pdc.yml")) {
            assertNotNull(input, "Legacy pdc yaml was null");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charsets.UTF_8))) {
                legacyConfig = YamlConfiguration.loadConfiguration(reader);
            }
        } catch (IOException e) {
            fail("Failed to find test resource!");
        }

        assertNotNull(legacyConfig, "Could not fetch legacy config");

        final ItemStack stack = legacyConfig.getItemStack("test");
        assertNotNull(stack);

        final ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        assertEquals(Byte.valueOf(Byte.MAX_VALUE), pdc.get(key("byte"), PersistentDataType.BYTE), "legacy byte was wrong");
        assertEquals(Short.valueOf(Short.MAX_VALUE), pdc.get(key("short"), PersistentDataType.SHORT), "legacy short was wrong");
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), pdc.get(key("integer"), PersistentDataType.INTEGER), "legacy integer was wrong");
        assertEquals(Long.valueOf(Long.MAX_VALUE), pdc.get(key("long"), PersistentDataType.LONG), "legacy long was wrong");
        assertEquals(Float.valueOf(Float.MAX_VALUE), pdc.get(key("float"), PersistentDataType.FLOAT), "legacy float was wrong");
        assertEquals(Double.valueOf(Double.MAX_VALUE), pdc.get(key("double"), PersistentDataType.DOUBLE), "legacy double was wrong");
        assertEquals("stringy", pdc.get(key("string_simple"), PersistentDataType.STRING), "legacy string-simple was wrong");
        assertEquals("What a fun complex string 🔥", pdc.get(key("string_complex"), PersistentDataType.STRING), "legacy string-complex was wrong");

        assertArrayEquals(new byte[]{Byte.MIN_VALUE}, pdc.get(key("byte_array"), PersistentDataType.BYTE_ARRAY), "legacy byte array was wrong");

        assertArrayEquals(new int[]{Integer.MIN_VALUE}, pdc.get(key("integer_array"), PersistentDataType.INTEGER_ARRAY), "legacy integer array was wrong");

        assertArrayEquals(new long[]{Long.MIN_VALUE}, pdc.get(key("long_array"), PersistentDataType.LONG_ARRAY), "legacy long array was wrong");

        assertEquals("5", pdc.get(key("string_edge_case_number"), PersistentDataType.STRING), "legacy string edge case number");
        assertEquals("\"Hello world\"", pdc.get(key("string_edge_case_quoted"), PersistentDataType.STRING), "legacy string edge case quotes");
    }

    private NamespacedKey key(String key) {
        return new NamespacedKey("test", key);
    }
}
