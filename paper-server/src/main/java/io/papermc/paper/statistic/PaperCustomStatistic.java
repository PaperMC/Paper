package io.papermc.paper.statistic;

import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperCustomStatistic implements CustomStatistic, Holderable<ResourceLocation> {

    private final Holder<ResourceLocation> holder;

    public PaperCustomStatistic(final Holder<ResourceLocation> holder) {
        this.holder = holder;
    }

    public static CustomStatistic minecraftToBukkit(final ResourceLocation minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.CUSTOM_STAT, Registry.CUSTOM_STAT);
    }

    public static ResourceLocation bukkitToMinecraft(final CustomStatistic bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    @Override
    public Holder<ResourceLocation> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public String translationKey() {
        return "stat." + this.getHandle().toString().replace(':', '.');
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
