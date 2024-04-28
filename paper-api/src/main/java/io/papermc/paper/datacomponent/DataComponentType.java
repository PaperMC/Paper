package io.papermc.paper.datacomponent;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DataComponentType extends Keyed {

    /**
     * Checks if this data component type is persistent, or
     * that it will be saved with any itemstack it's attached to.
     *
     * @return {@code true} if persistent, {@code false} otherwise
     */
    boolean isPersistent();

    @SuppressWarnings("unused")
    @ApiStatus.NonExtendable
    interface Valued<T> extends DataComponentType {

    }

    @ApiStatus.NonExtendable
    interface NonValued extends DataComponentType {

    }
}
