package org.bukkit.craftbukkit.inventory.trim;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class CraftTrimMaterial implements TrimMaterial, io.papermc.paper.util.Holderable<net.minecraft.world.item.equipment.trim.TrimMaterial> { // Paper - switch to Holder

    public static TrimMaterial minecraftToBukkit(net.minecraft.world.item.equipment.trim.TrimMaterial minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_MATERIAL);
    }

    public static TrimMaterial minecraftHolderToBukkit(Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TRIM_MATERIAL); // Paper - switch to Holder
    }

    public static net.minecraft.world.item.equipment.trim.TrimMaterial bukkitToMinecraft(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> bukkitToMinecraftHolder(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.TRIM_MATERIAL); // Paper - switch to Holder
    }

    // Paper start - switch to Holder
    private final Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> holder;

    public static Object bukkitToObject(TrimMaterial bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((CraftTrimMaterial) bukkit).toBukkitSerializationObject(net.minecraft.world.item.equipment.trim.TrimMaterial.DIRECT_CODEC); // Paper - switch to Holder
    }

    public static TrimMaterial objectToBukkit(Object object) {
        Preconditions.checkArgument(object != null);

        return io.papermc.paper.util.Holderable.fromBukkitSerializationObject(object, net.minecraft.world.item.equipment.trim.TrimMaterial.CODEC, RegistryKey.TRIM_MATERIAL); // Paper - switch to Holder
    }

    @Override
    public boolean equals(final Object o) {
        return this.implEquals(o);
    }

    @Override
    public int hashCode() {
        return this.implHashCode();
    }

    @Override
    public String toString() {
        return this.implToString();
    }

    public CraftTrimMaterial(final Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> holder) {
        this.holder = holder;
        // Paper end - switch to Holder
    }

    @Override
    public Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> getHolder() { // Paper - switch to Holder
        return this.holder; // Paper - switch to Holder
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        if (!(this.getHandle().description().getContents() instanceof TranslatableContents)) throw new UnsupportedOperationException("Description isn't translatable!"); // Paper
        return ((TranslatableContents) this.getHandle().description().getContents()).getKey();
    }

    // Paper start - adventure
    @Override
    public net.kyori.adventure.text.Component description() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getHandle().description());
    }
    // Paper end - adventure
}
