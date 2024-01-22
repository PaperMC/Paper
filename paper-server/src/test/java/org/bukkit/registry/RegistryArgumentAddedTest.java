package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Registry;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.support.DummyServer;
import org.bukkit.support.provider.RegistriesArgumentProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

/**
 * This class tests, if all default registries present in {@link Registry} are added to {@link RegistriesArgumentProvider}
 */
public class RegistryArgumentAddedTest extends AbstractTestingBase {

    @Test
    public void testPresent() throws ClassNotFoundException {
        // Make sure every registry is created
        Class.forName(Registry.class.getName());

        Set<Class<?>> loadedRegistries = new HashSet<>(DummyServer.registers.keySet());
        Set<Class<?>> notFound = new HashSet<>();

        RegistriesArgumentProvider
                .getData()
                .map(Arguments::get)
                .map(array -> array[0])
                .map(clazz -> (Class<?>) clazz)
                .forEach(clazz -> {
                    if (!loadedRegistries.remove(clazz)) {
                        notFound.add(clazz);
                    }
                });

        assertTrue(loadedRegistries.isEmpty(), String.format("""
                There are registries present, which are not registered in RegistriesArgumentProvider.

                Add the following registries to the RegistriesArgumentProvider class, so that they can be tested.
                %s""", Joiner.on('\n').join(loadedRegistries)));

        assertTrue(notFound.isEmpty(), String.format("""
                There are more registries present in RegistriesArgumentProvider then loaded by Registry class.

                Remove the following registries from the RegistriesArgumentProvider class.
                %s""", Joiner.on('\n').join(notFound)));
    }
}
