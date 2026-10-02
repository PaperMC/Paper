package io.papermc.paper.persistence;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPersistentDataKey<C> implements PersistentDataKey<C> {
    private final NamespacedKey key;
    private final PersistentDataType<?, C> type;

    public PaperPersistentDataKey(final NamespacedKey key, final PersistentDataType<?, C> type) {
        Preconditions.checkState(key != null, "The key cannot be null");
        Preconditions.checkState(type != null, "The type cannot be null");
        this.key = key;
        this.type = type;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return this.key;
    }

    @Override
    public PersistentDataType<?, C> getDataType() {
        return this.type;
    }
}
