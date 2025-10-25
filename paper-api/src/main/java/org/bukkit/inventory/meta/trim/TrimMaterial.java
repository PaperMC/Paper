package org.bukkit.inventory.meta.trim;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.InlinedRegistryBuilderProvider;
import io.papermc.paper.registry.data.TrimMaterialRegistryEntry;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a material that may be used in an {@link ArmorTrim}.
 */
@NullMarked
public interface TrimMaterial extends Keyed, Translatable {

    /**
     * Creates an inlined trim material.
     *
     * @param value a consumer for the builder factory
     * @return the created trim material
     */
    @ApiStatus.Experimental
    static TrimMaterial create(final Consumer<RegistryBuilderFactory<TrimMaterial, ? extends TrimMaterialRegistryEntry.Builder>> value) {
        return InlinedRegistryBuilderProvider.instance().createTrimMaterial(value);
    }

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

    private static TrimMaterial getTrimMaterial(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    /**
     * Get the description of this {@link TrimMaterial}.
     *
     * @return the description
     */
    Component description();

    /**
     * @deprecated this method assumes that {@link #description()} will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    String getTranslationKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}. TrimMaterials can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}. TrimMaterials can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    default Key key() {
        return Keyed.super.key();
    }
}
