package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Before;
import org.junit.Test;

public class PersistentDataContainerTest extends AbstractTestingBase {

    private static NamespacedKey VALID_KEY;

    @Before
    public void setup() {
        VALID_KEY = new NamespacedKey("test", "validkey");
    }

    /*
        Sets a test
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, new PrimitiveTagType<>(boolean.class), true);
    }

    /*
        Contains a tag
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHasNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1); // We gotta set this so we at least try to compare it
        itemMeta.getPersistentDataContainer().has(VALID_KEY, new PrimitiveTagType<>(boolean.class));
    }

    /*
        Getting a tag
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1); //We gotta set this so we at least try to compare it
        itemMeta.getPersistentDataContainer().get(VALID_KEY, new PrimitiveTagType<>(boolean.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrongType() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1);
        itemMeta.getPersistentDataContainer().get(VALID_KEY, PersistentDataType.STRING);
    }

    @Test
    public void testDifferentNamespace() {
        NamespacedKey namespacedKeyA = new NamespacedKey("plugin-a", "damage");
        NamespacedKey namespacedKeyB = new NamespacedKey("plugin-b", "damage");

        ItemMeta meta = createNewItemMeta();
        meta.getPersistentDataContainer().set(namespacedKeyA, PersistentDataType.LONG, 15L);
        meta.getPersistentDataContainer().set(namespacedKeyB, PersistentDataType.LONG, 160L);

        assertEquals(15L, (long) meta.getPersistentDataContainer().get(namespacedKeyA, PersistentDataType.LONG));
        assertEquals(160L, (long) meta.getPersistentDataContainer().get(namespacedKeyB, PersistentDataType.LONG));
    }

    private ItemMeta createNewItemMeta() {
        return Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
    }

    private NamespacedKey requestKey(String keyName) {
        return new NamespacedKey("test-plugin", keyName.toLowerCase());
    }

    /*
        Removing a tag
     */
    @Test
    public void testNBTTagStoring() {
        CraftMetaItem itemMeta = createComplexItemMeta();

        NBTTagCompound compound = new NBTTagCompound();
        itemMeta.applyToItem(compound);

        assertEquals(itemMeta, new CraftMetaItem(compound));
    }

    @Test
    public void testMapStoring() {
        CraftMetaItem itemMeta = createComplexItemMeta();

        Map<String, Object> serialize = itemMeta.serialize();
        assertEquals(itemMeta, new CraftMetaItem(serialize));
    }

    @Test
    public void testYAMLStoring() {
        ItemStack stack = new ItemStack(Material.DIAMOND);
        CraftMetaItem meta = createComplexItemMeta();
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
        CraftMetaItem meta = createComplexItemMeta();

        meta.getPersistentDataContainer().set(requestKey("int"), PersistentDataType.STRING, "1");
        meta.getPersistentDataContainer().set(requestKey("double"), PersistentDataType.STRING, "1.33");
        stack.setItemMeta(meta);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("testpath", stack);

        String configValue = configuration.saveToString();
        YamlConfiguration loadedConfig = YamlConfiguration.loadConfiguration(new StringReader(configValue));
        ItemStack newStack = loadedConfig.getSerializable("testpath", ItemStack.class);

        assertTrue(newStack.getItemMeta().getPersistentDataContainer().has(requestKey("int"), PersistentDataType.STRING));
        assertEquals(newStack.getItemMeta().getPersistentDataContainer().get(requestKey("int"), PersistentDataType.STRING), "1");

        assertTrue(newStack.getItemMeta().getPersistentDataContainer().has(requestKey("double"), PersistentDataType.STRING));
        assertEquals(newStack.getItemMeta().getPersistentDataContainer().get(requestKey("double"), PersistentDataType.STRING), "1.33");
    }

    private CraftMetaItem createComplexItemMeta() {
        CraftMetaItem itemMeta = (CraftMetaItem) createNewItemMeta();
        itemMeta.setDisplayName("Item Display Name");

        itemMeta.getPersistentDataContainer().set(requestKey("custom-long"), PersistentDataType.LONG, 4L); //Add random primitive values
        itemMeta.getPersistentDataContainer().set(requestKey("custom-byte-array"), PersistentDataType.BYTE_ARRAY, new byte[]{
            0, 1, 2, 10
        });
        itemMeta.getPersistentDataContainer().set(requestKey("custom-string"), PersistentDataType.STRING, "Hello there world");
        itemMeta.getPersistentDataContainer().set(requestKey("custom-int"), PersistentDataType.INTEGER, 3);
        itemMeta.getPersistentDataContainer().set(requestKey("custom-double"), PersistentDataType.DOUBLE, 3.123);

        PersistentDataContainer innerContainer = itemMeta.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer(); //Add a inner container
        innerContainer.set(VALID_KEY, PersistentDataType.LONG, 5L);
        itemMeta.getPersistentDataContainer().set(requestKey("custom-inner-compound"), PersistentDataType.TAG_CONTAINER, innerContainer);
        return itemMeta;
    }

    /*
        Test complex object storage
     */
    @Test
    public void storeUUIDOnItemTest() {
        ItemMeta itemMeta = createNewItemMeta();
        UUIDPersistentDataType uuidPersistentDataType = new UUIDPersistentDataType();
        UUID uuid = UUID.fromString("434eea72-22a6-4c61-b5ef-945874a5c478");

        itemMeta.getPersistentDataContainer().set(VALID_KEY, uuidPersistentDataType, uuid);
        assertTrue(itemMeta.getPersistentDataContainer().has(VALID_KEY, uuidPersistentDataType));
        assertEquals(uuid, itemMeta.getPersistentDataContainer().get(VALID_KEY, uuidPersistentDataType));
    }

