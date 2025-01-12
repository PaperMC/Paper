package io.papermc.paper.configuration;

import net.minecraft.core.RegistryAccess;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class GlobalConfigTestingBase {

    public static void setupGlobalConfigForTest(RegistryAccess registryAccess) {
        //noinspection ConstantConditions
        if (GlobalConfiguration.get() == null) {
            ConfigurationNode node = PaperConfigurations.createForTesting(registryAccess);
            try {
                GlobalConfiguration globalConfiguration = node.require(GlobalConfiguration.class);
                GlobalConfiguration.set(globalConfiguration);
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
