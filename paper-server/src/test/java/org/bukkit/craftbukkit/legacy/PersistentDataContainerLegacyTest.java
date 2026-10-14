package org.bukkit.craftbukkit.legacy;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.support.environment.Legacy;
import org.junit.jupiter.api.Test;

@Legacy
public class PersistentDataContainerLegacyTest {

    @Test
    public void ensureLegacyParsing() {
        CraftItemFactory.instance(); // Initialize craft item factory to register craft item meta serializers

        YamlConfiguration legacyConfig = null;
        try (final InputStream input = this.getClass().getClassLoader().getResourceAsStream("pdc/legacy_pdc.yml")) {
            assertNotNull(input, "Legacy pdc yaml was null");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
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
        assertEquals(Byte.valueOf(Byte.MAX_VALUE), pdc.get(this.key("byte"), PersistentDataType.BYTE), "legacy byte was wrong");
        assertEquals(Short.valueOf(Short.MAX_VALUE), pdc.get(this.key("short"), PersistentDataType.SHORT), "legacy short was wrong");
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), pdc.get(this.key("integer"), PersistentDataType.INTEGER), "legacy integer was wrong");
        assertEquals(Long.valueOf(Long.MAX_VALUE), pdc.get(this.key("long"), PersistentDataType.LONG), "legacy long was wrong");
        assertEquals(Float.valueOf(Float.MAX_VALUE), pdc.get(this.key("float"), PersistentDataType.FLOAT), "legacy float was wrong");
        assertEquals(Double.valueOf(Double.MAX_VALUE), pdc.get(this.key("double"), PersistentDataType.DOUBLE), "legacy double was wrong");
        assertEquals("stringy", pdc.get(this.key("string_simple"), PersistentDataType.STRING), "legacy string-simple was wrong");
        assertEquals("What a fun complex string 🔥", pdc.get(this.key("string_complex"), PersistentDataType.STRING), "legacy string-complex was wrong");

        assertArrayEquals(new byte[]{Byte.MIN_VALUE}, pdc.get(this.key("byte_array"), PersistentDataType.BYTE_ARRAY), "legacy byte array was wrong");

        assertArrayEquals(new int[]{Integer.MIN_VALUE}, pdc.get(this.key("integer_array"), PersistentDataType.INTEGER_ARRAY), "legacy integer array was wrong");

        assertArrayEquals(new long[]{Long.MIN_VALUE}, pdc.get(this.key("long_array"), PersistentDataType.LONG_ARRAY), "legacy long array was wrong");

        assertEquals("5", pdc.get(this.key("string_edge_case_number"), PersistentDataType.STRING), "legacy string edge case number");
        assertEquals("\"Hello world\"", pdc.get(this.key("string_edge_case_quoted"), PersistentDataType.STRING), "legacy string edge case quotes");
    }

    private NamespacedKey key(String key) {
        return new NamespacedKey("test", key);
    }
}
