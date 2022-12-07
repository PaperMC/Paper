package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.Art;

public class CraftArt {
    private static final BiMap<Holder<PaintingVariant>, Art> artwork;

    static {
        ImmutableBiMap.Builder<Holder<PaintingVariant>, Art> artworkBuilder = ImmutableBiMap.builder();
        for (ResourceKey<PaintingVariant> key : BuiltInRegistries.PAINTING_VARIANT.registryKeySet()) {
            artworkBuilder.put(BuiltInRegistries.PAINTING_VARIANT.getHolderOrThrow(key), Art.getByName(key.location().getPath()));
        }

        artwork = artworkBuilder.build();
    }

    public static Art NotchToBukkit(Holder<PaintingVariant> art) {
        Art bukkit = artwork.get(art);
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public static Holder<PaintingVariant> BukkitToNotch(Art art) {
        Holder<PaintingVariant> nms = artwork.inverse().get(art);
        Preconditions.checkArgument(nms != null);
        return nms;
    }
}
