package org.bukkit.support.extension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.DummyServerHelper;
import org.bukkit.support.RegistryHelper;
import org.junit.jupiter.api.extension.ExtensionContext;

public class VanillaFeatureExtension extends BaseExtension {

    private static final Map<Class<? extends Keyed>, Registry<?>> registries = new HashMap<>();

    public VanillaFeatureExtension() {
        super("VanillaFeature");
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
