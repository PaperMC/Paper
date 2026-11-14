package io.papermc.paper.loot.number;

import org.bukkit.loot.LootContext;

/**
 * Represents a floating point number that can be resolved against a {@link LootContext}.
 */
public interface ResolvableFloat {

    /**
     * Resolves this floating point number against the given loot context.
     *
     * @param context      the loot context to resolve against.
     * @param defaultValue the default value
     * @return the resolved number
     */
    float resolve(LootContext context, float defaultValue);

    /**
     * Represents a constant {@link ResolvableFloat}.
     */
    interface Constant extends ResolvableFloat {

        /**
         * @return the constant value
         */
        float getValue();

        /**
         * {@inheritDoc}
         */
        @Override
        default float resolve(LootContext context, float defaultValue) {
            return this.getValue();
        }
    }
}
