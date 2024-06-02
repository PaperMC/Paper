package org.bukkit.support;

import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

public final class TestServer {

    static {
        Server instance = mock(withSettings().stubOnly());

        Thread creatingThread = Thread.currentThread();
        when(instance.isPrimaryThread()).then(mock -> Thread.currentThread().equals(creatingThread));

        PluginManager pluginManager = new SimplePluginManager(instance, new SimpleCommandMap(instance));
        when(instance.getPluginManager()).thenReturn(pluginManager);

        Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());
        when(instance.getLogger()).thenReturn(logger);

        when(instance.getName()).thenReturn(TestServer.class.getSimpleName());

        when(instance.getVersion()).thenReturn("Version_" + TestServer.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn("BukkitVersion_" + TestServer.class.getPackage().getImplementationVersion());

        Map<Class<? extends Keyed>, Registry<?>> registers = new HashMap<>();
        when(instance.getRegistry(any())).then(invocationOnMock -> registers.computeIfAbsent(invocationOnMock.getArgument(0), aClass -> new Registry<>() {
            private final Map<NamespacedKey, Keyed> cache = new HashMap<>();

            @Override
            public Keyed get(NamespacedKey key) {
                Class<? extends Keyed> theClass;
                // Some registries have extra Typed classes such as BlockType and ItemType.
                // To avoid class cast exceptions during init mock the Typed class.
                // To get the correct class, we just use the field type.
                try {
                    theClass = (Class<? extends Keyed>) aClass.getField(key.getKey().toUpperCase(Locale.ROOT).replace('.', '_')).getType();
                } catch (ClassCastException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }

                return cache.computeIfAbsent(key, key2 -> mock(theClass, withSettings().stubOnly()));
            }

            @NotNull
            @Override
            public Stream<Keyed> stream() {
                throw new UnsupportedOperationException("Not supported");
            }

            @Override
            public Iterator<Keyed> iterator() {
                throw new UnsupportedOperationException("Not supported");
            }
        }));

        UnsafeValues unsafeValues = mock(withSettings().stubOnly());
        when(instance.getUnsafe()).thenReturn(unsafeValues);

        Bukkit.setServer(instance);
    }

    private TestServer() {
    }

    public static void setup() {
    }
}
