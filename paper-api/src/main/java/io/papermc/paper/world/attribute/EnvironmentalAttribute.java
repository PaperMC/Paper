package io.papermc.paper.world.attribute;

import io.papermc.paper.math.Position;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnvironmentalAttribute<T> {

    T getGlobal();

    T getPositioned(Position position);

    default T getTimed(long time) {
        return this.getValue(EnvironmentalAttributeContext.builder().time(time).build());
    }

    T getValue(EnvironmentalAttributeContext context);

}
