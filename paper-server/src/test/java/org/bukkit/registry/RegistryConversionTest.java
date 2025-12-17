package org.bukkit.registry;

import com.google.common.base.Joiner;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.test.RegistriesTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@AllFeatures
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistryConversionTest {

    private static final Set<Class<? extends Keyed>> IMPLEMENT_HOLDERABLE = new HashSet<>();

    public static Stream<Holderable<?>> getValues(RegistryKey<? extends Keyed> registryKey) {
        Registry<?> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        return registry.stream().map(keyed -> (Holderable<?>) keyed);
    }

    @Order(1)
    @RegistriesTest
    public void testHolderableImplementation(RegistryKey<? extends Keyed> apiRegistryKey, Class<? extends Keyed> clazz) {
        Set<Class<? extends Keyed>> notImplemented = new HashSet<>();
        Registry<? extends Keyed> registry = RegistryAccess.registryAccess().getRegistry(apiRegistryKey);

        for (Keyed item : registry) {
            if (!(item instanceof Holderable<?>)) {
                notImplemented.add(item.getClass());
            }
        }

        assertTrue(notImplemented.isEmpty(), String.format("""
            Not all implementations of the registry from the class %s have the Holderable interface implemented.
            Every Implementation should implement the Holderable interface.
            
            The following implementation do not implement Holderable:
            %s""", clazz.getName(), Joiner.on('\n').join(notImplemented)));
        RegistryConversionTest.IMPLEMENT_HOLDERABLE.add(clazz);
    }

    @Order(2)
    @RegistriesTest
    public void testMinecraftHolderToBukkit(RegistryKey<? extends Keyed> apiRegistryKey, Class<? extends Keyed> clazz, ResourceKey<net.minecraft.core.Registry<?>> registryKey) {
        this.checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();
        getValues(apiRegistryKey).forEach(bukkit -> {
            Object otherBukkit = CraftRegistry.minecraftHolderToBukkit(bukkit.getHolder(), (ResourceKey) registryKey);
            if (bukkit != otherBukkit) {
                notMatching.put(bukkit, otherBukkit);
            }
        });

        assertTrue(notMatching.isEmpty(), String.format("""
                The conversion method does not match all registry items correctly.
                
                Following registry items where match not correctly:
                %s""",
            Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    @Order(2)
    @RegistriesTest
    public void testBukkitToMinecraftHolder(RegistryKey<? extends Keyed> apiRegistryKey, Class<? extends Keyed> clazz) {
        this.checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();

        getValues(apiRegistryKey).forEach(bukkit -> {
            Object minecraft = bukkit.getHolder();
            Object otherMinecraft = CraftRegistry.bukkitToMinecraftHolder((Keyed) bukkit);
            if (minecraft != otherMinecraft) {
                notMatching.put(minecraft, otherMinecraft);
            }
        });

        assertTrue(notMatching.isEmpty(), String.format("""
                The conversion method does not match all registry items correctly.
                
                Following registry items where match not correctly:
                %s""", Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    /**
     * Minecraft registry can return a default key / value
     * when the passed minecraft value is not registry in this case, we want it to throw an error.
     */
    @Order(2)
    @RegistriesTest
    public void testMinecraftToBukkitNoValidMinecraft(
        RegistryKey<? extends Keyed> apiRegistryKey, Class<? extends Keyed> clazz, ResourceKey<net.minecraft.core.Registry<?>> registryKey,
        Class<? extends Keyed> craftClazz, Class<?> minecraftClazz
    ) {
        final Registry<?> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(apiRegistryKey);
        assumeFalse(((CraftRegistry<?, ?>) bukkitRegistry).supportsDirectHolders(), "skipped because these types support direct holders");
        Object minecraft = mock(minecraftClazz);
        assertThrows(IllegalStateException.class, () -> CraftRegistry.minecraftToBukkit(minecraft, (ResourceKey) registryKey),
            "Conversion method should not accept a none registered value and should throw a IllegalStateException.");
    }

    private void checkValidHandle(Class<? extends Keyed> clazz) {
        assumeTrue(RegistryConversionTest.IMPLEMENT_HOLDERABLE.contains(clazz), String.format("""
            Cannot test class %s, because it does not implement Holderable.
            
            Check test results of testHolderableImplementation for more information.
            """, clazz.getName()));
    }
}
