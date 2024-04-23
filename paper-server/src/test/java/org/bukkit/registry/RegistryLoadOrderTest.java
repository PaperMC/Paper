package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.AbstractTestingBase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RegistryLoadOrderTest extends AbstractTestingBase {

    private static boolean initInterface = false;
    private static boolean initAbstract = false;
    private static Registry<Keyed> registry;

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(
                        (Supplier<Boolean>) () -> initInterface,
                        BukkitInterfaceTestType.class,
                        (BiFunction<NamespacedKey, MinecraftTestType, Keyed>) CraftBukkitInterfaceTestType::new,
                        (Supplier<Keyed>) () -> BukkitInterfaceTestType.TEST_ONE,
                        (Supplier<Keyed>) () -> BukkitInterfaceTestType.TEST_TWO
                ),
                Arguments.of(
                        (Supplier<Boolean>) () -> initAbstract,
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
        testClassNotLoaded(init.get());

        ResourceKey<IRegistry<MinecraftTestType>> resourceKey = ResourceKey.createRegistryKey(MinecraftKey.tryBuild("bukkit", "test-registry"));
        RegistryMaterials<MinecraftTestType> minecraftRegistry = new RegistryMaterials<>(resourceKey, Lifecycle.experimental());

        minecraftRegistry.register(ResourceKey.create(resourceKey, MinecraftKey.tryBuild("bukkit", "test-one")), new MinecraftTestType(), new RegistrationInfo(Optional.empty(), Lifecycle.experimental()));
        minecraftRegistry.register(ResourceKey.create(resourceKey, MinecraftKey.tryBuild("bukkit", "test-two")), new MinecraftTestType(), new RegistrationInfo(Optional.empty(), Lifecycle.experimental()));
        minecraftRegistry.freeze();

        registry = new CraftRegistry<>(keyedClass, minecraftRegistry, minecraftToBukkit);
        testClassNotLoaded(init.get());

        Object testOne = registry.get(new NamespacedKey("bukkit", "test-one"));
        Object otherTestOne = registry.get(new NamespacedKey("bukkit", "test-one"));
        Object testTwo = registry.get(new NamespacedKey("bukkit", "test-two"));

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
        BukkitInterfaceTestType TEST_ONE = get("test-one");
        BukkitInterfaceTestType TEST_TWO = get("test-two");

        private static BukkitInterfaceTestType get(String key) {
            initInterface = true;
            if (registry == null) {
                return null;
            }

            return (BukkitInterfaceTestType) registry.get(new NamespacedKey("bukkit", key));
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
            return key;
        }
    }

    public abstract static class BukkitAbstractTestType implements Keyed {
        public static final BukkitAbstractTestType TEST_ONE = get("test-one");
        public static final BukkitAbstractTestType TEST_TWO = get("test-two");

        private static BukkitAbstractTestType get(String key) {
            initAbstract = true;
            if (registry == null) {
                return null;
            }

            return (BukkitAbstractTestType) registry.get(new NamespacedKey("bukkit", key));
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
            return key;
        }
    }

    public static class MinecraftTestType {
    }
}
