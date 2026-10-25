package io.papermc.paper.configuration.transformation;

import io.papermc.paper.configuration.Configurations.ContextKey;
import io.papermc.paper.configuration.Configurations.ContextMap;
import java.util.Set;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

/**
 * A versioned transformation that declares the context it needs instead of reaching for global state,
 * so it can be left out of loads where that context is not available yet.
 */
public interface ContextualTransformation {

    int version();

    /**
     * Keys that must be present in the {@link ContextMap} passed to {@link #create(ContextMap)}.
     *
     * @return required context keys
     */
    default Set<ContextKey<?>> requiredContext() {
        return Set.of();
    }

    /**
     * Creates the transformation.
     *
     * @param context context containing at least every key in {@link #requiredContext()}
     * @return the transformation
     */
    ConfigurationTransformation create(ContextMap context);
}
