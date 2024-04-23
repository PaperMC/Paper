package org.bukkit.craftbukkit.inventory.trim;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class CraftTrimPattern implements TrimPattern, Handleable<net.minecraft.world.item.armortrim.TrimPattern> {

    public static TrimPattern minecraftToBukkit(net.minecraft.world.item.armortrim.TrimPattern minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.TRIM_PATTERN, Registry.TRIM_PATTERN);
    }

    public static TrimPattern minecraftHolderToBukkit(Holder<net.minecraft.world.item.armortrim.TrimPattern> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static net.minecraft.world.item.armortrim.TrimPattern bukkitToMinecraft(TrimPattern bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.armortrim.TrimPattern> bukkitToMinecraftHolder(TrimPattern bukkit) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<net.minecraft.world.item.armortrim.TrimPattern> registry = CraftRegistry.getMinecraftRegistry(Registries.TRIM_PATTERN);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<net.minecraft.world.item.armortrim.TrimPattern> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own trim pattern without properly registering it.");
    }

    private final NamespacedKey key;
    private final net.minecraft.world.item.armortrim.TrimPattern handle;

    public CraftTrimPattern(NamespacedKey key, net.minecraft.world.item.armortrim.TrimPattern handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    public net.minecraft.world.item.armortrim.TrimPattern getHandle() {
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
