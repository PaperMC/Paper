package io.papermc.paper.registry;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class TestRegistryAccess implements RegistryAccess {

    @Override
    @Deprecated(since = "1.20.6", forRemoval = true)
    public <T extends Keyed> @Nullable Registry<T> getRegistry(final Class<T> type) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public <T extends Keyed> Registry<T> getRegistry(final RegistryKey<T> registryKey) {
        throw new UnsupportedOperationException("Not supported");
    }
}
