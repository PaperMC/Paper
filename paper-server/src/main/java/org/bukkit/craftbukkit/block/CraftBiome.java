package org.bukkit.craftbukkit.block;

import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeBase;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftBiome implements Biome, Handleable<BiomeBase> {

    private static int count = 0;

    public static Biome minecraftToBukkit(BiomeBase minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BIOME, Registry.BIOME);
    }

    public static Biome minecraftHolderToBukkit(Holder<BiomeBase> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static BiomeBase bukkitToMinecraft(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }

        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<BiomeBase> bukkitToMinecraftHolder(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }

        IRegistry<BiomeBase> registry = CraftRegistry.getMinecraftRegistry(Registries.BIOME);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<BiomeBase> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own biome base with out properly registering it.");
    }

    private final NamespacedKey key;
    private final BiomeBase biomeBase;
    private final String name;
    private final int ordinal;

    public CraftBiome(NamespacedKey key, BiomeBase biomeBase) {
        this.key = key;
        this.biomeBase = biomeBase;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive biome specific values.
        // Custom biomes will return the key with namespace. For a plugin this should look than like a new biome
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT);
        } else {
            this.name = key.toString();
        }
        this.ordinal = count++;
    }

    @Override
    public BiomeBase getHandle() {
        return biomeBase;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int compareTo(@NotNull Biome biome) {
        return ordinal - biome.ordinal();
    }

    @NotNull
    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        // For backwards compatibility
        return name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftBiome otherBiome)) {
            return false;
        }

        return getKey().equals(otherBiome.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
