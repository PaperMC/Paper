package org.bukkit.craftbukkit.inventory.tags;

import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class DeprecatedItemTagType<P, C> implements PersistentDataType<P, C> {

    private final ItemTagType<P, C> deprecated;

    public DeprecatedItemTagType(ItemTagType<P, C> deprecated) {
        this.deprecated = deprecated;
    }

    @NotNull
    @Override
    public Class<P> getPrimitiveType() {
        return deprecated.getPrimitiveType();
    }

    @NotNull
    @Override
    public Class<C> getComplexType() {
        return deprecated.getComplexType();
    }

    @NotNull
    @Override
    public P toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
        return this.deprecated.toPrimitive(complex, new DeprecatedItemAdapterContext(context));
    }

    @NotNull
    @Override
    public C fromPrimitive(@NotNull P primitive, @NotNull PersistentDataAdapterContext context) {
        return this.deprecated.fromPrimitive(primitive, new DeprecatedItemAdapterContext(context));
    }
}
