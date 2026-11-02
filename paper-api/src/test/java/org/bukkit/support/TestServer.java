package org.bukkit.support;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.testing.EmptyTag;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.jspecify.annotations.NullMarked;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@NullMarked
public final class TestServer {

    static {
        final Server instance = mock(withSettings().stubOnly());

        final Thread creatingThread = Thread.currentThread();
        when(instance.isPrimaryThread()).then(_ -> Thread.currentThread().equals(creatingThread));

        // Paper - remove plugin manager for Paper Plugins

        final Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());
        when(instance.getLogger()).thenReturn(logger);

        when(instance.getName()).thenReturn(TestServer.class.getSimpleName());

        when(instance.getVersion()).thenReturn("Version_" + TestServer.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn("BukkitVersion_" + TestServer.class.getPackage().getImplementationVersion());

        // Paper start - RegistryAccess
        when(instance.getRegistry(any())).then(invocationOnMock -> RegistryAccess.registryAccess().getRegistry(((Class<Keyed>) invocationOnMock.getArgument(0))));
        // Paper end - RegistryAccess

        final UnsafeValues unsafeValues = mock(withSettings().stubOnly());
        when(instance.getUnsafe()).thenReturn(unsafeValues);

        // Paper start - testing changes
        when(instance.getTag(anyString(), any(NamespacedKey.class), any())).thenAnswer(ignored -> new EmptyTag());
        when(instance.getScoreboardCriteria(anyString())).thenReturn(null);
        // Paper end - testing changes

        Bukkit.setServer(instance);
    }

    private TestServer() {
    }

    public static void setup() {
    }
}
