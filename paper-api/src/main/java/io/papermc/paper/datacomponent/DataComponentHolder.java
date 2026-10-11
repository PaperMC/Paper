package io.papermc.paper.datacomponent;

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
public interface DataComponentHolder extends DataComponentView, DataComponentMutable {
}
