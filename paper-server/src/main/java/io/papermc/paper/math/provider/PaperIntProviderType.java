package io.papermc.paper.math.provider;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import io.papermc.paper.world.worldgen.DimensionType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperIntProviderType<T extends IntProvider> implements IntProviderType<T>, Holderable<net.minecraft.util.valueproviders.IntProviderType<?>> {

    public static IntProviderType<?> minecraftToBukkit(final net.minecraft.util.valueproviders.IntProviderType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.INT_PROVIDER_TYPE, RegistryAccess.registryAccess().getRegistry(RegistryKey.INT_PROVIDER_TYPE));
    }

    public static IntProviderType<?> minecraftHolderToBukkit(Holder<net.minecraft.util.valueproviders.IntProviderType<?>> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, RegistryAccess.registryAccess().getRegistry(RegistryKey.INT_PROVIDER_TYPE));
    }

    public static net.minecraft.util.valueproviders.IntProviderType<?> bukkitToMinecraft(IntProviderType<?> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.util.valueproviders.IntProviderType<?>> bukkitToMinecraftHolder(IntProviderType<?> bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.INT_PROVIDER_TYPE);
    }

    private final Holder<net.minecraft.util.valueproviders.IntProviderType<?>> holder;

    public PaperIntProviderType(final Holder<net.minecraft.util.valueproviders.IntProviderType<?>> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.util.valueproviders.IntProviderType<?>> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.equals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
