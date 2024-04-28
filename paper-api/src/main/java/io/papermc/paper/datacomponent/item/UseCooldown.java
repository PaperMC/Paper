package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the contents of cooldown information when an item is used.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#USE_COOLDOWN
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface UseCooldown {

    /**
     * Creates a new builder for use cooldown.
     *
     * @param seconds the duration in seconds; must be positive
     * @return builder
     */
    @Contract(value = "_ -> new", pure = true)
    static UseCooldown.Builder useCooldown(final float seconds) {
        return ItemComponentTypesBridge.bridge().useCooldown(seconds);
    }

    /**
     * The amount of seconds the cooldown will be active for.
     *
     * @return cooldown seconds
     */
    @Contract(pure = true)
    float seconds();

    /**
     * The unique resource location to identify this cooldown group.
     * <p>
     * This allows items to share cooldowns with other items in the same cooldown group, if present.
     *
     * @return cooldown group, or null if not present
     */
    @Contract(pure = true)
    @Nullable Key cooldownGroup();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<UseCooldown> {

        /**
         * Sets a unique resource location for this cooldown group.
         * <p>
         * This allows items to share cooldowns with other items in the same cooldown group.
         * </p>
         *
         * @param key the unique resource location; can be null
         * @return the builder for chaining
         * @see #cooldownGroup()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder cooldownGroup(@Nullable Key key);
    }
}
