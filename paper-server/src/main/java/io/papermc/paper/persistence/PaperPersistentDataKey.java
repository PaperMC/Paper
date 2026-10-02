package io.papermc.paper.persistence;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import java.util.Objects;

@NullMarked
public class PaperPersistentDataKey<P, C> implements PersistentDataKey<P, C> {
    private final NamespacedKey key;
    private final PersistentDataType<P, C> type;

    public PaperPersistentDataKey(final NamespacedKey key, final PersistentDataType<P, C> type) {
        Preconditions.checkState(key != null, "The key cannot be null");
        Preconditions.checkState(type != null, "The type cannot be null");
        this.key = Objects.requireNonNull(key);
        this.type = Objects.requireNonNull(type);
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
    public PersistentDataType<P, C> getDataType() {
        return this.type;
    }
}
