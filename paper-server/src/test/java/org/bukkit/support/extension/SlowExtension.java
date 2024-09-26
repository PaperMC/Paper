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

public class SlowExtension extends BaseExtension {

    private static final Map<Class<? extends Keyed>, Registry<?>> registries = new HashMap<>();

    public SlowExtension() {
        super("Slow");
    }

    @Override
    public void init(ExtensionContext extensionContext) {
        RegistryHelper.setup(FeatureFlags.VANILLA_SET);

        Server server = DummyServerHelper.setup();

        Bukkit.setServer(server);

        when(server.getRegistry(any()))
                .then(invocation -> {
                    Class<? extends Keyed> keyed = invocation.getArgument(0);
                    return registries.computeIfAbsent(keyed, k -> CraftRegistry.createRegistry(keyed, RegistryHelper.getRegistry()));
                });

        CraftRegistry.setMinecraftRegistry(RegistryHelper.getRegistry());
    }

    @Override
    void runBeforeEach(ExtensionContext extensionContext) {
    }
}
