package io.papermc.paper.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperSimplePersistentDataKey<P> extends PaperPersistentDataKey<P, P> implements SimplePersistentDataKey<P> {

    public PaperSimplePersistentDataKey(final NamespacedKey key, final PersistentDataType<P, P> type) {
        super(key, type);
    }
}
