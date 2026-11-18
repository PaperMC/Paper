package io.papermc.generator.registry;

public record RegistryKeyField<T>(Class<T> elementClass, String name) {
}
