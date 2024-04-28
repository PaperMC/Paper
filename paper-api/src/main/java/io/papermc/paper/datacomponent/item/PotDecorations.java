package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the item types for the decorations on a flower pot.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#POT_DECORATIONS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PotDecorations {

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static PotDecorations potDecorations(final @Nullable ItemType back, final @Nullable ItemType left, final @Nullable ItemType right, final @Nullable ItemType front) {
        return potDecorations().back(back).left(left).right(right).front(front).build();
    }

    @Contract(value = "-> new", pure = true)
    static PotDecorations.Builder potDecorations() {
        return ItemComponentTypesBridge.bridge().potDecorations();
    }

    /**
     * Get the item type for the back.
     *
     * @return the back item type.
     */
    @Contract(pure = true)
    @Nullable ItemType back();

    /**
     * Get the item type for the left.
     *
     * @return the left item type.
     */
    @Contract(pure = true)
    @Nullable ItemType left();

    /**
     * Get the item type for the right.
     *
     * @return the right item type.
     */
    @Contract(pure = true)
    @Nullable ItemType right();

    /**
     * Get the item type for the front.
     *
     * @return the front item type.
     */
    @Contract(pure = true)
    @Nullable ItemType front();

    /**
     * Builder for {@link PotDecorations}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<PotDecorations> {

        /**
         * Set the {@link ItemType} for the back.
         *
         * @param back item for the back
         * @return the builder for chaining
         * @see #back()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder back(@Nullable ItemType back);

        /**
         * Set the {@link ItemType} for the left.
         *
         * @param left item for the left
         * @return the builder for chaining
         * @see #left()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder left(@Nullable ItemType left);

        /**
         * Set the {@link ItemType} for the right.
         *
         * @param right item for the right
         * @return the builder for chaining
         * @see #right()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder right(@Nullable ItemType right);

        /**
         * Set the {@link ItemType} for the front.
         *
         * @param front item for the front
         * @return the builder for chaining
         * @see #front()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder front(@Nullable ItemType front);
    }
}
