package io.papermc.generator.registry;

import io.papermc.generator.resources.data.RegistryData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegistryIntern<T>(ResourceKey<? extends Registry<T>> registryKey, RegistryKeyField<T> registryKeyField, Class<?> holderElementsClass) implements RegistryIdentifiable<T> {
    public RegistryIntern(ResourceKey<? extends Registry<T>> registryKey, Class<?> holderElementsClass) {
        this(registryKey, (RegistryKeyField<T>) RegistryEntries.REGISTRY_KEY_FIELDS.get(registryKey), holderElementsClass);
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    public RegistryEntry<?> bind(RegistryData data) {
        return new RegistryEntry<>(this.registryKey, this.registryKeyField, this.holderElementsClass, data);
    }
}
