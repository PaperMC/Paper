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
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@AllFeatures
public class PerRegistryTest {

    private static Random random;

    @BeforeAll
    public static void init() {
        PerRegistryTest.random = new Random();
    }

    public static Stream<Arguments> data() {
        List<Arguments> data = Lists.newArrayList();

        Field[] registryFields = Registry.class.getFields();
        for (Field registryField : registryFields) {
            try {
                Object object = registryField.get(null);
                // Ignore Bukkit's default SimpleRegistry. It cannot be tested correctly
                if (object instanceof Registry.NotARegistry) {
                    continue;
                }

                data.add(Arguments.of(object));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return data.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public <T extends Keyed> void testGet(Registry<T> registry) { // Paper - improve Registry
        registry.forEach(element -> {
            NamespacedKey key = registry.getKey(element); // Paper - improve Registry
            assertNotNull(key); // Paper - improve Registry
            // Values in the registry should be referentially equal to what is returned with #get()
            // This ensures that new instances are not created each time #get() is invoked
            assertSame(element, registry.get(key)); // Paper - improve Registry
        });
    }

    @ParameterizedTest
    @MethodSource("data")
    public <T extends Keyed> void testMatch(Registry<T> registry) { // Paper - improve Registry
        registry.forEach(element -> {
            NamespacedKey key = registry.getKey(element); // Paper - improve Registry
            assertNotNull(key); // Paper - improve Registry

            this.assertSameMatchWithKeyMessage(registry, element, key.toString()); // namespace:key
            this.assertSameMatchWithKeyMessage(registry, element, key.getKey()); // key
            this.assertSameMatchWithKeyMessage(registry, element, key.toString().replace('_', ' ')); // namespace:key with space
            this.assertSameMatchWithKeyMessage(registry, element, key.getKey().replace('_', ' ')); // key with space
            this.assertSameMatchWithKeyMessage(registry, element, this.randomizeCase(key.toString())); // nAmeSPaCe:kEY
            this.assertSameMatchWithKeyMessage(registry, element, this.randomizeCase(key.getKey())); // kEy
        });
    }

    private <T extends Keyed> void assertSameMatchWithKeyMessage(Registry<T> registry, T element, String key) { // Paper - improve Registry
        assertSame(element, registry.match(key), key);
    }

    private String randomizeCase(String input) {
        int size = input.length();
        StringBuilder builder = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            char character = input.charAt(i);
            builder.append(PerRegistryTest.random.nextBoolean() ? Character.toUpperCase(character) : character);
        }

        return builder.toString();
    }
}
