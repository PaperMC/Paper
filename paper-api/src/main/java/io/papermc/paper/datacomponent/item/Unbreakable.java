package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * If set, the item will not lose any durability when used.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#UNBREAKABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Unbreakable extends ShownInTooltip<Unbreakable> {

    @Contract(value = "_ -> new", pure = true)
    static Unbreakable unbreakable(final boolean showInTooltip) {
        return unbreakable().showInTooltip(showInTooltip).build();
    }

    @Contract(value = "-> new", pure = true)
    static Unbreakable.Builder unbreakable() {
        return ItemComponentTypesBridge.bridge().unbreakable();
    }

    /**
     * Builder for {@link Unbreakable}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ShownInTooltip.Builder<Builder>, DataComponentBuilder<Unbreakable> {
    }
}
