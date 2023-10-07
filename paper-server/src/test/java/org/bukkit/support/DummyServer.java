package org.bukkit.support;

import static org.mockito.Mockito.*;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Versioning;

public final class DummyServer {

    static {
        try {
            Server instance = mock(withSettings().stubOnly());

            when(instance.getItemFactory()).thenAnswer(mock -> CraftItemFactory.instance());

            when(instance.getName()).thenReturn(DummyServer.class.getName());

            when(instance.getVersion()).thenReturn(DummyServer.class.getPackage().getImplementationVersion());

            when(instance.getBukkitVersion()).thenReturn(Versioning.getBukkitVersion());

            when(instance.getLogger()).thenReturn(Logger.getLogger(DummyServer.class.getCanonicalName()));

            when(instance.getUnsafe()).then(mock -> CraftMagicNumbers.INSTANCE);

            when(instance.createBlockData(any(Material.class))).then(mock -> CraftBlockData.newData(mock.getArgument(0), null));

            when(instance.getLootTable(any())).then(mock -> new CraftLootTable(mock.getArgument(0),
                    AbstractTestingBase.DATA_PACK.getLootData().getLootTable(CraftNamespacedKey.toMinecraft(mock.getArgument(0)))));

            when(instance.getRegistry(any())).then(mock -> CraftRegistry.createRegistry(mock.getArgument(0), AbstractTestingBase.REGISTRY_CUSTOM));

            Bukkit.setServer(instance);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void setup() {}

    private DummyServer() {};
}
