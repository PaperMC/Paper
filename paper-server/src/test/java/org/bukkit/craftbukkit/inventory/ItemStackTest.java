package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.bukkit.support.Matchers.sameHash;
import static org.hamcrest.Matchers.*;
import com.google.common.base.Joiner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackTest extends AbstractTestingBase {
    abstract static class StackProvider {
        final Material material;

        StackProvider(Material material) {
            this.material = material;
        }

        ItemStack bukkit() {
            return operate(cleanStack(material, false));
        }

        ItemStack craft() {
            return operate(cleanStack(material, true));
        }

        abstract ItemStack operate(ItemStack cleanStack);

        static ItemStack cleanStack(Material material, boolean craft) {
            final ItemStack stack = new ItemStack(material);
            return craft ? CraftItemStack.asCraftCopy(stack) : stack;
        }

        @Override
        public String toString() {
            return material.toString();
        }

        /**
         * For each item in parameterList, it will apply nameFormat at nameIndex.
         * For each item in parameterList for each item in materials, it will create a stack provider at each array index that contains an Operator.
         *
         * @param parameterList
         * @param nameFormat
         * @param nameIndex
         * @param materials
         * @return
         */
        static Stream<Arguments> compound(final List<Object[]> parameterList, final String nameFormat, final int nameIndex, final Material... materials) {
            final List<Arguments> out = new ArrayList<>();
            for (Object[] params : parameterList) {
                final int len = params.length;
                for (final Material material : materials) {
                    final Object[] paramsOut = params.clone();
                    for (int i = 0; i < len; i++) {
                        final Object param = paramsOut[i];
                        if (param instanceof Operator) {
                            final Operator operator = (Operator) param;
                            paramsOut[i] = new StackProvider(material) {
                                @Override
                                ItemStack operate(ItemStack cleanStack) {
                                    return operator.operate(cleanStack);
                                }
                            };
                        }
                    }
                    paramsOut[nameIndex] = String.format(nameFormat, paramsOut[nameIndex], material);
                    out.add(Arguments.of(paramsOut));
                }
            }
            return out.stream();
        }
    }

    interface Operator {
        ItemStack operate(ItemStack cleanStack);
    }

    static class CompoundOperator implements Operator {
        static class RecursiveContainer {
            final Joiner joiner;
            final Object[] strings;
            final int nameParameter;
            final List<Object[]> stack;
            final List<Object[]> out;
            final List<Object[]>[] lists;

            RecursiveContainer(Joiner joiner, Object[] strings, int nameParameter, List<Object[]> stack, List<Object[]> out, List<Object[]>[] lists) {
                this.joiner = joiner;
                this.strings = strings;
                this.nameParameter = nameParameter;
                this.stack = stack;
                this.out = out;
                this.lists = lists;
            }
        }
        final Operator[] operators;

        CompoundOperator(Operator...operators) {
            this.operators = operators;
        }

        @Override
        public ItemStack operate(ItemStack cleanStack) {
            for (Operator operator : operators) {
                operator.operate(cleanStack);
            }
            return cleanStack;
        }

        @Override
        public String toString() {
            return Arrays.toString(operators);
        }


        /**
         * This combines different tests into one large collection, combining no two tests from the same list.
         * @param joiner used to join names
         * @param nameParameter index of the name parameter
         * @param singletonBitmask a list of bits representing the 'singletons' located in your originalLists. Lowest order bits represent the first items in originalLists.
         *      Singletons are exponentially linked with each other, such that,
         *      the output will contain every unique subset of only items from the singletons,
         *      as well as every unique subset that contains at least one item from each non-singleton.
         * @param originalLists
         * @return
         */
        static List<Object[]> compound(final Joiner joiner, final int nameParameter, final long singletonBitmask, final List<Object[]>...originalLists) {

            final List<Object[]> out = new ArrayList<Object[]>();
            final List<List<Object[]>> singletons = new ArrayList<List<Object[]>>();
            final List<List<Object[]>> notSingletons = new ArrayList<List<Object[]>>();

            { // Separate and prime the 'singletons'
                int i = 0;
                for (List<Object[]> list : originalLists) {
                    (((singletonBitmask >>> i++) & 0x1) == 0x1 ? singletons : notSingletons).add(list);
                }
            }

            for (final List<Object[]> primarySingleton : singletons) {
                // Iterate over our singletons, to multiply the 'out' each time
                for (final Object[] entry : out.toArray(EMPTY_ARRAY)) {
                    // Iterate over a snapshot of 'out' to prevent CMEs / infinite iteration
                    final int len = entry.length;
                    for (final Object[] singleton : primarySingleton) {
                        // Iterate over each item in our singleton for the current 'out' entry
                        final Object[] toOut = entry.clone();
                        for (int i = 0; i < len; i++) {
                            // Iterate over each parameter
                            if (i == nameParameter) {
                                toOut[i] = joiner.join(toOut[i], singleton[i]);
                            } else if (toOut[i] instanceof Operator) {
                                final Operator op1 = (Operator) toOut[i];
                                final Operator op2 = (Operator) singleton[i];
                                toOut[i] = new Operator() {
                                    @Override
                                    public ItemStack operate(final ItemStack cleanStack) {
                                        return op2.operate(op1.operate(cleanStack));
                                    }
                                };
                            }
                        }
                        out.add(toOut);
                    }
                }
                out.addAll(primarySingleton);
            }

            @SuppressWarnings("unchecked")
            final List<Object[]>[] lists = new List[notSingletons.size() + 1];
            notSingletons.toArray(lists);
            lists[lists.length - 1] = out;

            final RecursiveContainer methodParams = new RecursiveContainer(joiner, new Object[lists.length], nameParameter, new ArrayList<Object[]>(lists.length), new ArrayList<Object[]>(), lists);

            recursivelyCompound(methodParams, 0);
            methodParams.out.addAll(out);

            return methodParams.out;
        }

        private static void recursivelyCompound(final RecursiveContainer methodParams, final int level) {
            final List<Object[]> stack = methodParams.stack;

            if (level == methodParams.lists.length) {
                final Object[] firstParams = stack.get(0);
                final int len = firstParams.length;
                final int stackSize = stack.size();
                final Object[] params = new Object[len];

                for (int i = 0; i < len; i++) {
                    final Object firstParam = firstParams[i];

                    if (firstParam instanceof Operator) {
                        final Operator[] operators = new Operator[stackSize];
                        for (int j = 0; j < stackSize; j++) {
                            operators[j] = (Operator) stack.get(j)[i];
                        }

                        params[i] = new CompoundOperator(operators);
                    } else if (i == methodParams.nameParameter) {
                        final Object[] strings = methodParams.strings;
                        for (int j = 0; j < stackSize; j++) {
                            strings[j] = stack.get(j)[i];
                        }

                        params[i] = methodParams.joiner.join(strings);
                    } else {
                        params[i] = firstParam;
                    }
                }

                methodParams.out.add(params);
            } else {
                final int marker = stack.size();

                for (final Object[] params : methodParams.lists[level]) {
                    stack.add(params);
                    recursivelyCompound(methodParams, level + 1);
                    stack.remove(marker);
                }
            }
        }
    }

    interface StackWrapper {
        ItemStack stack();
    }

    static class CraftWrapper implements StackWrapper {
        final StackProvider provider;

        CraftWrapper(StackProvider provider) {
            this.provider = provider;
        }

        @Override
        public ItemStack stack() {
            return provider.craft();
        }

        @Override
        public String toString() {
            return "Craft " + provider;
        }
    }

    static class BukkitWrapper implements StackWrapper {
        final StackProvider provider;

        BukkitWrapper(StackProvider provider) {
            this.provider = provider;
        }

        @Override
        public ItemStack stack() {
            return provider.bukkit();
        }

        @Override
        public String toString() {
            return "Bukkit " + provider;
        }
    }

    static class NoOpProvider extends StackProvider {

        NoOpProvider(Material material) {
            super(material);
        }

        @Override
        ItemStack operate(ItemStack cleanStack) {
            return cleanStack;
        }

        @Override
        public String toString() {
            return "NoOp " + super.toString();
        }
    }

    public static Stream<Arguments> data() {
        return Stream.empty(); // TODO, test basic durability issues
    }

    static final Object[][] EMPTY_ARRAY = new Object[0][];
    /**
     * Materials that generate unique item meta types.
     */
    static final Material[] COMPOUND_MATERIALS;
    static final int NAME_PARAMETER = 2;
    static {
        final ItemFactory factory = CraftItemFactory.instance();
        final Map<Class<? extends ItemMeta>, Material> possibleMaterials = new HashMap<Class<? extends ItemMeta>, Material>();
        ItemMeta meta;
        for (final Material material : Material.values()) {
            meta = factory.getItemMeta(material);
            if (meta == null || possibleMaterials.containsKey(meta.getClass()))
                continue;
            possibleMaterials.put(meta.getClass(), material);

        }
        COMPOUND_MATERIALS = possibleMaterials.values().toArray(new Material[possibleMaterials.size()]);
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testBukkitInequality(StackProvider provider, StackProvider unequalProvider, String name) {
        final StackWrapper bukkitWrapper = new CraftWrapper(provider);
        testInequality(bukkitWrapper, new BukkitWrapper(unequalProvider));
        testInequality(bukkitWrapper, new BukkitWrapper(new NoOpProvider(provider.material)));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testCraftInequality(StackProvider provider, StackProvider unequalProvider, String name) {
        final StackWrapper craftWrapper = new CraftWrapper(provider);
        testInequality(craftWrapper, new CraftWrapper(unequalProvider));
        testInequality(craftWrapper, new CraftWrapper(new NoOpProvider(provider.material)));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testMixedInequality(StackProvider provider, StackProvider unequalProvider, String name) {
        final StackWrapper craftWrapper = new CraftWrapper(provider);
        testInequality(craftWrapper, new BukkitWrapper(unequalProvider));
        testInequality(craftWrapper, new BukkitWrapper(new NoOpProvider(provider.material)));

        final StackWrapper bukkitWrapper = new CraftWrapper(provider);
        testInequality(bukkitWrapper, new CraftWrapper(unequalProvider));
        testInequality(bukkitWrapper, new CraftWrapper(new NoOpProvider(provider.material)));
    }

    static void testInequality(StackWrapper provider, StackWrapper unequalProvider) {
        final ItemStack stack = provider.stack();
        final ItemStack stack2 = provider.stack();
        assertThat(stack, allOf(equalTo(stack), sameHash(stack)));
        assertThat(stack, is(not(sameInstance(stack2))));
        assertThat(stack, allOf(equalTo(stack2), sameHash(stack2)));

        final ItemStack unequalStack = unequalProvider.stack();
        final ItemStack unequalStack2 = unequalProvider.stack();
        assertThat(unequalStack, allOf(equalTo(unequalStack), sameHash(unequalStack)));
        assertThat(unequalStack, is(not(sameInstance(unequalStack2))));
        assertThat(unequalStack, allOf(equalTo(unequalStack2), sameHash(unequalStack2)));

        assertThat(stack, is(not(unequalStack)));
        assertThat(unequalStack, is(not(stack)));

        final ItemStack newStack = new ItemStack(stack2);
        assertThat(newStack, allOf(equalTo(stack), sameHash(stack)));
        assertThat(newStack, is(not(unequalStack)));
        assertThat(newStack.getItemMeta(), allOf(equalTo(stack.getItemMeta()), sameHash(stack.getItemMeta())));
        assertThat(newStack.getItemMeta(), is(not(unequalStack.getItemMeta())));

        final ItemStack craftStack = CraftItemStack.asCraftCopy(stack2);
        assertThat(craftStack, allOf(equalTo(stack), sameHash(stack)));
        assertThat(craftStack, is(not(unequalStack)));
        assertThat(craftStack.getItemMeta(), allOf(equalTo(stack.getItemMeta()), sameHash(stack.getItemMeta())));
        assertThat(craftStack.getItemMeta(), is(not(unequalStack.getItemMeta())));

        final ItemStack newUnequalStack = new ItemStack(unequalStack2);
        assertThat(newUnequalStack, allOf(equalTo(unequalStack), sameHash(unequalStack)));
        assertThat(newUnequalStack, is(not(stack)));
        assertThat(newUnequalStack.getItemMeta(), allOf(equalTo(unequalStack.getItemMeta()), sameHash(unequalStack.getItemMeta())));
        assertThat(newUnequalStack.getItemMeta(), is(not(stack.getItemMeta())));

        final ItemStack newUnequalCraftStack = CraftItemStack.asCraftCopy(unequalStack2);
        assertThat(newUnequalCraftStack, allOf(equalTo(unequalStack), sameHash(unequalStack)));
        assertThat(newUnequalCraftStack, is(not(stack)));
        assertThat(newUnequalCraftStack.getItemMeta(), allOf(equalTo(unequalStack.getItemMeta()), sameHash(unequalStack.getItemMeta())));
        assertThat(newUnequalCraftStack.getItemMeta(), is(not(stack.getItemMeta())));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testBukkitYamlDeserialize(StackProvider provider, StackProvider unequalProvider, String name) throws Throwable {
        testYamlDeserialize(new BukkitWrapper(provider), new BukkitWrapper(unequalProvider));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testCraftYamlDeserialize(StackProvider provider, StackProvider unequalProvider, String name) throws Throwable {
        testYamlDeserialize(new CraftWrapper(provider), new CraftWrapper(unequalProvider));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testBukkitStreamDeserialize(StackProvider provider, StackProvider unequalProvider, String name) throws Throwable {
        testStreamDeserialize(new BukkitWrapper(provider), new BukkitWrapper(unequalProvider));
    }

    @ParameterizedTest(name = "[{index}]:{" + NAME_PARAMETER + "}")
    @MethodSource({"data",
            "org.bukkit.craftbukkit.inventory.ItemStackSkullTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackPotionsTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackMapTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLoreEnchantmentTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackLeatherTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackFireworkChargeTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackEnchantStorageTest#data",
            "org.bukkit.craftbukkit.inventory.ItemStackBookTest#data"
    })
    public void testCraftStreamDeserialize(StackProvider provider, StackProvider unequalProvider, String name) throws Throwable {
        testStreamDeserialize(new CraftWrapper(provider), new CraftWrapper(unequalProvider));
    }

    static void testStreamDeserialize(StackWrapper provider, StackWrapper unequalProvider) throws Throwable {
        final ItemStack stack = provider.stack();
        final ItemStack unequalStack = unequalProvider.stack();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new BukkitObjectOutputStream(out);

            oos.writeObject(stack);
            oos.writeObject(unequalStack);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                }
            }
        }

        final String data = new String(Base64Coder.encode(out.toByteArray()));

        ObjectInputStream ois = null;

        final ItemStack readFirst;
        final ItemStack readSecond;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ois = new BukkitObjectInputStream(in);

            readFirst = (ItemStack) ois.readObject();
            readSecond = (ItemStack) ois.readObject();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                }
            }
        }

        testEqualities(data, readFirst, readSecond, stack, unequalStack);
    }

    static void testYamlDeserialize(StackWrapper provider, StackWrapper unequalProvider) {
        final ItemStack stack = provider.stack();
        final ItemStack unequalStack = unequalProvider.stack();
        final YamlConfiguration configOut = new YamlConfiguration();

        configOut.set("provider", stack);
        configOut.set("unequal", unequalStack);

        final String out = '\n' + configOut.saveToString();
        final YamlConfiguration configIn = new YamlConfiguration();

        try {
            configIn.loadFromString(out);
        } catch (InvalidConfigurationException ex) {
            throw new RuntimeException(out, ex);
        }

        testEqualities(out, configIn.getItemStack("provider"), configIn.getItemStack("unequal"), stack, unequalStack);
    }

    static void testEqualities(String information, ItemStack primaryRead, ItemStack unequalRead, ItemStack primaryOriginal, ItemStack unequalOriginal) {
        assertThat(primaryRead, allOf(equalTo(primaryOriginal), sameHash(primaryOriginal)), information);
        assertThat(unequalRead, allOf(equalTo(unequalOriginal), sameHash(unequalOriginal)), information);
        assertThat(primaryRead, is(not(unequalOriginal)), information);
        assertThat(primaryRead, is(not(unequalRead)), information);
    }
}
