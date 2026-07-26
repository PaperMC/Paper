package io.papermc.paper.datacomponent.item;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Controls which blocks a player in Adventure mode can do a certain action with this item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CAN_BREAK
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CAN_PLACE_ON
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemAdventurePredicate {

    @Contract(value = "_ -> new", pure = true)
    static ItemAdventurePredicate itemAdventurePredicate(final List<BlockPredicate> predicates) {
        return itemAdventurePredicate().addPredicates(predicates).build();
    }

    @Contract(value = "-> new", pure = true)
    static ItemAdventurePredicate.Builder itemAdventurePredicate() {
        return ItemComponentTypesBridge.bridge().itemAdventurePredicate();
    }

    /**
     * List of block predicates that control if the action is allowed.
     *
     * @return predicates
     */
    @Contract(pure = true)
    @Unmodifiable List<BlockPredicate> predicates();

    /**
     * Builder for {@link ItemAdventurePredicate}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemAdventurePredicate> {
        /**
         * Adds a block predicate to this builder.
         *
         * @param predicate predicate
         * @return the builder for chaining
         * @see #predicates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPredicate(BlockPredicate predicate);

        /**
         * Adds block predicates to this builder.
         *
         * @param predicates predicates
         * @return the builder for chaining
         * @see #predicates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPredicates(List<BlockPredicate> predicates);
    }
}
