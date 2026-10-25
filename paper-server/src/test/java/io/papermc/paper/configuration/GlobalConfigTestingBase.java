package io.papermc.paper.configuration;

import net.minecraft.core.RegistryAccess;
import org.spongepowered.configurate.serialize.SerializationException;

public final class GlobalConfigTestingBase {

    public static void setupGlobalConfigForTest(RegistryAccess registryAccess) {
        //noinspection ConstantConditions
        if (GlobalConfiguration.get() == null) {
            try {
                GlobalConfiguration.set(PaperConfigurations.createGlobalForTesting(registryAccess));
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
