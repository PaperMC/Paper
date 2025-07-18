package org.bukkit.craftbukkit.inventory.trim;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class CraftTrimMaterial extends HolderableBase<net.minecraft.world.item.equipment.trim.TrimMaterial> implements TrimMaterial {

    public static TrimMaterial minecraftToBukkit(net.minecraft.world.item.equipment.trim.TrimMaterial minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_MATERIAL);
    }

    public static TrimMaterial minecraftHolderToBukkit(Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TRIM_MATERIAL);
    }

    public static net.minecraft.world.item.equipment.trim.TrimMaterial bukkitToMinecraft(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> bukkitToMinecraftHolder(TrimMaterial bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static Object bukkitToObject(TrimMaterial bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((CraftTrimMaterial) bukkit).toBukkitSerializationObject(net.minecraft.world.item.equipment.trim.TrimMaterial.DIRECT_CODEC);
    }

    public static TrimMaterial objectToBukkit(Object object) {
        Preconditions.checkArgument(object != null);

        return io.papermc.paper.util.Holderable.fromBukkitSerializationObject(object, net.minecraft.world.item.equipment.trim.TrimMaterial.CODEC, RegistryKey.TRIM_MATERIAL);
    }

    public CraftTrimMaterial(final Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> holder) {
        super(holder);
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        if (!(this.getHandle().description().getContents() instanceof TranslatableContents)) throw new UnsupportedOperationException("Description isn't translatable!"); // Paper
        return ((TranslatableContents) this.getHandle().description().getContents()).getKey();
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getHandle().description());
    }
}
