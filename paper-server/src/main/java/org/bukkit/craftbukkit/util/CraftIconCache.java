package org.bukkit.craftbukkit.util;

import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.Nullable;

public class CraftIconCache implements CachedServerIcon {
    public final byte[] value;

    public CraftIconCache(final byte[] value) {
        this.value = value;
    }

    @Override
    public @Nullable String getData() {
        if (this.value == null) {
            return null;
        }
        return "data:image/png;base64," + new String(java.util.Base64.getEncoder().encode(this.value), java.nio.charset.StandardCharsets.UTF_8);
    }
}
