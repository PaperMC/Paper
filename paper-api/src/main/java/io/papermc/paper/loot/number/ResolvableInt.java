package io.papermc.paper.loot.number;

import org.bukkit.loot.LootContext;

/**
 * Represents an integer number that can be resolved against a {@link LootContext}.
 */
public interface ResolvableInt {

    /**
     * Resolves this integer number against the given loot context.
     *
     * @param context      the loot context to resolve against.
     * @param defaultValue the default value
     * @return the resolved number
     */
    int resolve(LootContext context, int defaultValue);

    /**
     * Represents a constant {@link ResolvableInt}.
     */
    interface Constant extends ResolvableInt {

        /**
         * @return the constant value
         */
        int getValue();

        /**
         * {@inheritDoc}
         */
        @Override
        default int resolve(LootContext context, int defaultValue) {
            return this.getValue();
        }
    }
}
