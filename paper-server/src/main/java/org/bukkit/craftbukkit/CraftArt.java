package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.Art;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftArt implements Art, Handleable<PaintingVariant> {

    private static int count = 0;

    public static Art minecraftToBukkit(PaintingVariant minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.PAINTING_VARIANT, Registry.ART);
    }

    public static Art minecraftHolderToBukkit(Holder<PaintingVariant> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static PaintingVariant bukkitToMinecraft(Art bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<PaintingVariant> bukkitToMinecraftHolder(Art bukkit) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<PaintingVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.PAINTING_VARIANT);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<PaintingVariant> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own painting variant with out properly registering it.");
    }

    private final NamespacedKey key;
    private final PaintingVariant paintingVariant;
    private final String name;
    private final int ordinal;

    public CraftArt(NamespacedKey key, PaintingVariant paintingVariant) {
        this.key = key;
        this.paintingVariant = paintingVariant;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive art specific values.
        // Custom arts will return the key with namespace. For a plugin this should look than like a new art
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT);
        } else {
            this.name = key.toString();
        }
        this.ordinal = count++;
    }

    @Override
    public PaintingVariant getHandle() {
        return paintingVariant;
    }

    @Override
    public int getBlockWidth() {
        return paintingVariant.width();
    }

    @Override
    public int getBlockHeight() {
        return paintingVariant.height();
    }

    @Override
    public int getId() {
        return CraftRegistry.getMinecraftRegistry(Registries.PAINTING_VARIANT).getId(paintingVariant);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int compareTo(@NotNull Art art) {
        return ordinal - art.ordinal();
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

        if (!(other instanceof CraftArt otherArt)) {
            return false;
        }

        return getKey().equals(otherArt.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
