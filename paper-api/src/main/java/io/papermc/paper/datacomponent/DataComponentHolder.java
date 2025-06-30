package io.papermc.paper.datacomponent;

import org.bukkit.Utility;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * This represents an object capable of holding and mutating data components.
 *
 * @see PersistentDataContainer
 */
@NullMarked
@ApiStatus.NonExtendable
public interface DataComponentHolder extends DataComponentView {

    /**
     * Sets the value of the data component type for this holder.
     *
     * @param type the data component type
     * @param valueBuilder value builder
     * @param <T> value type
     */
    @Utility
    @ApiStatus.Experimental
    <T> void setData(final DataComponentType.Valued<T> type, final DataComponentBuilder<T> valueBuilder);

    /**
     * Sets the value of the data component type for this holder.
     *
     * @param type the data component type
     * @param value value to set
     * @param <T> value type
     */
    @ApiStatus.Experimental
    <T> void setData(final DataComponentType.Valued<T> type, final T value);

    /**
     * Marks this non-valued data component type as present in this itemstack.
     *
     * @param type the data component type
     */
    @ApiStatus.Experimental
    void setData(final DataComponentType.NonValued type);

   // TODO: Do we even want to have the concept of overriding here? Not sure what is going on with entity components
}
