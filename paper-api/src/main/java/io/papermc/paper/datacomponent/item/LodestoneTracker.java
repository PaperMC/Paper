package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * If present, specifies the target Lodestone that a Compass should point towards.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#LODESTONE_TRACKER
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface LodestoneTracker {

    @Contract(value = "_, _ -> new", pure = true)
    static LodestoneTracker lodestoneTracker(final @Nullable Location location, final boolean tracked) {
        return lodestoneTracker().location(location).tracked(tracked).build();
    }

    @Contract(value = "-> new", pure = true)
    static LodestoneTracker.Builder lodestoneTracker() {
        return ItemComponentTypesBridge.bridge().lodestoneTracker();
    }

    /**
     * The location that the compass should point towards.
     *
     * @return location
     */
    @Contract(value = "-> new", pure = true)
    @Nullable Location location();

    /**
     * If {@code true}, when the Lodestone at the target position is removed, the component will be removed.
     *
     * @return tracked
     */
    @Contract(pure = true)
    boolean tracked();

    /**
     * Builder for {@link LodestoneTracker}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<LodestoneTracker> {

        /**
         * Sets the location to point towards for this builder.
         *
         * @param location location to point towards
         * @return the builder for chaining
         * @see #location()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder location(@Nullable Location location);

        /**
         * Sets if this location lodestone is tracked for this builder.
         *
         * @param tracked is tracked
         * @return the builder for chaining
         * @see #tracked()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder tracked(boolean tracked);
    }
}
