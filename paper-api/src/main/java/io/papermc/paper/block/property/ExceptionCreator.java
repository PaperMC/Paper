package io.papermc.paper.block.property;

import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@FunctionalInterface
interface ExceptionCreator {

    ExceptionCreator INSTANCE = (value, type, property) -> new IllegalArgumentException(String.format("%s (%s) is not a valid %s for %s", value, value.getClass().getSimpleName(), type.name().toLowerCase(Locale.ENGLISH), property));

    IllegalArgumentException create(Object value, Type type, BlockProperty<?> property);

    enum Type {
        NAME,
        VALUE,
    }
}
