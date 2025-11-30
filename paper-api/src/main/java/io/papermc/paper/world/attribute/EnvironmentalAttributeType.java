package io.papermc.paper.world.attribute;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnvironmentalAttributeType<T> extends Keyed {

    T getDefaultValue();

}
