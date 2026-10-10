package org.bukkit.support.extension;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.Field;
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
import org.bukkit.event.Event;
import org.bukkit.support.DummyServerHelper;
import org.bukkit.support.RegistryHelper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

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
        try {
            final Field empty = Event.class.getDeclaredField("EMPTY");
            empty.trySetAccessible();
            final Field callEvent = Event.class.getDeclaredField("CALL_EVENT");
            callEvent.trySetAccessible();
            final MutableCallSite m = (MutableCallSite) callEvent.get(null);
            m.setTarget((MethodHandle) empty.get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Keyed> Registry<T> createMockBukkitRegistry(Class<T> keyed) {
        Map<NamespacedKey, T> mocks = new HashMap<>();
        Registry<T> registry = mock(withSettings().stubOnly().defaultAnswer(NormalExtension.DEFAULT_ANSWER));

        doAnswer(invocation ->
                mocks.computeIfAbsent(invocation.getArgument(0), k -> mock(RegistryHelper.getFieldType(keyed, invocation.getArgument(0)), withSettings().stubOnly().defaultAnswer(DEFAULT_ANSWER)))
        ).when(registry).get((NamespacedKey) any()); // Allow static classes to fill their fields, so that it does not error out, just by loading them // Paper - registry modification api - specifically call namespaced key overload

        return registry;
    }
}
