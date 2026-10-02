package io.papermc.paper.adventure.providers;

import net.kyori.adventure.internal.properties.AdventureProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // Permitted provider.
public class AdventurePropertiesDefaultOverrideProviderImpl implements AdventureProperties.DefaultOverrideProvider {
    @SuppressWarnings("unchecked") // We do check.
    @Override
    public <T> @Nullable T overrideDefault(final AdventureProperties.@NotNull Property<T> property, @Nullable final T existingDefault) {
        if (property == AdventureProperties.DEFAULT_FLATTENER_NESTING_LIMIT) {
            // Large nesting limits can cause stack overflows with overly complicated components.
            // Limiting to 30 is a reasonable default.
            return (T) Integer.valueOf(30);
        } else {
            return null;
        }
    }
}
