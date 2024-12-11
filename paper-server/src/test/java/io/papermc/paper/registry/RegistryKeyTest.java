package io.papermc.paper.registry;

import io.papermc.paper.registry.entry.RegistryEntry;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
class RegistryKeyTest {

    @BeforeAll
    static void before() throws ClassNotFoundException {
        Class.forName(RegistryKey.class.getName()); // load all keys so they are found for the test
    }

    static Stream<RegistryKey<?>> data() {
        return RegistryKeyImpl.REGISTRY_KEYS.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    void testApiRegistryKeysExist(final RegistryKey<?> key) {
        final Optional<Registry<Object>> registry = RegistryHelper.getRegistry().lookup(ResourceKey.createRegistryKey(ResourceLocation.parse(key.key().asString())));
        assertTrue(registry.isPresent(), "Missing vanilla registry for " + key.key().asString());
    }

    @ParameterizedTest
    @MethodSource("data")
    void testRegistryEntryExists(final RegistryKey<?> key) {
        final @Nullable RegistryEntry<?, ?> entry = PaperRegistries.getEntry(key);
        assertNotNull(entry, "Missing PaperRegistries entry for " + key);
    }
}
