package io.papermc.paper.world.attribute;

import io.papermc.paper.math.Position;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnvironmentalAttribute<T> {

    T getGlobal();

    T getPositioned(Position position);

    default T getTimed(long time) {
        return this.getValue(context -> context.time(time));
    }

    default T getValue(UnaryOperator<EnvironmentalAttributeContext.Builder> context) {
        return this.getValue(context.apply(EnvironmentalAttributeContext.builder()).build());
    }

    T getValue(EnvironmentalAttributeContext context);

}
