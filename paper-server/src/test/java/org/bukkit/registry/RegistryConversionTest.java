package org.bukkit.registry;

import com.google.common.base.Joiner;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.test.RegistriesTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@AllFeatures
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistryConversionTest {

    private static final String MINECRAFT_TO_BUKKIT = "minecraftToBukkit";
    private static final String BUKKIT_TO_MINECRAFT = "bukkitToMinecraft";

    private static final String MINECRAFT_TO_BUKKIT_NEW = "minecraftToBukkitNew";
    private static final String BUKKIT_TO_MINECRAFT_NEW = "bukkitToMinecraftNew";

    private static final Map<Class<? extends Keyed>, Method> MINECRAFT_TO_BUKKIT_METHODS = new HashMap<>();
    private static final Map<Class<? extends Keyed>, Method> BUKKIT_TO_MINECRAFT_METHODS = new HashMap<>();

    private static final Set<Class<? extends Keyed>> IMPLEMENT_HANDLE_ABLE = new HashSet<>();

    public static Stream<? extends Arguments> getValues(RegistryKey<? extends Keyed> registryType) { // Paper
        Registry<?> registry = RegistryAccess.registryAccess().getRegistry(registryType); // Paper
        return registry.stream().map(keyed -> (Handleable<?>) keyed)
            .map(handleAble -> Arguments.of(handleAble, handleAble.getHandle()));
    }

    @Order(1)
    @RegistriesTest
    public void testHandleableImplementation(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz) { // Paper
        Set<Class<? extends Keyed>> notImplemented = new HashSet<>();
        Registry<? extends Keyed> registry = io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(type); // Paper

        for (Keyed item : registry) {
            if (!(item instanceof Handleable<?>)) {
                notImplemented.add(item.getClass());
            }
        }

        assertTrue(notImplemented.isEmpty(), String.format("""
                Not all implementations of the registry from the class %s have the Handleable interface implemented.
                Every Implementation should implement the Handleable interface.

                The following implementation do not implement Handleable:
                %s""", clazz.getName(), Joiner.on('\n').join(notImplemented)));

        RegistryConversionTest.IMPLEMENT_HANDLE_ABLE.add(clazz);
    }

    @Order(2)
    @RegistriesTest
    public void testMinecraftToBukkitPresent(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz, ResourceKey<net.minecraft.core.Registry<?>> registryKey,
                                             Class<? extends Keyed> craftClazz, Class<?> minecraftClazz, boolean newMethod) {
        String methodName = (newMethod) ? RegistryConversionTest.MINECRAFT_TO_BUKKIT_NEW : RegistryConversionTest.MINECRAFT_TO_BUKKIT;
        Method method = null;
        try {
            method = craftClazz.getDeclaredMethod(methodName, minecraftClazz);
        } catch (NoSuchMethodException e) {
            fail(String.format("""
                    The class %s does not have a public static method to convert a minecraft value to a bukkit value.

                    Following method should be add which, returns the bukkit value based on the minecraft value.
                    %s
                    """, craftClazz, this.buildMinecraftToBukkitMethod(clazz, methodName, minecraftClazz)));
        }

        assertTrue(Modifier.isPublic(method.getModifiers()), String.format("""
                The method %s in class %s is not public.

                The method should be made public, method structure:
                %s
                """, methodName, craftClazz, this.buildMinecraftToBukkitMethod(clazz, methodName, minecraftClazz)));

        assertTrue(Modifier.isStatic(method.getModifiers()), String.format("""
                The method %s in class %s is not static.

                The method should be made static, method structure:
                %s
                """, methodName, craftClazz, this.buildMinecraftToBukkitMethod(clazz, methodName, minecraftClazz)));

        assertSame(clazz, method.getReturnType(), String.format("""
                The method %s in class %s has the wrong return value.

                The method should have the correct return value, method structure:
                %s
                """, methodName, craftClazz, this.buildMinecraftToBukkitMethod(clazz, methodName, minecraftClazz)));

        RegistryConversionTest.MINECRAFT_TO_BUKKIT_METHODS.put(clazz, method);
    }

    private String buildMinecraftToBukkitMethod(Class<? extends Keyed> clazz, String methodName, Class<?> minecraftClazz) {
        return String.format("""
                public static %s %s(%s minecraft) {
                    [...]
                }
                """, clazz.getSimpleName(), methodName, minecraftClazz.getName());
    }

    @Order(2)
    @RegistriesTest
    public void testBukkitToMinecraftPresent(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz, ResourceKey<net.minecraft.core.Registry<?>> registryKey,
                                             Class<? extends Keyed> craftClazz, Class<?> minecraftClazz, boolean newMethod) {
        String methodName = (newMethod) ? RegistryConversionTest.BUKKIT_TO_MINECRAFT_NEW : RegistryConversionTest.BUKKIT_TO_MINECRAFT;
        Method method = null;
        try {
            method = craftClazz.getDeclaredMethod(methodName, clazz);
        } catch (NoSuchMethodException e) {
            fail(String.format("""
                    The class %s does not have a public static method to convert a bukkit value to a minecraft value.

                    Following method should be add which, returns the minecraft value based on the bukkit value.
                    %s
                    """, craftClazz, this.buildBukkitToMinecraftMethod(clazz, methodName, minecraftClazz)));
        }

        assertTrue(Modifier.isPublic(method.getModifiers()), String.format("""
                The method %s in class %s is not public.

                The method should be made public, method structure:
                %s
                """, methodName, craftClazz, this.buildBukkitToMinecraftMethod(clazz, methodName, minecraftClazz)));

        assertTrue(Modifier.isStatic(method.getModifiers()), String.format("""
                The method %s in class %s is not static.

                The method should be made static, method structure:
                %s
                """, methodName, craftClazz, this.buildBukkitToMinecraftMethod(clazz, methodName, minecraftClazz)));

        assertSame(minecraftClazz, method.getReturnType(), String.format("""
                The method %s in class %s has the wrong return value.

                The method should have the correct return value, method structure:
                %s
                """, methodName, craftClazz, this.buildBukkitToMinecraftMethod(clazz, methodName, minecraftClazz)));

        RegistryConversionTest.BUKKIT_TO_MINECRAFT_METHODS.put(clazz, method);
    }

    private String buildBukkitToMinecraftMethod(Class<? extends Keyed> clazz, String methodName, Class<?> minecraftClazz) {
        return String.format("""
                public static %s %s(%s bukkit) {
                    [...]
                }
                """, minecraftClazz.getName(), methodName, clazz.getSimpleName());
    }

    @Order(3)
    @RegistriesTest
    public void testMinecraftToBukkitNullValue(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz) throws IllegalAccessException { // Paper
        this.checkValidMinecraftToBukkit(clazz);

        try {
            Object result = RegistryConversionTest.MINECRAFT_TO_BUKKIT_METHODS.get(clazz).invoke(null, (Object) null);
            fail(String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    Got '%s' as return object.
                    """, RegistryConversionTest.MINECRAFT_TO_BUKKIT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalArgumentException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    """, RegistryConversionTest.MINECRAFT_TO_BUKKIT, clazz.getName()));
        }
    }

    @Order(3)
    @RegistriesTest
    public void testBukkitToMinecraftNullValue(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz) throws IllegalAccessException { // Paper
        this.checkValidBukkitToMinecraft(clazz);

        try {
            Object result = RegistryConversionTest.BUKKIT_TO_MINECRAFT_METHODS.get(clazz).invoke(null, (Object) null);
            fail(String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    Got '%s' as return object.
                    """, RegistryConversionTest.BUKKIT_TO_MINECRAFT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalArgumentException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    """, RegistryConversionTest.BUKKIT_TO_MINECRAFT, clazz.getName()));
        }
    }

    @Order(3)
    @RegistriesTest
    public void testMinecraftToBukkit(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz) { // Paper
        this.checkValidMinecraftToBukkit(clazz);
        this.checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();
        Method method = RegistryConversionTest.MINECRAFT_TO_BUKKIT_METHODS.get(clazz);

        getValues(type).map(Arguments::get).forEach(arguments -> { // Paper
            Keyed bukkit = (Keyed) arguments[0];
            Object minecraft = arguments[1];

            try {
                Object otherBukkit = method.invoke(null, minecraft);
                if (bukkit != otherBukkit) {
                    notMatching.put(bukkit, otherBukkit);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        assertTrue(notMatching.isEmpty(), String.format("""
                        The method %s in class %s does not match all registry items correctly.

                        Following registry items where match not correctly:
                        %s""", RegistryConversionTest.MINECRAFT_TO_BUKKIT, clazz.getName(),
                Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    @Order(3)
    @RegistriesTest
    public void testBukkitToMinecraft(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz) { // Paper
        this.checkValidBukkitToMinecraft(clazz);
        this.checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();
        Method method = RegistryConversionTest.BUKKIT_TO_MINECRAFT_METHODS.get(clazz);

        getValues(type).map(Arguments::get).forEach(arguments -> { // Paper
            Keyed bukkit = (Keyed) arguments[0];
            Object minecraft = arguments[1];

            try {
                Object otherMinecraft = method.invoke(null, bukkit);
                if (minecraft != otherMinecraft) {
                    notMatching.put(minecraft, otherMinecraft);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        assertTrue(notMatching.isEmpty(), String.format("""
                        The method %s in class %s does not match all registry items correctly.

                        Following registry items where match not correctly:
                        %s""", RegistryConversionTest.BUKKIT_TO_MINECRAFT, clazz.getName(),
                Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    static final Set<RegistryKey<?>> IGNORE_FOR_DIRECT_HOLDER = Set.of(RegistryKey.TRIM_MATERIAL, RegistryKey.TRIM_PATTERN, RegistryKey.INSTRUMENT, RegistryKey.BANNER_PATTERN, RegistryKey.SOUND_EVENT, RegistryKey.DIALOG); // Paper

    /**
     * Minecraft registry can return a default key / value
     * when the passed minecraft value is not registry in this case, we want it to throw an error.
     */
    @Order(3)
    @RegistriesTest
    public void testMinecraftToBukkitNoValidMinecraft(io.papermc.paper.registry.RegistryKey<? extends Keyed> type, Class<? extends Keyed> clazz, ResourceKey<net.minecraft.core.Registry<?>> registryKey, // Paper
                                                      Class<? extends Keyed> craftClazz, Class<?> minecraftClazz) throws IllegalAccessException {
        this.checkValidMinecraftToBukkit(clazz);

        assumeFalse(IGNORE_FOR_DIRECT_HOLDER.contains(type), "skipped because these types support direct holders"); // Paper - manually skip for now
        try {

            Object minecraft = mock(minecraftClazz);
            Object result = RegistryConversionTest.MINECRAFT_TO_BUKKIT_METHODS.get(clazz).invoke(null, minecraft);
            fail(String.format("""
                    Method %s in class %s should not accept a none registered value and should throw a IllegalStateException.
                    Got '%s' as return object.
                    """, RegistryConversionTest.MINECRAFT_TO_BUKKIT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalStateException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept a none registered value and should throw a IllegalStateException.
                    """, RegistryConversionTest.MINECRAFT_TO_BUKKIT, clazz.getName()));
        }
    }

    private void checkValidBukkitToMinecraft(Class<? extends Keyed> clazz) {
        assumeTrue(RegistryConversionTest.BUKKIT_TO_MINECRAFT_METHODS.containsKey(clazz), String.format("""
                Cannot test class %s, because it does not have a valid %s method.

                Check test results of testBukkitToMinecraftPresent for more information.
                """, clazz.getName(), RegistryConversionTest.BUKKIT_TO_MINECRAFT));
    }

    private void checkValidMinecraftToBukkit(Class<? extends Keyed> clazz) {
        assumeTrue(RegistryConversionTest.MINECRAFT_TO_BUKKIT_METHODS.containsKey(clazz), String.format("""
                Cannot test class %s, because it does not have a valid %s method.

                Check test results of testMinecraftToBukkitPresent for more information.
                """, clazz.getName(), RegistryConversionTest.MINECRAFT_TO_BUKKIT));
    }

    private void checkValidHandle(Class<? extends Keyed> clazz) {
        assumeTrue(RegistryConversionTest.IMPLEMENT_HANDLE_ABLE.contains(clazz), String.format("""
                Cannot test class %s, because it does not implement Handleable.

                Check test results of testHandleableImplementation for more information.
                """, clazz.getName()));
    }
}
