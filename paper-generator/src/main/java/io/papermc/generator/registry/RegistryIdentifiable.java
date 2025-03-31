package io.papermc.generator.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RegistryIdentifiable<T> {

    ResourceKey<? extends Registry<T>> getRegistryKey();
}
