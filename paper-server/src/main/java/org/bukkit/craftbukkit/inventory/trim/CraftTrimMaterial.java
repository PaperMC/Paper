package org.bukkit.craftbukkit.inventory.trim;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class CraftTrimMaterial implements TrimMaterial {

    private final NamespacedKey key;
    private final net.minecraft.world.item.armortrim.TrimMaterial handle;

    public CraftTrimMaterial(NamespacedKey key, net.minecraft.world.item.armortrim.TrimMaterial handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    public net.minecraft.world.item.armortrim.TrimMaterial getHandle() {
        return handle;
    }
}
