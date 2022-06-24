package io.papermc.paper.testplugin.datatypes;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BooleanDataType implements PersistentDataType<Byte, Boolean> {
    @Override
    public @NotNull Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public @NotNull Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public @NotNull Byte toPrimitive(@NotNull Boolean complex, @NotNull PersistentDataAdapterContext context) {
        return complex ? (byte) 1 : (byte) 0;
    }

    @Override
    public @NotNull Boolean fromPrimitive(@NotNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
        return primitive == 1;
    }
}
