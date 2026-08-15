package io.papermc.paper.block.property;

import java.util.Locale;

@FunctionalInterface
@SuppressWarnings("checkstyle:JavadocVariable") // todo see https://github.com/checkstyle/checkstyle/issues/17117
interface ExceptionCreator {

    ExceptionCreator NOT_VALID = (value, type, property) -> new IllegalArgumentException(String.format("%s (%s) is not a valid %s for %s", value, value.getClass().getSimpleName(), type.name().toLowerCase(Locale.ENGLISH), property));

    IllegalArgumentException create(Object value, Type type, BlockProperty<?> property);

    enum Type {
        NAME,
        VALUE,
    }
}
