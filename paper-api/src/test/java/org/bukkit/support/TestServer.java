package org.bukkit.support;

import static org.mockito.Mockito.*;
import com.google.common.base.Preconditions;
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

        // Paper - remove plugin manager for Paper Plugins

        Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());
        when(instance.getLogger()).thenReturn(logger);

        when(instance.getName()).thenReturn(TestServer.class.getSimpleName());

        when(instance.getVersion()).thenReturn("Version_" + TestServer.class.getPackage().getImplementationVersion());

        when(instance.getBukkitVersion()).thenReturn("BukkitVersion_" + TestServer.class.getPackage().getImplementationVersion());

        // Paper start - RegistryAccess
        when(instance.getRegistry(any())).then(invocationOnMock -> {
            return io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(((Class<Keyed>)invocationOnMock.getArgument(0)));
        });
        // Paper end - RegistryAccess

        UnsafeValues unsafeValues = mock(withSettings().stubOnly());
        when(instance.getUnsafe()).thenReturn(unsafeValues);

        // Paper start - testing changes
        when(instance.getTag(anyString(), any(NamespacedKey.class), any())).thenAnswer(ignored -> new io.papermc.paper.testing.EmptyTag());
        when(instance.getScoreboardCriteria(anyString())).thenReturn(null);
        // Paper end - testing changes

        Bukkit.setServer(instance);
    }

    private TestServer() {
    }

    public static void setup() {
    }
}
