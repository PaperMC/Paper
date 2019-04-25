package org.bukkit.craftbukkit.inventory.tags;

import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public final class DeprecatedItemTagType<T, Z> implements PersistentDataType<T, Z> {

    private final ItemTagType<T, Z> deprecated;

    public DeprecatedItemTagType(ItemTagType<T, Z> deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public Class<T> getPrimitiveType() {
        return deprecated.getPrimitiveType();
    }

    @Override
    public Class<Z> getComplexType() {
        return deprecated.getComplexType();
    }

    @Override
    public T toPrimitive(Z complex, PersistentDataAdapterContext context) {
        return this.deprecated.toPrimitive(complex, new DeprecatedItemAdapterContext(context));
    }

    @Override
    public Z fromPrimitive(T primitive, PersistentDataAdapterContext context) {
        return this.deprecated.fromPrimitive(primitive, new DeprecatedItemAdapterContext(context));
    }
}
