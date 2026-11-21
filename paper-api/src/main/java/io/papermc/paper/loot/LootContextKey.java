package io.papermc.paper.loot;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * A key to a possible value in a {@link org.bukkit.loot.LootContext}.
 */
@SuppressWarnings("unused")
public sealed interface LootContextKey extends Keyed permits LootContextKey.Valued, LootContextKey.NonValued {

    /**
     * A key to a possible value in a {@link org.bukkit.loot.LootContext} that has a value.
     *
     * @param <T> the value type
     */
    @ApiStatus.NonExtendable
    non-sealed interface Valued<T> extends LootContextKey {
    }

    /**
     * A key to a possible value in a {@link org.bukkit.loot.LootContext} that does not have a value.
     */
    @ApiStatus.NonExtendable
    non-sealed interface NonValued extends LootContextKey {
    }
}
