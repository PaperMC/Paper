package io.papermc.paper.datacomponent;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BuildableDataComponent<C extends BuildableDataComponent<C, B>, B extends DataComponentBuilder<C>> {

    /**
     * Creates a new builder from this data component.
     *
     * @return a new builder
     */
    @Contract(value = "-> new", pure = true)
    B toBuilder();
}
