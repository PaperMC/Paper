package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.environment.Normal;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Normal
public class RegistryLoadOrderTest {

    private static boolean initInterface = false;
    private static boolean initAbstract = false;
    private static Registry<Keyed> registry;

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(
                        (Supplier<Boolean>) () -> RegistryLoadOrderTest.initInterface,
                        BukkitInterfaceTestType.class,
                        (BiFunction<NamespacedKey, MinecraftTestType, Keyed>) CraftBukkitInterfaceTestType::new,
                        (Supplier<Keyed>) () -> BukkitInterfaceTestType.TEST_ONE,
                        (Supplier<Keyed>) () -> BukkitInterfaceTestType.TEST_TWO
                ),
                Arguments.of(
                        (Supplier<Boolean>) () -> RegistryLoadOrderTest.initAbstract,
                        BukkitAbstractTestType.class,
                        (BiFunction<NamespacedKey, MinecraftTestType, Keyed>) CraftBukkitAbstractTestType::new,
                        (Supplier<Keyed>) () -> BukkitAbstractTestType.TEST_ONE,
                        (Supplier<Keyed>) () -> BukkitAbstractTestType.TEST_TWO
                )
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRegistryLoadOrder(Supplier<Boolean> init, Class<Keyed> keyedClass, BiFunction<NamespacedKey, MinecraftTestType, Keyed> minecraftToBukkit, Supplier<Keyed> first, Supplier<Keyed> second) {
        this.testClassNotLoaded(init.get());

        ResourceKey<net.minecraft.core.Registry<MinecraftTestType>> resourceKey = ResourceKey.createRegistryKey(ResourceLocation.tryBuild("bukkit", "test-registry"));
        MappedRegistry<MinecraftTestType> minecraftRegistry = new MappedRegistry<>(resourceKey, Lifecycle.experimental());

        minecraftRegistry.register(ResourceKey.create(resourceKey, ResourceLocation.tryBuild("bukkit", "test-one")), new MinecraftTestType(), new RegistrationInfo(Optional.empty(), Lifecycle.experimental()));
        minecraftRegistry.register(ResourceKey.create(resourceKey, ResourceLocation.tryBuild("bukkit", "test-two")), new MinecraftTestType(), new RegistrationInfo(Optional.empty(), Lifecycle.experimental()));
        minecraftRegistry.freeze();

        RegistryLoadOrderTest.registry = new CraftRegistry<>(keyedClass, minecraftRegistry, minecraftToBukkit, (namespacedKey, apiVersion) -> namespacedKey);
        this.testClassNotLoaded(init.get());
        ((CraftRegistry<?, ?>) RegistryLoadOrderTest.registry).lockReferenceHolders();

        Object testOne = RegistryLoadOrderTest.registry.get(new NamespacedKey("bukkit", "test-one"));
        Object otherTestOne = RegistryLoadOrderTest.registry.get(new NamespacedKey("bukkit", "test-one"));
        Object testTwo = RegistryLoadOrderTest.registry.get(new NamespacedKey("bukkit", "test-two"));

        assertNotNull(testOne);
        assertNotNull(otherTestOne);
        assertNotNull(testTwo);
        assertNotNull(first.get());
        assertNotNull(second.get());

        assertSame(testOne, otherTestOne);
        assertSame(testOne, first.get());
        assertSame(otherTestOne, first.get());
        assertSame(testTwo, second.get());

        assertTrue(init.get());
    }

    private void testClassNotLoaded(boolean init) {
        assertFalse(init, """
                TestType class is already loaded, this test however tests the behavior when the class is not loaded.
                This should normally not happen with how classes should be loaded.
                Something has changed how classes are loaded and a more manual deeper look is required.
                """);
    }

    public interface BukkitInterfaceTestType extends Keyed {
        BukkitInterfaceTestType TEST_ONE = BukkitInterfaceTestType.get("test-one");
        BukkitInterfaceTestType TEST_TWO = BukkitInterfaceTestType.get("test-two");

        private static BukkitInterfaceTestType get(String key) {
            RegistryLoadOrderTest.initInterface = true;
            if (RegistryLoadOrderTest.registry == null) {
                return null;
            }

            return (BukkitInterfaceTestType) RegistryLoadOrderTest.registry.get(new NamespacedKey("bukkit", key));
        }
    }

    public static class CraftBukkitInterfaceTestType implements BukkitInterfaceTestType {

        private final NamespacedKey key;

        public CraftBukkitInterfaceTestType(NamespacedKey key, MinecraftTestType minecraftTestType) {
            this.key = key;
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return this.key;
        }
    }

    public abstract static class BukkitAbstractTestType implements Keyed {
        public static final BukkitAbstractTestType TEST_ONE = BukkitAbstractTestType.get("test-one");
        public static final BukkitAbstractTestType TEST_TWO = BukkitAbstractTestType.get("test-two");

        private static BukkitAbstractTestType get(String key) {
            RegistryLoadOrderTest.initAbstract = true;
            if (RegistryLoadOrderTest.registry == null) {
                return null;
            }

            return (BukkitAbstractTestType) RegistryLoadOrderTest.registry.get(new NamespacedKey("bukkit", key));
        }
    }

    public static class CraftBukkitAbstractTestType extends BukkitAbstractTestType {

        private final NamespacedKey key;

        public CraftBukkitAbstractTestType(NamespacedKey key, MinecraftTestType minecraftTestType) {
            this.key = key;
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return this.key;
        }
    }

    public static class MinecraftTestType {
    }
}