    @Test
    public void encapsulatedContainers() {
        NamespacedKey innerKey = new NamespacedKey("plugin-a", "inner");

        ItemMeta meta = createNewItemMeta();
        PersistentDataAdapterContext context = meta.getPersistentDataContainer().getAdapterContext();

        PersistentDataContainer thirdContainer = context.newPersistentDataContainer();
        thirdContainer.set(VALID_KEY, PersistentDataType.LONG, 3L);

        PersistentDataContainer secondContainer = context.newPersistentDataContainer();
        secondContainer.set(VALID_KEY, PersistentDataType.LONG, 2L);
        secondContainer.set(innerKey, PersistentDataType.TAG_CONTAINER, thirdContainer);

        meta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.LONG, 1L);
        meta.getPersistentDataContainer().set(innerKey, PersistentDataType.TAG_CONTAINER, secondContainer);

        assertEquals(3L, meta.getPersistentDataContainer()
                .get(innerKey, PersistentDataType.TAG_CONTAINER)
                .get(innerKey, PersistentDataType.TAG_CONTAINER)
                .get(VALID_KEY, PersistentDataType.LONG).longValue());

        assertEquals(2L, meta.getPersistentDataContainer()
                .get(innerKey, PersistentDataType.TAG_CONTAINER)
                .get(VALID_KEY, PersistentDataType.LONG).longValue());

        assertEquals(1L, meta.getPersistentDataContainer()
                .get(VALID_KEY, PersistentDataType.LONG).longValue());
    }

    class UUIDPersistentDataType implements PersistentDataType<byte[], UUID> {

        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public byte[] toPrimitive(UUID complex, PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @Override
        public UUID fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }

    @Test
    public void testPrimitiveCustomTags() {
        ItemMeta itemMeta = createNewItemMeta();

        testPrimitiveCustomTag(itemMeta, PersistentDataType.BYTE, (byte) 1);
        testPrimitiveCustomTag(itemMeta, PersistentDataType.SHORT, (short) 1);
        testPrimitiveCustomTag(itemMeta, PersistentDataType.INTEGER, 1);
        testPrimitiveCustomTag(itemMeta, PersistentDataType.LONG, 1L);
        testPrimitiveCustomTag(itemMeta, PersistentDataType.FLOAT, 1.34F);
        testPrimitiveCustomTag(itemMeta, PersistentDataType.DOUBLE, 151.123);

        testPrimitiveCustomTag(itemMeta, PersistentDataType.STRING, "test");

        testPrimitiveCustomTag(itemMeta, PersistentDataType.BYTE_ARRAY, new byte[]{
            1, 4, 2, Byte.MAX_VALUE
        });
        testPrimitiveCustomTag(itemMeta, PersistentDataType.INTEGER_ARRAY, new int[]{
            1, 4, 2, Integer.MAX_VALUE
        });
        testPrimitiveCustomTag(itemMeta, PersistentDataType.LONG_ARRAY, new long[]{
            1L, 4L, 2L, Long.MAX_VALUE
        });
    }

    private <T, Z> void testPrimitiveCustomTag(ItemMeta meta, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey tagKey = new NamespacedKey("test", String.valueOf(type.hashCode()));

        meta.getPersistentDataContainer().set(tagKey, type, value);
        assertTrue(meta.getPersistentDataContainer().has(tagKey, type));

        Z foundValue = meta.getPersistentDataContainer().get(tagKey, type);
        if (foundValue.getClass().isArray()) { // Compare arrays using reflection access
            int length = Array.getLength(foundValue);
            int originalLength = Array.getLength(value);
            for (int i = 0; i < length && i < originalLength; i++) {
                assertEquals(Array.get(value, i), Array.get(foundValue, i));
            }
        } else {
            assertEquals(foundValue, value);
        }

        meta.getPersistentDataContainer().remove(tagKey);
        assertFalse(meta.getPersistentDataContainer().has(tagKey, type));
    }

    class PrimitiveTagType<T> implements PersistentDataType<T, T> {

        private final Class<T> primitiveType;

        PrimitiveTagType(Class<T> primitiveType) {
            this.primitiveType = primitiveType;
        }

        @Override
        public Class<T> getPrimitiveType() {
            return primitiveType;
        }

        @Override
        public Class<T> getComplexType() {
            return primitiveType;
        }

        @Override
        public T toPrimitive(T complex, PersistentDataAdapterContext context) {
            return complex;
        }

        @Override
        public T fromPrimitive(T primitive, PersistentDataAdapterContext context) {
            return primitive;
        }
    }

    @Test
    public void testItemMetaClone() {
        ItemMeta itemMeta = createNewItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.STRING, "notch");

        ItemMeta clonedMeta = itemMeta.clone();
        PersistentDataContainer clonedContainer = clonedMeta.getPersistentDataContainer();

        assertNotSame(container, clonedContainer);
        assertEquals(container, clonedContainer);

        clonedContainer.set(VALID_KEY, PersistentDataType.STRING, "dinnerbone");
        assertNotEquals(container, clonedContainer);
    }
}
