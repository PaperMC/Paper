package org.bukkit.registry;

import com.google.common.base.Joiner;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Registry;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.provider.RegistriesArgumentProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests, if all default registries present in {@link Registry} are added to {@link RegistriesArgumentProvider}
 */
@AllFeatures
public class RegistryArgumentAddedTest {

    @Test
    public void testPresent() throws ClassNotFoundException {
        // Make sure every registry is created
        Class.forName(Registry.class.getName());

        // Paper start
        Set<io.papermc.paper.registry.RegistryKey<?>> loadedRegistries = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        loadedRegistries.addAll(io.papermc.paper.registry.PaperRegistryAccess.instance().getLoadedServerBackedRegistries());
        // Paper end
        Set<io.papermc.paper.registry.RegistryKey<?>> notFound = new HashSet<>(); // Paper

        RegistriesArgumentProvider
                .getData()
                .map(RegistriesArgumentProvider.RegistryArgument::apiRegistryKey)
                .forEach(key -> {
                    if (!loadedRegistries.remove(key)) {
                        notFound.add(key);
                    }
                });

        loadedRegistries.remove(io.papermc.paper.registry.RegistryKey.TRIGGER_TYPE); // remove because it's only partially implemented (only supports adding custom ones for now)
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
