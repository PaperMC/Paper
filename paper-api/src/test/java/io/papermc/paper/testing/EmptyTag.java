package io.papermc.paper.testing;

import java.util.Collections;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record EmptyTag(NamespacedKey key) implements Tag<Keyed> {

    @SuppressWarnings("deprecation")
    public EmptyTag() {
        this(NamespacedKey.randomKey());
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public boolean isTagged(final Keyed item) {
        return false;
    }

    @Override
    public Set<Keyed> getValues() {
        return Collections.emptySet();
    }
}
