package org.bukkit.craftbukkit.inventory.tags;

import java.util.Objects;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * The {@link DeprecatedCustomTagContainer} is a simply wrapper implementation
 * that wraps the new api to still be usable with the old api parts.
 */
@SuppressWarnings("unchecked")
public final class DeprecatedCustomTagContainer implements CustomItemTagContainer {

    private final PersistentDataContainer wrapped;

    public DeprecatedCustomTagContainer(PersistentDataContainer wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public <T, Z> void setCustomTag(NamespacedKey key, ItemTagType<T, Z> type, Z value) {
        if (Objects.equals(CustomItemTagContainer.class, type.getPrimitiveType())) {
            wrapped.set(key, new DeprecatedContainerTagType<>((ItemTagType<CustomItemTagContainer, Z>) type), value);
        } else {
            wrapped.set(key, new DeprecatedItemTagType<>(type), value);
        }
    }

    @Override
    public <T, Z> boolean hasCustomTag(NamespacedKey key, ItemTagType<T, Z> type) {
        if (Objects.equals(CustomItemTagContainer.class, type.getPrimitiveType())) {
            return wrapped.has(key, new DeprecatedContainerTagType<>((ItemTagType<CustomItemTagContainer, Z>) type));
        } else {
            return wrapped.has(key, new DeprecatedItemTagType<>(type));
        }
    }

    @Override
    public <T, Z> Z getCustomTag(NamespacedKey key, ItemTagType<T, Z> type) {
        if (Objects.equals(CustomItemTagContainer.class, type.getPrimitiveType())) {
            return wrapped.get(key, new DeprecatedContainerTagType<>((ItemTagType<CustomItemTagContainer, Z>) type));
        } else {
            return wrapped.get(key, new DeprecatedItemTagType<>(type));
        }
    }

    @Override
    public void removeCustomTag(NamespacedKey key) {
        wrapped.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public ItemTagAdapterContext getAdapterContext() {
        return new DeprecatedItemAdapterContext(this.wrapped.getAdapterContext());
    }

    public PersistentDataContainer getWrapped() {
        return wrapped;
    }
}
