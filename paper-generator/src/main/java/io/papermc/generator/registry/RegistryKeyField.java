package io.papermc.generator.registry;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegistryKeyField<T>(Class<T> elementClass, String name) {
}
