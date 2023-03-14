package org.bukkit.craftbukkit.util;

import org.bukkit.util.CachedServerIcon;

public class CraftIconCache implements CachedServerIcon {
    public final byte[] value;

    public CraftIconCache(final byte[] value) {
        this.value = value;
    }
}
