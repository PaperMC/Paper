package org.bukkit.craftbukkit.inventory;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentPatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.support.AbstractTestingBase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PersistentDataContainerTest extends AbstractTestingBase {

    private static NamespacedKey VALID_KEY;

    @BeforeAll
    public static void setup() {
        VALID_KEY = new NamespacedKey("test", "validkey");
    }

    // Sets a test
    @Test
    public void testSetNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getPersistentDataContainer().set(VALID_KEY, new PrimitiveTagType<>(boolean.class), true));
    }

    // Contains a tag
    @Test
    public void testHasNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1); // We gotta set this so we at least try to compare it
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getPersistentDataContainer().has(VALID_KEY, new PrimitiveTagType<>(boolean.class)));
    }

    @Test
    public void testHasNoType() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1); // We gotta set this so we at least try to compare it
        assertTrue(itemMeta.getPersistentDataContainer().has(VALID_KEY));
    }

    // Getting a tag
    @Test
    public void testGetNoAdapter() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1); //We gotta set this so we at least try to compare it
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getPersistentDataContainer().get(VALID_KEY, new PrimitiveTagType<>(boolean.class)));
    }

    @Test
    public void testGetWrongType() {
        ItemMeta itemMeta = createNewItemMeta();
        itemMeta.getPersistentDataContainer().set(VALID_KEY, PersistentDataType.INTEGER, 1);
        assertThrows(IllegalArgumentException.class, () -> itemMeta.getPersistentDataContainer().get(VALID_KEY, PersistentDataType.STRING));
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

    private static ItemMeta createNewItemMeta() {
        return Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
    }

    private static NamespacedKey requestKey(String keyName) {
        return new NamespacedKey("test-plugin", keyName.toLowerCase());
    }

    @Test
    public void testCopyTo() {
        PersistentDataContainer container = createComplexItemMeta().getPersistentDataContainer();
        PersistentDataContainer target = container.getAdapterContext().newPersistentDataContainer();
        target.set(VALID_KEY, PersistentDataType.INTEGER, 1);
        container.set(VALID_KEY, PersistentDataType.INTEGER, 2);
        container.copyTo(target, false);

        assertEquals(1, target.get(VALID_KEY, PersistentDataType.INTEGER)); // Should not be replaced
    }

    @Test
    public void testCopyToReplace() {
        PersistentDataContainer container = createComplexItemMeta().getPersistentDataContainer();
        PersistentDataContainer target = container.getAdapterContext().newPersistentDataContainer();
        target.set(VALID_KEY, PersistentDataType.INTEGER, 1);
        container.set(VALID_KEY, PersistentDataType.INTEGER, 2);
        container.copyTo(target, true);

        assertEquals(container, target);
    }

    // Removing a tag
    @Test
    public void testNBTTagStoring() {
        CraftMetaItem itemMeta = createComplexItemMeta();

        CraftMetaItem.Applicator compound = new CraftMetaItem.Applicator();
        itemMeta.applyToItem(compound);

        assertEquals(itemMeta, new CraftMetaItem(compound.build()));
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
        itemMeta.getPersistentDataContainer().set(
                requestKey("custom-list-string"), PersistentDataType.LIST.strings(), List.of("first[]", "second{}", "third()")
        );
        itemMeta.getPersistentDataContainer().set(
                requestKey("custom-list-bytes"), PersistentDataType.LIST.bytes(), List.of((byte) 1, (byte) 2, (byte) 3)
        );

        PersistentDataContainer innerContainer = itemMeta.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer(); //Add a inner container
        innerContainer.set(VALID_KEY, PersistentDataType.LONG, 5L);
        itemMeta.getPersistentDataContainer().set(requestKey("custom-inner-compound"), PersistentDataType.TAG_CONTAINER, innerContainer);
        return itemMeta;
    }

    // Test edge cases with strings
    @Test
    public void testStringEdgeCases() throws IOException, InvalidConfigurationException {
        final ItemStack stack = new ItemStack(Material.DIAMOND);
        final ItemMeta meta = stack.getItemMeta();
        assertNotNull(meta);

        final String arrayLookalike = "[\"UnicornParticle\",\"TotemParticle\",\"AngelParticle\",\"ColorSwitchParticle\"]";
        final String jsonLookalike = """
                {
                 "key": 'A value wrapped in single quotes',
                 "other": "A value with normal quotes",
                 "array": ["working", "unit", "tests"]
                }
                """;

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(requestKey("string_int"), PersistentDataType.STRING, "5i");
        pdc.set(requestKey("string_true"), PersistentDataType.STRING, "true");
        pdc.set(requestKey("string_byte_array"), PersistentDataType.STRING, "[B;-128B]");
        pdc.set(requestKey("string_array_lookalike"), PersistentDataType.STRING, arrayLookalike);
        pdc.set(requestKey("string_json_lookalike"), PersistentDataType.STRING, jsonLookalike);

        stack.setItemMeta(meta);

        final YamlConfiguration config = new YamlConfiguration();
        config.set("test", stack);
        config.load(new StringReader(config.saveToString())); // Reload config from string

        final ItemStack loadedStack = config.getItemStack("test");
        assertNotNull(loadedStack);
        final ItemMeta loadedMeta = loadedStack.getItemMeta();
        assertNotNull(loadedMeta);

        final PersistentDataContainer loadedPdc = loadedMeta.getPersistentDataContainer();
        assertEquals("5i", loadedPdc.get(requestKey("string_int"), PersistentDataType.STRING));
        assertEquals("true", loadedPdc.get(requestKey("string_true"), PersistentDataType.STRING));
        assertEquals(arrayLookalike, loadedPdc.get(requestKey("string_array_lookalike"), PersistentDataType.STRING));
        assertEquals(jsonLookalike, loadedPdc.get(requestKey("string_json_lookalike"), PersistentDataType.STRING));
    }

    // Test complex object storage
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
        @NotNull
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @NotNull
        @Override
        public Class<UUID> getComplexType() {
            return UUID.class;
        }

        @NotNull
        @Override
        public byte[] toPrimitive(@NotNull UUID complex, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(complex.getMostSignificantBits());
            bb.putLong(complex.getLeastSignificantBits());
            return bb.array();
        }

        @NotNull
        @Override
        public UUID fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
            ByteBuffer bb = ByteBuffer.wrap(primitive);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }

    @Test
    public void testPrimitiveCustomTags() {
        ItemMeta itemMeta = createNewItemMeta();

        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.BYTE, (byte) 1);
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.SHORT, (short) 1);
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.INTEGER, 1);
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.LONG, 1L);
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.FLOAT, 1.34F);
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.DOUBLE, 151.123);

        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.STRING, "test");

        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.BYTE_ARRAY, new byte[]{
            1, 4, 2, Byte.MAX_VALUE
        });
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.INTEGER_ARRAY, new int[]{
            1, 4, 2, Integer.MAX_VALUE
        });
        this.testPrimitiveCustomTag(itemMeta, PersistentDataType.LONG_ARRAY, new long[]{
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

    class PrimitiveTagType<P> implements PersistentDataType<P, P> {

        private final Class<P> primitiveType;

        PrimitiveTagType(Class<P> primitiveType) {
            this.primitiveType = primitiveType;
        }

        @NotNull
        @Override
        public Class<P> getPrimitiveType() {
            return this.primitiveType;
        }

        @NotNull
        @Override
        public Class<P> getComplexType() {
            return this.primitiveType;
        }

        @NotNull
        @Override
        public P toPrimitive(@NotNull P complex, @NotNull PersistentDataAdapterContext context) {
            return complex;
        }

        @NotNull
        @Override
        public P fromPrimitive(@NotNull P primitive, @NotNull PersistentDataAdapterContext context) {
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

        clonedContainer.set(PersistentDataContainerTest.VALID_KEY, PersistentDataType.STRING, "dinnerbone");
        assertNotEquals(container, clonedContainer);
    }

    @ParameterizedTest
    @MethodSource("testListTypeArgumentSource")
    public <T> void testListType(@NotNull final ListPersistentDataType<T, T> type, @NotNull final List<T> list, @NotNull final BiConsumer<T, T> equalsCheck) {
        final ItemMeta meta = createNewItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(requestKey("list"), type, list);

        final List<T> returnedList = container.get(requestKey("list"), type);

        assertNotNull(returnedList);
        assertEquals(list.size(), returnedList.size());

        for (int i = 0; i < list.size(); i++) {
            final T expectedValue = list.get(i);
            final T foundValue = returnedList.get(i);
            equalsCheck.accept(expectedValue, foundValue);
        }
    }

    @NotNull
    private static Stream<Arguments> testListTypeArgumentSource() {
        final PersistentDataContainer first = createNewItemMeta().getPersistentDataContainer();
        final PersistentDataContainer second = first.getAdapterContext().newPersistentDataContainer();
        first.set(requestKey("a"), PersistentDataType.STRING, "hello world");
        second.set(requestKey("b"), PersistentDataType.BOOLEAN, true);

        final BiConsumer<Object, Object> objectAssertion = Assertions::assertEquals;
        final BiConsumer<byte[], byte[]> byteArrayAssertion = Assertions::assertArrayEquals;
        final BiConsumer<int[], int[]> intArrayAssertion = Assertions::assertArrayEquals;
        final BiConsumer<long[], long[]> longArrayAssertion = Assertions::assertArrayEquals;

        return Stream.of(
                Arguments.of(PersistentDataType.LIST.bytes(), List.of((byte) 1, (byte) 2, (byte) 3), objectAssertion),
                Arguments.of(PersistentDataType.LIST.shorts(), List.of((short) 1, (short) 2, (short) 3), objectAssertion),
                Arguments.of(PersistentDataType.LIST.integers(), List.of(1, 2, 3), objectAssertion),
                Arguments.of(PersistentDataType.LIST.longs(), List.of(1L, 2L, 3L), objectAssertion),
                Arguments.of(PersistentDataType.LIST.floats(), List.of(1F, 2F, 3F), objectAssertion),
                Arguments.of(PersistentDataType.LIST.doubles(), List.of(1D, 2D, 3D), objectAssertion),
                Arguments.of(PersistentDataType.LIST.booleans(), List.of(true, true, false), objectAssertion),
                Arguments.of(PersistentDataType.LIST.strings(), List.of("a", "b", "c"), objectAssertion),
                Arguments.of(PersistentDataType.LIST.byteArrays(), List.of(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}), byteArrayAssertion),
                Arguments.of(PersistentDataType.LIST.integerArrays(), List.of(new int[]{1, 2, 3}, new int[]{4, 5, 6}), intArrayAssertion),
                Arguments.of(PersistentDataType.LIST.longArrays(), List.of(new long[]{1, 2, 3}, new long[]{4, 5, 6}), longArrayAssertion),
                Arguments.of(PersistentDataType.LIST.dataContainers(), List.of(first, second), objectAssertion));
    }

    @Test
    public void testEmptyListApplicationToAnyType() throws IOException {
        final CraftMetaItem craftItem = new CraftMetaItem(DataComponentPatch.EMPTY);
        final PersistentDataContainer container = craftItem.getPersistentDataContainer();

        container.set(requestKey("list"), PersistentDataType.LIST.strings(), List.of());
        assertTrue(container.has(requestKey("list"), PersistentDataType.LIST.strings()));
        assertTrue(container.has(requestKey("list"), PersistentDataType.LIST.bytes()));
        assertFalse(container.has(requestKey("list"), PersistentDataType.STRING));
        assertEquals(List.of(), container.get(requestKey("list"), PersistentDataType.LIST.strings()));

        // Write and read the entire container to NBT
        final CraftMetaItem.Applicator storage = new CraftMetaItem.Applicator();
        craftItem.applyToItem(storage);

        final CraftMetaItem readItem = new CraftMetaItem(storage.build());
        final PersistentDataContainer readContainer = readItem.getPersistentDataContainer();

        assertTrue(readContainer.has(requestKey("list"), PersistentDataType.LIST.strings()));
        assertTrue(readContainer.has(requestKey("list"), PersistentDataType.LIST.bytes()));
        assertFalse(readContainer.has(requestKey("list"), PersistentDataType.STRING));
        assertEquals(List.of(), readContainer.get(requestKey("list"), PersistentDataType.LIST.strings()));
    }

    // This is a horrific marriage of tag container array "primitive" types the API offered and the new list types.
    // We are essentially testing if these two play nice as tag container array was an emulated primitive type
    // that used lists under the hood, hence this is testing the extra handling of TAG_CONTAINER_ARRAY in combination
    // with lists. Plain lists in lists are tested above.
    //
    // Little faith is to be had when it comes to abominations constructed by plugin developers, this test ensures
    // even this disgrace of a combination functions in PDCs.
    @Test
    public void testListOfListViaContainerArray() {
        final ListPersistentDataType<PersistentDataContainer[], PersistentDataContainer[]> listPersistentDataType = PersistentDataType.LIST.listTypeFrom(PersistentDataType.TAG_CONTAINER_ARRAY);

        final ItemMeta meta = createNewItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final PersistentDataAdapterContext adapterContext = container.getAdapterContext();

        final PersistentDataContainer first = adapterContext.newPersistentDataContainer();
        first.set(requestKey("a"), PersistentDataType.STRING, "hi");

        final PersistentDataContainer second = adapterContext.newPersistentDataContainer();
        second.set(requestKey("a"), PersistentDataType.INTEGER, 2);

        final List<PersistentDataContainer[]> listOfArrays = new ArrayList<>();
        listOfArrays.add(new PersistentDataContainer[]{first, second});

        container.set(requestKey("containerListList"), listPersistentDataType, listOfArrays);

        assertTrue(container.has(requestKey("containerListList"), listPersistentDataType));

        final List<PersistentDataContainer[]> containerListList = container.get(requestKey("containerListList"), listPersistentDataType);

        assertNotNull(containerListList);
        assertEquals(1, containerListList.size());

        final PersistentDataContainer[] arrayOfPDC = containerListList.get(0);
        assertEquals(2, arrayOfPDC.length);

        assertEquals("hi", arrayOfPDC[0].get(requestKey("a"), PersistentDataType.STRING));
        assertEquals(2, arrayOfPDC[1].get(requestKey("a"), PersistentDataType.INTEGER));
    }
}
