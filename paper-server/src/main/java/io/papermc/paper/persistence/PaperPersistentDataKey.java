package io.papermc.paper.persistence;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPersistentDataKey<C>(
    NamespacedKey key,
    PersistentDataType<?, C> dataType
) implements PersistentDataKey<C> {

    public PaperPersistentDataKey {
        Preconditions.checkArgument(key != null, "The key cannot be null");
        Preconditions.checkArgument(dataType != null, "The type cannot be null");
    }
}
