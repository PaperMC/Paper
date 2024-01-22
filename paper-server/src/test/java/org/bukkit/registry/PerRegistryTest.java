package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PerRegistryTest extends AbstractTestingBase {

    private static Random random;

    @BeforeAll
    public static void init() {
        random = new Random();
    }

    public static Stream<Arguments> data() {
        List<Arguments> data = Lists.newArrayList();

        Field[] registryFields = Registry.class.getFields();
        for (Field registryField : registryFields) {
            try {
                Object object = registryField.get(null);
                // Ignore Bukkit's default SimpleRegistry. It cannot be tested correctly
                if (!(object instanceof CraftRegistry<?, ?> registry)) {
                    continue;
                }

                data.add(Arguments.of(registry));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return data.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGet(Registry<?> registry) {
        registry.forEach(element -> {
            // Values in the registry should be referentially equal to what is returned with #get()
            // This ensures that new instances are not created each time #get() is invoked
            assertSame(element, registry.get(element.getKey()));
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testMatch(Registry<?> registry) {
        registry.forEach(element -> {
            NamespacedKey key = element.getKey();

            assertSameMatchWithKeyMessage(registry, element, key.toString()); // namespace:key
            assertSameMatchWithKeyMessage(registry, element, key.getKey()); // key
            assertSameMatchWithKeyMessage(registry, element, key.toString().replace('_', ' ')); // namespace:key with space
            assertSameMatchWithKeyMessage(registry, element, key.getKey().replace('_', ' ')); // key with space
            assertSameMatchWithKeyMessage(registry, element, randomizeCase(key.toString())); // nAmeSPaCe:kEY
            assertSameMatchWithKeyMessage(registry, element, randomizeCase(key.getKey())); // kEy
        });
    }

    private void assertSameMatchWithKeyMessage(Registry<?> registry, Keyed element, String key) {
        assertSame(element, registry.match(key), key);
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
