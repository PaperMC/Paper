package org.bukkit.craftbukkit.inventory;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.support.environment.Legacy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Legacy
public class DeprecatedItemMetaCustomValueTest {

    private static NamespacedKey VALID_KEY;

    @BeforeAll
    public static void setup() {
        DeprecatedItemMetaCustomValueTest.VALID_KEY = new NamespacedKey("test", "validkey");
    }

    /*
        Sets a test
     */
    @Test
    public void testSetNoAdapter() {
        ItemMeta itemMeta = this.createNewItemMeta();
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getCustomTagContainer().setCustomTag(VALID_KEY, new PrimitiveTagType<>(boolean.class), true));
    }

    /*
        Contains a tag
     */
    @Test
    public void testHasNoAdapter() {
        ItemMeta itemMeta = this.createNewItemMeta();
        itemMeta.getCustomTagContainer().setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.INTEGER, 1); // We gotta set this so we at least try to compare it
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getCustomTagContainer().hasCustomTag(VALID_KEY, new PrimitiveTagType<>(boolean.class)));
    }

    /*
        Getting a tag
     */
    @Test
    public void testGetNoAdapter() {
        ItemMeta itemMeta = this.createNewItemMeta();
        itemMeta.getCustomTagContainer().setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.INTEGER, 1); //We gotta set this so we at least try to compare it
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getCustomTagContainer().getCustomTag(VALID_KEY, new PrimitiveTagType<>(boolean.class)));
    }

    @Test
    public void testGetWrongType() {
        ItemMeta itemMeta = this.createNewItemMeta();
        itemMeta.getCustomTagContainer().setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.INTEGER, 1);
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getCustomTagContainer().getCustomTag(VALID_KEY, ItemTagType.STRING));
    }

    @Test
    public void testDifferentNamespace() {
        NamespacedKey namespacedKeyA = new NamespacedKey("plugin-a", "damage");
        NamespacedKey namespacedKeyB = new NamespacedKey("plugin-b", "damage");

        ItemMeta meta = this.createNewItemMeta();
        meta.getCustomTagContainer().setCustomTag(namespacedKeyA, ItemTagType.LONG, 15L);
        meta.getCustomTagContainer().setCustomTag(namespacedKeyB, ItemTagType.LONG, 160L);

        assertEquals(15L, (long) meta.getCustomTagContainer().getCustomTag(namespacedKeyA, ItemTagType.LONG));
        assertEquals(160L, (long) meta.getCustomTagContainer().getCustomTag(namespacedKeyB, ItemTagType.LONG));
    }

    private ItemMeta createNewItemMeta() {
        return Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
    }

    private NamespacedKey requestKey(String keyName) {
        return new NamespacedKey("test-plugin", keyName.toLowerCase(Locale.ROOT));
    }

    /*
        Removing a tag
     */
    @Test
    public void testNBTTagStoring() {
        CraftMetaItem itemMeta = this.createComplexItemMeta();

        CraftMetaItem.Applicator compound = new CraftMetaItem.Applicator() {}; // Paper
        itemMeta.applyToItem(compound);

        assertEquals(itemMeta, new CraftMetaItem(compound.build(), null)); // Paper
    }

    @Test
    public void testMapStoring() {
        CraftMetaItem itemMeta = this.createComplexItemMeta();

        Map<String, Object> serialize = itemMeta.serialize();
        assertEquals(itemMeta, new CraftMetaItem(serialize));
    }

    @Test
    public void testYAMLStoring() {
        ItemStack stack = new ItemStack(Material.DIAMOND);
        CraftMetaItem meta = this.createComplexItemMeta();
        stack.setItemMeta(meta);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("testpath", stack);

        String configValue = configuration.saveToString();
        YamlConfiguration loadedConfig = YamlConfiguration.loadConfiguration(new StringReader(configValue));

        assertEquals(stack, loadedConfig.getSerializable("testpath", ItemStack.class));
        assertNotEquals(new ItemStack(Material.DIAMOND), loadedConfig.getSerializable("testpath", ItemStack.class));
    }

    @Test
    public void testCorrectType() {
        ItemStack stack = new ItemStack(Material.DIAMOND);
        CraftMetaItem meta = this.createComplexItemMeta();

        meta.getCustomTagContainer().setCustomTag(this.requestKey("int"), ItemTagType.STRING, "1");
        meta.getCustomTagContainer().setCustomTag(this.requestKey("double"), ItemTagType.STRING, "1.33");
        stack.setItemMeta(meta);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("testpath", stack);

        String configValue = configuration.saveToString();
        YamlConfiguration loadedConfig = YamlConfiguration.loadConfiguration(new StringReader(configValue));
        ItemStack newStack = loadedConfig.getSerializable("testpath", ItemStack.class);

        assertTrue(newStack.getItemMeta().getCustomTagContainer().hasCustomTag(this.requestKey("int"), ItemTagType.STRING));
        assertEquals(newStack.getItemMeta().getCustomTagContainer().getCustomTag(this.requestKey("int"), ItemTagType.STRING), "1");

        assertTrue(newStack.getItemMeta().getCustomTagContainer().hasCustomTag(this.requestKey("double"), ItemTagType.STRING));
        assertEquals(newStack.getItemMeta().getCustomTagContainer().getCustomTag(this.requestKey("double"), ItemTagType.STRING), "1.33");
    }

    private CraftMetaItem createComplexItemMeta() {
        CraftMetaItem itemMeta = (CraftMetaItem) this.createNewItemMeta();
        itemMeta.setDisplayName("Item Display Name");

        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-long"), ItemTagType.LONG, 4L); //Add random primitive values
        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-byte-array"), ItemTagType.BYTE_ARRAY, new byte[]{
            0, 1, 2, 10
        });
        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-string"), ItemTagType.STRING, "Hello there world");
        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-int"), ItemTagType.INTEGER, 3);
        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-double"), ItemTagType.DOUBLE, 3.123);

        CustomItemTagContainer innerContainer = itemMeta.getCustomTagContainer().getAdapterContext().newTagContainer(); //Add a inner container
        innerContainer.setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG, 5L);
        itemMeta.getCustomTagContainer().setCustomTag(this.requestKey("custom-inner-compound"), ItemTagType.TAG_CONTAINER, innerContainer);
        return itemMeta;
    }

    /*
        Test complex object storage
     */
    @Test
    public void storeUUIDOnItemTest() {
        ItemMeta itemMeta = this.createNewItemMeta();
        UUIDItemTagType uuidItemTagType = new UUIDItemTagType();
        UUID uuid = UUID.fromString("434eea72-22a6-4c61-b5ef-945874a5c478");

        itemMeta.getCustomTagContainer().setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, uuidItemTagType, uuid);
        assertTrue(itemMeta.getCustomTagContainer().hasCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, uuidItemTagType));
        assertEquals(uuid, itemMeta.getCustomTagContainer().getCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, uuidItemTagType));
    }

    @Test
    public void encapsulatedContainers() {
        NamespacedKey innerKey = new NamespacedKey("plugin-a", "inner");

        ItemMeta meta = this.createNewItemMeta();
        ItemTagAdapterContext context = meta.getCustomTagContainer().getAdapterContext();

        CustomItemTagContainer thirdContainer = context.newTagContainer();
        thirdContainer.setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG, 3L);

        CustomItemTagContainer secondContainer = context.newTagContainer();
        secondContainer.setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG, 2L);
        secondContainer.setCustomTag(innerKey, ItemTagType.TAG_CONTAINER, thirdContainer);

        meta.getCustomTagContainer().setCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG, 1L);
        meta.getCustomTagContainer().setCustomTag(innerKey, ItemTagType.TAG_CONTAINER, secondContainer);

        assertEquals(3L, meta.getCustomTagContainer()
                .getCustomTag(innerKey, ItemTagType.TAG_CONTAINER)
                .getCustomTag(innerKey, ItemTagType.TAG_CONTAINER)
                .getCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG).longValue());

        assertEquals(2L, meta.getCustomTagContainer()
                .getCustomTag(innerKey, ItemTagType.TAG_CONTAINER)
                .getCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG).longValue());

        assertEquals(1L, meta.getCustomTagContainer()
                .getCustomTag(DeprecatedItemMetaCustomValueTest.VALID_KEY, ItemTagType.LONG).longValue());
    }

    class UUIDItemTagType implements ItemTagType<byte[], UUID> {

        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public byte[] toPrimitive(UUID complex, ItemTagAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @Override
        public UUID fromPrimitive(byte[] primitive, ItemTagAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }

    @Test
    public void testPrimitiveCustomTags() {
        ItemMeta itemMeta = this.createNewItemMeta();

        this.testPrimitiveCustomTag(itemMeta, ItemTagType.BYTE, (byte) 1);
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.SHORT, (short) 1);
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.INTEGER, 1);
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.LONG, 1L);
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.FLOAT, 1.34F);
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.DOUBLE, 151.123);

        this.testPrimitiveCustomTag(itemMeta, ItemTagType.STRING, "test");

        this.testPrimitiveCustomTag(itemMeta, ItemTagType.BYTE_ARRAY, new byte[]{
            1, 4, 2, Byte.MAX_VALUE
        });
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.INTEGER_ARRAY, new int[]{
            1, 4, 2, Integer.MAX_VALUE
        });
        this.testPrimitiveCustomTag(itemMeta, ItemTagType.LONG_ARRAY, new long[]{
            1L, 4L, 2L, Long.MAX_VALUE
        });
    }

    private <T, Z> void testPrimitiveCustomTag(ItemMeta meta, ItemTagType<T, Z> type, Z value) {
        NamespacedKey tagKey = new NamespacedKey("test", String.valueOf(type.hashCode()));

        meta.getCustomTagContainer().setCustomTag(tagKey, type, value);
        assertTrue(meta.getCustomTagContainer().hasCustomTag(tagKey, type));

        Z foundValue = meta.getCustomTagContainer().getCustomTag(tagKey, type);
        if (foundValue.getClass().isArray()) { // Compare arrays using reflection access
            int length = Array.getLength(foundValue);
            int originalLength = Array.getLength(value);
            for (int i = 0; i < length && i < originalLength; i++) {
                assertEquals(Array.get(value, i), Array.get(foundValue, i));
            }
        } else {
            assertEquals(foundValue, value);
        }

        meta.getCustomTagContainer().removeCustomTag(tagKey);
        assertFalse(meta.getCustomTagContainer().hasCustomTag(tagKey, type));
    }

    class PrimitiveTagType<T> implements ItemTagType<T, T> {

        private final Class<T> primitiveType;

        PrimitiveTagType(Class<T> primitiveType) {
            this.primitiveType = primitiveType;
        }

        @Override
        public Class<T> getPrimitiveType() {
            return this.primitiveType;
        }

        @Override
        public Class<T> getComplexType() {
            return this.primitiveType;
        }

        @Override
        public T toPrimitive(T complex, ItemTagAdapterContext context) {
            return complex;
        }

        @Override
        public T fromPrimitive(T primitive, ItemTagAdapterContext context) {
            return primitive;
        }
    }
}
