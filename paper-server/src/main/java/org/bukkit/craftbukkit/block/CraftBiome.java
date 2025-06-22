package org.bukkit.craftbukkit.block;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

@NullMarked
public class CraftBiome extends OldEnumHolderable<Biome, net.minecraft.world.level.biome.Biome> implements Biome {

    private static int count = 0;

    public static Biome minecraftToBukkit(net.minecraft.world.level.biome.Biome minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BIOME);
    }

    public static Biome minecraftHolderToBukkit(Holder<net.minecraft.world.level.biome.Biome> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.BIOME);
    }

    public static net.minecraft.world.level.biome.@Nullable Biome bukkitToMinecraft(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }

        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static @Nullable Holder<net.minecraft.world.level.biome.Biome> bukkitToMinecraftHolder(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftBiome(final Holder<net.minecraft.world.level.biome.Biome> holder) {
        super(holder, count++);
    }

    /**
     * Implementation for the deprecated, API only, CUSTOM biome.
     * As per {@link #bukkitToMinecraft(Biome)} and {@link #bukkitToMinecraftHolder(Biome)} it cannot be
     * converted into an internal biome and only serves backwards compatibility reasons.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    public static class LegacyCustomBiomeImpl implements Biome {

        private static final NamespacedKey LEGACY_CUSTOM_KEY = new NamespacedKey("minecraft", "custom");
        private final int ordinal;

        public LegacyCustomBiomeImpl() {
            this.ordinal = count++;
        }

        @Override
        public NamespacedKey getKey() {
            return LEGACY_CUSTOM_KEY;
        }

        @Override
        public int compareTo(final Biome other) {
            return this.ordinal - other.ordinal();
        }

        @Override
        public String name() {
            return "CUSTOM";
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            final LegacyCustomBiomeImpl that = (LegacyCustomBiomeImpl) object;
            return ordinal == that.ordinal;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(ordinal);
        }

        @Override
        public String toString() {
            return "CUSTOM";
        }
    }
}
