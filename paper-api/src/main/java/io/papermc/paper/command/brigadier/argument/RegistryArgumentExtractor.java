package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;

/**
 * Utilities for extracting registry-related arguments from a {@link CommandContext}.
 */
public final class RegistryArgumentExtractor {

    /**
     * Gets a typed key argument from a command context.
     *
     * @param context the command context
     * @param registryKey the registry key for the typed key
     * @param name the argument name
     * @return the typed key argument
     * @param <T> the value type
     * @param <S> the sender type
     * @throws IllegalArgumentException if the registry key doesn't match the typed key
     */
    @SuppressWarnings("unchecked")
    public static <T, S> TypedKey<T> getTypedKey(final CommandContext<S> context, final RegistryKey<T> registryKey, final String name) {
        final TypedKey<T> typedKey = context.getArgument(name, TypedKey.class);
        if (typedKey.registryKey().equals(registryKey)) {
            return typedKey;
        }
        throw new IllegalArgumentException(registryKey + " is not the correct registry for " + typedKey);
    }

    private RegistryArgumentExtractor() {
    }
}
