package io.papermc.paper.entity.ai;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

public interface MemoryKey extends Keyed { // todo migrate on top of existing api?

    @SuppressWarnings("unused")
    @ApiStatus.NonExtendable
    interface Valued<T> extends MemoryKey {

    }

    @ApiStatus.NonExtendable
    interface NonValued extends MemoryKey {

    }
}
