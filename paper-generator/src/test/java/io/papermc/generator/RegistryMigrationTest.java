package io.papermc.generator;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegistryMigrationTest {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();
    }

    @Test
    public void testBuiltInToDataDriven() {
        Set<String> migratedRegistries = new HashSet<>();
        for (RegistryEntry<?> entry : RegistryEntries.BUILT_IN) {
            ResourceKey<? extends Registry<?>> key = entry.registryKey();
            if (!BuiltInRegistries.REGISTRY.containsKey(key.location())) {
                migratedRegistries.add(key.toString());
            }
        }

        Assertions.assertTrue(migratedRegistries.isEmpty(), () -> "Some registries have become data-driven: %s".formatted(String.join(", ", migratedRegistries)));
    }

    @Test
    public void testDataDrivenToBuiltIn() { // shouldn't really happen but just in case
        Set<String> migratedRegistries = new HashSet<>();
        for (RegistryEntry<?> entry : RegistryEntries.DATA_DRIVEN) {
            ResourceKey<? extends Registry<?>> key = entry.registryKey();
            if (BuiltInRegistries.REGISTRY.containsKey(key.location())) {
                migratedRegistries.add(key.toString());
            }
        }

        Assertions.assertTrue(migratedRegistries.isEmpty(), () -> "Some registries have become built-in: %s".formatted(String.join(", ", migratedRegistries)));
    }
}
