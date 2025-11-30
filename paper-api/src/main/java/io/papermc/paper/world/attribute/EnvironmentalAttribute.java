package io.papermc.paper.world.attribute;

import io.papermc.paper.math.Position;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnvironmentalAttribute<T> {

    T getGlobal();

    default T getPositioned(Position position) {
        return this.getValue(EnvironmentalAttributeContext.builder().position(position).build());
    }

    default T getTimed(long time) {
        return this.getValue(EnvironmentalAttributeContext.builder().time(time).build());
    }

    T getValue(EnvironmentalAttributeContext context);

}
