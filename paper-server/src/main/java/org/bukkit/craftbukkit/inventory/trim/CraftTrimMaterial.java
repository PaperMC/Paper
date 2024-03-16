package org.bukkit.craftbukkit.inventory.trim;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class CraftTrimMaterial implements TrimMaterial, Handleable<net.minecraft.world.item.armortrim.TrimMaterial> {

    public static TrimMaterial minecraftToBukkit(net.minecraft.world.item.armortrim.TrimMaterial minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_MATERIAL, Registry.TRIM_MATERIAL);
    }

    public static net.minecraft.world.item.armortrim.TrimMaterial bukkitToMinecraft(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final NamespacedKey key;
    private final net.minecraft.world.item.armortrim.TrimMaterial handle;

    public CraftTrimMaterial(NamespacedKey key, net.minecraft.world.item.armortrim.TrimMaterial handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    public net.minecraft.world.item.armortrim.TrimMaterial getHandle() {
        return handle;
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return ((TranslatableContents) handle.description().getContents()).getKey();
    }
}
