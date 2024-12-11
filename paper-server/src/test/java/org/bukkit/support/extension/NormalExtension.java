package org.bukkit.support.extension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.support.DummyServerHelper;
import org.bukkit.support.RegistryHelper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.stubbing.Answer;

public class NormalExtension extends BaseExtension {

    private static final Answer<?> DEFAULT_ANSWER = invocation -> {
        throw new UnsupportedOperationException("""
                Cannot use registry operation during normal testing.
                Either change the code so that it does no longer need registry for testing,
                or use another testing environment, such as @VanillaFeature or @AllFeatures.
                """);
    };

    private static final Map<Class<? extends Keyed>, Registry<?>> registries = new HashMap<>();

    public NormalExtension() {
        super("Normal");
    }

    @Override
    public void init(ExtensionContext extensionContext) {
        RegistryHelper.setup(FeatureFlags.VANILLA_SET);

        Server server = DummyServerHelper.setup();

        Bukkit.setServer(server);

        when(server.getRegistry(any()))
                .then(invocation -> {
                    Class<? extends Keyed> keyed = invocation.getArgument(0);
                    return registries.computeIfAbsent(keyed, k -> createMockBukkitRegistry(keyed));
                });


        RegistryAccess registry = mock(withSettings().stubOnly().defaultAnswer(NormalExtension.DEFAULT_ANSWER));
        CraftRegistry.setMinecraftRegistry(registry);
    }

    @Override
    void runBeforeEach(ExtensionContext extensionContext) {
    }

    private <T extends Keyed> Registry<T> createMockBukkitRegistry(Class<T> keyed) {
        Map<NamespacedKey, T> mocks = new HashMap<>();
        Registry<T> registry = mock(withSettings().stubOnly().defaultAnswer(NormalExtension.DEFAULT_ANSWER));

        doAnswer(invocation ->
                mocks.computeIfAbsent(invocation.getArgument(0), k -> mock(RegistryHelper.updateClass(keyed, invocation.getArgument(0)), withSettings().stubOnly().defaultAnswer(DEFAULT_ANSWER)))
        ).when(registry).get((NamespacedKey) any()); // Allow static classes to fill there fields, so that it does not error out, just by loading them // Paper - registry modification api - specifically call namespaced key overload

        return registry;
    }
}
