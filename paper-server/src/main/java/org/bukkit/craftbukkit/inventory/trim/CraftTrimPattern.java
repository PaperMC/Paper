package org.bukkit.craftbukkit.inventory.trim;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class CraftTrimPattern extends HolderableBase<net.minecraft.world.item.equipment.trim.TrimPattern> implements TrimPattern {

    public static TrimPattern minecraftToBukkit(net.minecraft.world.item.equipment.trim.TrimPattern minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_PATTERN);
    }

    public static TrimPattern minecraftHolderToBukkit(Holder<net.minecraft.world.item.equipment.trim.TrimPattern> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TRIM_PATTERN);
    }

    public static net.minecraft.world.item.equipment.trim.TrimPattern bukkitToMinecraft(TrimPattern bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.equipment.trim.TrimPattern> bukkitToMinecraftHolder(TrimPattern bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static Object bukkitToObject(TrimPattern bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((CraftTrimPattern) bukkit).toBukkitSerializationObject(net.minecraft.world.item.equipment.trim.TrimPattern.DIRECT_CODEC);
    }

    public static TrimPattern objectToBukkit(Object object) {
        Preconditions.checkArgument(object != null);

        return io.papermc.paper.util.Holderable.fromBukkitSerializationObject(object, net.minecraft.world.item.equipment.trim.TrimPattern.CODEC, RegistryKey.TRIM_PATTERN);
    }

    public CraftTrimPattern(Holder<net.minecraft.world.item.equipment.trim.TrimPattern> handle) {
        super(handle);
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
