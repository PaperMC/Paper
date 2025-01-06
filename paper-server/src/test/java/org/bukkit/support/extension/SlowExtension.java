package org.bukkit.support.extension;

import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.DummyServerHelper;
import org.bukkit.support.RegistryHelper;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SlowExtension extends BaseExtension {

    public SlowExtension() {
        super("Slow");
    }

    @Override
    public void init(ExtensionContext extensionContext) {
        RegistryHelper.setup(FeatureFlags.VANILLA_SET);

        Server server = DummyServerHelper.setup();

        Bukkit.setServer(server);

        // Paper - Add RegistryAccess for managing registries - replaced with registry access

        CraftRegistry.setMinecraftRegistry(RegistryHelper.getRegistry());
    }

    @Override
    void runBeforeEach(ExtensionContext extensionContext) {
    }
}
