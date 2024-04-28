package io.papermc.paper.datacomponent.item;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * References the shared map state holding map contents and markers for a Filled Map.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#MAP_ID
 */
@NullMarked
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface MapId {

    @Contract(value = "_ -> new", pure = true)
    static MapId mapId(final int id) {
        return ItemComponentTypesBridge.bridge().mapId(id);
    }

    /**
     * The map id.
     *
     * @return id
     */
    @Contract(pure = true)
    int id();
}
