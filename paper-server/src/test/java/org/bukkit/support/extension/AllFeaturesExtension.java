package org.bukkit.support.extension;

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

public class AllFeaturesExtension extends BaseExtension {

    private static final Map<Class<? extends Keyed>, Registry<?>> realRegistries = new HashMap<>();

    public AllFeaturesExtension() {
        super("AllFeatures");
    }

    public static <T extends Keyed> Registry<T> getRealRegistry(Class<T> clazz) {
        return (Registry<T>) AllFeaturesExtension.realRegistries.get(clazz);
    }

    public static Map<Class<? extends Keyed>, Registry<?>> getRealRegistries() {
        return AllFeaturesExtension.realRegistries;
    }

    @Override
    public void init(ExtensionContext extensionContext) {
        RegistryHelper.setup(FeatureFlags.REGISTRY.allFlags());

        Server server = DummyServerHelper.setup();

        Bukkit.setServer(server);

        // Paper - Add RegistryAccess for managing registries - replaced with registry access

        CraftRegistry.setMinecraftRegistry(RegistryHelper.getRegistry());
    }

    @Override
    void runBeforeEach(ExtensionContext extensionContext) {
    }
}
