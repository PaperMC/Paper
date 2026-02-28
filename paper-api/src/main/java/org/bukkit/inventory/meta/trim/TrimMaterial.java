package org.bukkit.inventory.meta.trim;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a material that may be used in an {@link ArmorTrim}.
 */
public interface TrimMaterial extends RegistryElement<TrimMaterial>, Keyed, Translatable {

    // Start generate - TrimMaterial
    TrimMaterial AMETHYST = getTrimMaterial("amethyst");

    TrimMaterial COPPER = getTrimMaterial("copper");

    TrimMaterial DIAMOND = getTrimMaterial("diamond");

    TrimMaterial EMERALD = getTrimMaterial("emerald");

    TrimMaterial GOLD = getTrimMaterial("gold");

    TrimMaterial IRON = getTrimMaterial("iron");

    TrimMaterial LAPIS = getTrimMaterial("lapis");

    TrimMaterial NETHERITE = getTrimMaterial("netherite");

    TrimMaterial QUARTZ = getTrimMaterial("quartz");

    TrimMaterial REDSTONE = getTrimMaterial("redstone");

    TrimMaterial RESIN = getTrimMaterial("resin");
    // End generate - TrimMaterial

    @NotNull
    private static TrimMaterial getTrimMaterial(@NotNull @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    // Paper start - adventure
    /**
     * Get the description of this {@link TrimMaterial}.
     *
     * @return the description
     */
    net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component description();

    /**
     * @deprecated this method assumes that {@link #description()} will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.NotNull String getTranslationKey();
    // Paper end - adventure

    // Paper start - Registry#getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}. TrimMaterials can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    org.bukkit.@org.jetbrains.annotations.NotNull NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}. TrimMaterials can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    default net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return org.bukkit.Keyed.super.key();
    }
    // Paper end - Registry#getKey
}
