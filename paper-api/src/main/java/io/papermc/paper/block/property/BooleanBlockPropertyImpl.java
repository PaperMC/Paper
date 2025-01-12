package io.papermc.paper.block.property;

import it.unimi.dsi.fastutil.booleans.BooleanSet;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Internal
record BooleanBlockPropertyImpl(String name) implements BooleanBlockProperty {

    private static final BooleanSet VALUES = BooleanSet.of(true, false);

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

    @Override
    public String name(final Boolean value) {
        return value.toString();
    }

    @Override
    public boolean isValidName(final String name) {
        return "true".equals(name) || "false".equals(name);
    }

    @Override
    public Boolean value(final String name) {
        return switch (name) {
            case "true" -> true;
            case "false" -> false;
            default -> throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        };
    }

    @Override
    public boolean isValidValue(final Boolean value) {
        return true;
    }

    @Override
    public @Unmodifiable BooleanSet values() {
        return VALUES;
    }
}
