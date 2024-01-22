package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.Mockito.*;
import com.google.common.base.Joiner;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.support.provider.RegistryArgumentProvider;
import org.bukkit.support.test.RegistriesTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.provider.Arguments;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistryConversionTest extends AbstractTestingBase {

    private static final String MINECRAFT_TO_BUKKIT = "minecraftToBukkit";
    private static final String BUKKIT_TO_MINECRAFT = "bukkitToMinecraft";

    private static final Map<Class<? extends Keyed>, Method> MINECRAFT_TO_BUKKIT_METHODS = new HashMap<>();
    private static final Map<Class<? extends Keyed>, Method> BUKKIT_TO_MINECRAFT_METHODS = new HashMap<>();

    private static final Set<Class<? extends Keyed>> IMPLEMENT_HANDLE_ABLE = new HashSet<>();

    @Order(1)
    @RegistriesTest
    public void testHandleableImplementation(Class<? extends Keyed> clazz) {
        Set<Class<? extends Keyed>> notImplemented = new HashSet<>();
        Registry<? extends Keyed> registry = Bukkit.getRegistry(clazz);

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

        IMPLEMENT_HANDLE_ABLE.add(clazz);
    }

    @Order(2)
    @RegistriesTest
    public void testMinecraftToBukkitPresent(Class<? extends Keyed> clazz, ResourceKey<IRegistry<?>> registryKey,
                                             Class<? extends Keyed> craftClazz, Class<?> minecraftClazz) {
        Method method = null;
        try {
            method = craftClazz.getDeclaredMethod(MINECRAFT_TO_BUKKIT, minecraftClazz);
        } catch (NoSuchMethodException e) {
            fail(String.format("""
                    The class %s does not have a public static method to convert a minecraft value to a bukkit value.

                    Following method should be add which, returns the bukkit value based on the minecraft value.
                    %s
                    """, craftClazz, buildMinecraftToBukkitMethod(clazz, minecraftClazz)));
        }

        assertTrue(Modifier.isPublic(method.getModifiers()), String.format("""
                The method %s in class %s is not public.

                The method should be made public, method structure:
                %s
                """, MINECRAFT_TO_BUKKIT, craftClazz, buildMinecraftToBukkitMethod(clazz, minecraftClazz)));

        assertTrue(Modifier.isStatic(method.getModifiers()), String.format("""
                The method %s in class %s is not static.

                The method should be made static, method structure:
                %s
                """, MINECRAFT_TO_BUKKIT, craftClazz, buildMinecraftToBukkitMethod(clazz, minecraftClazz)));

        assertSame(clazz, method.getReturnType(), String.format("""
                The method %s in class %s has the wrong return value.

                The method should have the correct return value, method structure:
                %s
                """, MINECRAFT_TO_BUKKIT, craftClazz, buildMinecraftToBukkitMethod(clazz, minecraftClazz)));

        MINECRAFT_TO_BUKKIT_METHODS.put(clazz, method);
    }

    private String buildMinecraftToBukkitMethod(Class<? extends Keyed> clazz, Class<?> minecraftClazz) {
        return String.format("""
                public static %s minecraftToBukkit(%s minecraft) {
                    [...]
                }
                """, clazz.getSimpleName(), minecraftClazz.getName());
    }

    @Order(2)
    @RegistriesTest
    public void testBukkitToMinecraftPresent(Class<? extends Keyed> clazz, ResourceKey<IRegistry<?>> registryKey,
                                             Class<? extends Keyed> craftClazz, Class<?> minecraftClazz) {
        Method method = null;
        try {
            method = craftClazz.getDeclaredMethod(BUKKIT_TO_MINECRAFT, clazz);
        } catch (NoSuchMethodException e) {
            fail(String.format("""
                    The class %s does not have a public static method to convert a bukkit value to a minecraft value.

                    Following method should be add which, returns the minecraft value based on the bukkit value.
                    %s
                    """, craftClazz, buildBukkitToMinecraftMethod(clazz, minecraftClazz)));
        }

        assertTrue(Modifier.isPublic(method.getModifiers()), String.format("""
                The method %s in class %s is not public.

                The method should be made public, method structure:
                %s
                """, BUKKIT_TO_MINECRAFT, craftClazz, buildBukkitToMinecraftMethod(clazz, minecraftClazz)));

        assertTrue(Modifier.isStatic(method.getModifiers()), String.format("""
                The method %s in class %s is not static.

                The method should be made static, method structure:
                %s
                """, BUKKIT_TO_MINECRAFT, craftClazz, buildBukkitToMinecraftMethod(clazz, minecraftClazz)));

        assertSame(minecraftClazz, method.getReturnType(), String.format("""
                The method %s in class %s has the wrong return value.

                The method should have the correct return value, method structure:
                %s
                """, BUKKIT_TO_MINECRAFT, craftClazz, buildBukkitToMinecraftMethod(clazz, minecraftClazz)));

        BUKKIT_TO_MINECRAFT_METHODS.put(clazz, method);
    }

    private String buildBukkitToMinecraftMethod(Class<? extends Keyed> clazz, Class<?> minecraftClazz) {
        return String.format("""
                public static %s bukkitToMinecraft(%s bukkit) {
                    [...]
                }
                """, minecraftClazz.getName(), clazz.getSimpleName());
    }

    @Order(2)
    @RegistriesTest
    public void testMinecraftToBukkitNullValue(Class<? extends Keyed> clazz) throws IllegalAccessException {
        checkValidMinecraftToBukkit(clazz);

        try {
            Object result = MINECRAFT_TO_BUKKIT_METHODS.get(clazz).invoke(null, (Object) null);
            fail(String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    Got '%s' as return object.
                    """, MINECRAFT_TO_BUKKIT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalArgumentException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    """, MINECRAFT_TO_BUKKIT, clazz.getName()));
        }
    }

    @Order(3)
    @RegistriesTest
    public void testBukkitToMinecraftNullValue(Class<? extends Keyed> clazz) throws IllegalAccessException {
        checkValidBukkitToMinecraft(clazz);

        try {
            Object result = BUKKIT_TO_MINECRAFT_METHODS.get(clazz).invoke(null, (Object) null);
            fail(String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    Got '%s' as return object.
                    """, BUKKIT_TO_MINECRAFT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalArgumentException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept null values and should throw a IllegalArgumentException.
                    """, BUKKIT_TO_MINECRAFT, clazz.getName()));
        }
    }

    @Order(3)
    @RegistriesTest
    public void testMinecraftToBukkit(Class<? extends Keyed> clazz) {
        checkValidMinecraftToBukkit(clazz);
        checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();
        Method method = MINECRAFT_TO_BUKKIT_METHODS.get(clazz);

        RegistryArgumentProvider.getValues(clazz).map(Arguments::get).forEach(arguments -> {
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
                        %s""", MINECRAFT_TO_BUKKIT, clazz.getName(),
                Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    @Order(3)
    @RegistriesTest
    public void testBukkitToMinecraft(Class<? extends Keyed> clazz) {
        checkValidBukkitToMinecraft(clazz);
        checkValidHandle(clazz);

        Map<Object, Object> notMatching = new HashMap<>();
        Method method = BUKKIT_TO_MINECRAFT_METHODS.get(clazz);

        RegistryArgumentProvider.getValues(clazz).map(Arguments::get).forEach(arguments -> {
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
                        %s""", BUKKIT_TO_MINECRAFT, clazz.getName(),
                Joiner.on('\n').withKeyValueSeparator(" got: ").join(notMatching)));
    }

    /**
     * Minecraft registry can return a default key / value
     * when the passed minecraft value is not registry in this case, we want it to throw an error.
     */
    @Order(3)
    @RegistriesTest
    public void testMinecraftToBukkitNoValidMinecraft(Class<? extends Keyed> clazz, ResourceKey<IRegistry<?>> registryKey,
                                                      Class<? extends Keyed> craftClazz, Class<?> minecraftClazz) throws IllegalAccessException {
        checkValidMinecraftToBukkit(clazz);

        try {

            Object minecraft = mock(minecraftClazz);
            Object result = MINECRAFT_TO_BUKKIT_METHODS.get(clazz).invoke(null, minecraft);
            fail(String.format("""
                    Method %s in class %s should not accept a none registered value and should throw a IllegalStateException.
                    Got '%s' as return object.
                    """, MINECRAFT_TO_BUKKIT, clazz.getName(), result));
        } catch (InvocationTargetException e) {
            // #invoke wraps the error in a InvocationTargetException, so we need to check it this way
            assertSame(IllegalStateException.class, e.getCause().getClass(), String.format("""
                    Method %s in class %s should not accept a none registered value and should throw a IllegalStateException.
                    """, MINECRAFT_TO_BUKKIT, clazz.getName()));
        }
    }

    private void checkValidBukkitToMinecraft(Class<? extends Keyed> clazz) {
        assumeTrue(BUKKIT_TO_MINECRAFT_METHODS.containsKey(clazz), String.format("""
                Cannot test class %s, because it does not have a valid %s method.

                Check test results of testBukkitToMinecraftPresent for more information.
                """, clazz.getName(), BUKKIT_TO_MINECRAFT));
    }

    private void checkValidMinecraftToBukkit(Class<? extends Keyed> clazz) {
        assumeTrue(MINECRAFT_TO_BUKKIT_METHODS.containsKey(clazz), String.format("""
                Cannot test class %s, because it does not have a valid %s method.

                Check test results of testMinecraftToBukkitPresent for more information.
                """, clazz.getName(), MINECRAFT_TO_BUKKIT));
    }

    private void checkValidHandle(Class<? extends Keyed> clazz) {
        assumeTrue(IMPLEMENT_HANDLE_ABLE.contains(clazz), String.format("""
                Cannot test class %s, because it does not implement Handleable.

                Check test results of testHandleableImplementation for more information.
                """, clazz.getName()));
    }
}
