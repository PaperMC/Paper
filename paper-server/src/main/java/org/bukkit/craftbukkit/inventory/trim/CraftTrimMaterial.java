package org.bukkit.craftbukkit.inventory.trim;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class CraftTrimMaterial implements TrimMaterial, Handleable<net.minecraft.world.item.equipment.trim.TrimMaterial> {

    public static TrimMaterial minecraftToBukkit(net.minecraft.world.item.equipment.trim.TrimMaterial minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_MATERIAL, Registry.TRIM_MATERIAL);
    }

    public static TrimMaterial minecraftHolderToBukkit(Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> minecraft) {
        return CraftTrimMaterial.minecraftToBukkit(minecraft.value());
    }

    public static net.minecraft.world.item.equipment.trim.TrimMaterial bukkitToMinecraft(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> bukkitToMinecraftHolder(TrimMaterial bukkit) {
        Preconditions.checkArgument(bukkit != null);

        net.minecraft.core.Registry<net.minecraft.world.item.equipment.trim.TrimMaterial> registry = CraftRegistry.getMinecraftRegistry(Registries.TRIM_MATERIAL);

        if (registry.wrapAsHolder(CraftTrimMaterial.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<net.minecraft.world.item.equipment.trim.TrimMaterial> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own trim material without properly registering it.");
    }

    private final NamespacedKey key;
    private final net.minecraft.world.item.equipment.trim.TrimMaterial handle;

    public CraftTrimMaterial(NamespacedKey key, net.minecraft.world.item.equipment.trim.TrimMaterial handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    public net.minecraft.world.item.equipment.trim.TrimMaterial getHandle() {
        return this.handle;
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        if (true) return java.util.Objects.requireNonNull(org.bukkit.Registry.TRIM_MATERIAL.getKey(this), () -> this + " doesn't have a key"); // Paper
        return this.key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        if (!(this.handle.description().getContents() instanceof TranslatableContents)) throw new UnsupportedOperationException("Description isn't translatable!"); // Paper
        return ((TranslatableContents) this.handle.description().getContents()).getKey();
    }

    // Paper start - adventure
    @Override
    public net.kyori.adventure.text.Component description() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.handle.description());
    }
    // Paper end - adventure
}
