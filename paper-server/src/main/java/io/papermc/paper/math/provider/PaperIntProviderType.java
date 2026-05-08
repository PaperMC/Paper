package io.papermc.paper.math.provider;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperIntProviderType<T extends IntProvider> extends HolderableBase<net.minecraft.util.valueproviders.IntProviderType<?>> implements IntProviderType<T> {

    public PaperIntProviderType(final Holder<net.minecraft.util.valueproviders.IntProviderType<?>> holder) {
        super(holder);
    }

    public static IntProviderType<?> minecraftToBukkit(final net.minecraft.util.valueproviders.IntProviderType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.INT_PROVIDER_TYPE);
    }

    public static IntProviderType<?> minecraftHolderToBukkit(final Holder<net.minecraft.util.valueproviders.IntProviderType<?>> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.INT_PROVIDER_TYPE);
    }

    public static net.minecraft.util.valueproviders.IntProviderType<?> bukkitToMinecraft(final IntProviderType<?> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.util.valueproviders.IntProviderType<?>> bukkitToMinecraftHolder(final IntProviderType<?> bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.INT_PROVIDER_TYPE);
    }
}
