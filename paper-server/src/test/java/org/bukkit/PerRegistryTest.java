package org.bukkit;

import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PerRegistryTest extends AbstractTestingBase {

    private static Random random;

    @BeforeClass
    public static void init() {
        random = new Random();
    }

    @Parameters(name = "{index}: {0}")
    public static List<Object[]> data() {
        List<Object[]> data = Lists.newArrayList();

        Field[] registryFields = Registry.class.getFields();
        for (Field registryField : registryFields) {
            try {
                Object object = registryField.get(null);
                // Ignore Bukkit's default SimpleRegistry. It cannot be tested correctly
                if (!(object instanceof CraftRegistry<?, ?> registry)) {
                    continue;
                }

                data.add(new Object[] {registry});
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    @Parameter public Registry<? extends Keyed> registry;

    @Test
    public void testGet() {
        this.registry.forEach(element -> {
            // Values in the registry should be referentially equal to what is returned with #get()
            // This ensures that new instances are not created each time #get() is invoked
            Assert.assertSame(element, registry.get(element.getKey()));
        });
    }

    @Test
    public void testMatch() {
        this.registry.forEach(element -> {
            NamespacedKey key = element.getKey();

            assertSameMatchWithKeyMessage(element, key.toString()); // namespace:key
            assertSameMatchWithKeyMessage(element, key.getKey()); // key
            assertSameMatchWithKeyMessage(element, key.toString().replace('_', ' ')); // namespace:key with space
            assertSameMatchWithKeyMessage(element, key.getKey().replace('_', ' ')); // key with space
            assertSameMatchWithKeyMessage(element, randomizeCase(key.toString())); // nAmeSPaCe:kEY
            assertSameMatchWithKeyMessage(element, randomizeCase(key.getKey())); // kEy
        });
    }

    private void assertSameMatchWithKeyMessage(Keyed element, String key) {
        Assert.assertSame(key, element, registry.match(key));
    }

    private String randomizeCase(String input) {
        int size = input.length();
        StringBuilder builder = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            char character = input.charAt(i);
            builder.append(random.nextBoolean() ? Character.toUpperCase(character) : character);
        }

        return builder.toString();
    }
}
