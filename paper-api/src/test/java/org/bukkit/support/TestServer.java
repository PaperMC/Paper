package org.bukkit.support;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public final class TestServer {

    static {
        Server instance = mock(withSettings().stubOnly());

        Thread creatingThread = Thread.currentThread();
        when(instance.isPrimaryThread()).then(mock -> Thread.currentThread().equals(creatingThread));

        Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());
        when(instance.getLogger()).thenReturn(logger);

        when(instance.getName()).thenReturn(TestServer.class.getSimpleName());

        when(instance.getVersion()).thenReturn("Version_" + TestServer.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn("BukkitVersion_" + TestServer.class.getPackage().getImplementationVersion());

        when(instance.getRegistry(any())).then(invocationOnMock -> {
            return io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(((Class<Keyed>)invocationOnMock.getArgument(0)));
        });

        UnsafeValues unsafeValues = mock(withSettings().stubOnly());
        when(instance.getUnsafe()).thenReturn(unsafeValues);

        when(instance.getTag(anyString(), any(NamespacedKey.class), any())).thenAnswer(ignored -> new io.papermc.paper.testing.EmptyTag());

        Bukkit.setServer(instance);
    }

    private TestServer() {
    }

    public static void setup() {
    }
}
