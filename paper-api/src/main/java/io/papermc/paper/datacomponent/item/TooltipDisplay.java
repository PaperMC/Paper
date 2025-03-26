package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface TooltipDisplay {

    /**
     * Returns a new builder for creating a TooltipDisplay.
     *
     * @return a builder
     */
    @Contract(value = "-> new", pure = true)
    static Builder tooltipDisplay() {
        return ItemComponentTypesBridge.bridge().tooltipDisplay();
    }

    boolean hideTooltip();

    Set<DataComponentType> hiddenComponents();

    /**
     * Builder for {@link TooltipDisplay}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<TooltipDisplay> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder hideTooltip(boolean hide);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addHiddenComponents(DataComponentType... components);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hiddenComponents(Set<DataComponentType> components);
    }
}
