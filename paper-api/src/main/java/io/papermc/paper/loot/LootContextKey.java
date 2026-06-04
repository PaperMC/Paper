package io.papermc.paper.loot;

import net.kyori.adventure.key.Keyed;

/**
 * A key to a possible value in a {@link org.bukkit.loot.LootContext}.
 */
@SuppressWarnings("unused")
public interface LootContextKey extends Keyed {

    /**
     * @param <T> the value type
     */
    interface Valued<T> extends LootContextKey {
    }

    interface NonValued extends LootContextKey {
    }
}
