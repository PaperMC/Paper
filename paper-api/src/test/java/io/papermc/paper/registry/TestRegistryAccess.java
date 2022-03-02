package io.papermc.paper.registry;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestRegistryAccess implements RegistryAccess {

    @Override
    @Deprecated(since = "1.20.6", forRemoval = true)
    public @Nullable <T extends Keyed> Registry<T> getRegistry(final @NotNull Class<T> type) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public @NotNull <T extends Keyed> Registry<T> getRegistry(final @NotNull RegistryKey<T> registryKey) {
        throw new UnsupportedOperationException("Not supported");
    }
}
