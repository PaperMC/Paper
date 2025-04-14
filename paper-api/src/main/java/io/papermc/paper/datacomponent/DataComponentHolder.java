package io.papermc.paper.datacomponent;

import org.bukkit.Utility;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
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
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type, final @NotNull io.papermc.paper.datacomponent.DataComponentBuilder<T> valueBuilder);

    /**
     * Sets the value of the data component type for this holder.
     *
     * @param type the data component type
     * @param value value to set
     * @param <T> value type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type, final @NotNull T value);

    /**
     * Marks this non-valued data component type as present in this itemstack.
     *
     * @param type the data component type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull NonValued type);

   // TODO: Do we even want to have the concept of overriding here? Not sure what is going on with entity components
}
