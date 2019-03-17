package io.papermc.paper.testing;

import java.util.Collections;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

public record EmptyTag(NamespacedKey key) implements Tag<Keyed> {

    @SuppressWarnings("deprecation")
    public EmptyTag() {
        this(NamespacedKey.randomKey());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public boolean isTagged(@NotNull final Keyed item) {
        return false;
    }

    @Override
    public @NotNull Set<Keyed> getValues() {
        return Collections.emptySet();
    }
}
