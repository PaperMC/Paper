package io.papermc.paper.configuration.serializer.registry;

import io.leangen.geantyref.TypeToken;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.configurate.serialize.SerializationException;

/**
 * Use {@link RegistryHolderSerializer} for datapack-configurable things.
 */
public final class RegistryValueSerializer<T> extends RegistryEntrySerializer<T, T> {

    public RegistryValueSerializer(
        final TypeToken<T> type,
        final RegistryAccess registryAccess,
        final ResourceKey<? extends Registry<T>> registryKey,
        final boolean omitMinecraftNamespace
    ) {
        super(type, registryAccess, registryKey, omitMinecraftNamespace);
    }

    public RegistryValueSerializer(
        final Class<T> type,
        final RegistryAccess registryAccess,
        final ResourceKey<? extends Registry<T>> registryKey,
        final boolean omitMinecraftNamespace
    ) {
        super(type, registryAccess, registryKey, omitMinecraftNamespace);
    }

    @Override
    protected T convertFromResourceKey(final ResourceKey<T> key) throws SerializationException {
        final T value = this.registry().getValue(key);
        if (value == null) {
            throw new SerializationException("Missing value in " + this.registry() + " with key " + key.location());
        }
        return value;
    }

    @Override
    protected ResourceKey<T> convertToResourceKey(final T value) {
        return this.registry().getResourceKey(value).orElseThrow();
    }
}
