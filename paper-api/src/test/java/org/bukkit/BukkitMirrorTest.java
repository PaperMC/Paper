package org.bukkit;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class BukkitMirrorTest {
    @Test
    public final void test() throws NoSuchMethodException {
        Method[] serverMethods = Server.class.getDeclaredMethods();
        for(Method method : serverMethods) {
            Method mirrorMethod = Bukkit.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            assertTrue("Bukkit." + method.getName() + " must be static!", Modifier.isStatic(mirrorMethod.getModifiers()));
        }
    }
}
