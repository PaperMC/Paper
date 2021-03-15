package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.decoration.Paintings;
import org.bukkit.Art;

public class CraftArt {
    private static final BiMap<Paintings, Art> artwork;

    static {
        ImmutableBiMap.Builder<Paintings, Art> artworkBuilder = ImmutableBiMap.builder();
        for (MinecraftKey key : IRegistry.MOTIVE.keySet()) {
            artworkBuilder.put(IRegistry.MOTIVE.get(key), Art.getByName(key.getKey()));
        }

        artwork = artworkBuilder.build();
    }

    public static Art NotchToBukkit(Paintings art) {
        Art bukkit = artwork.get(art);
        Preconditions.checkArgument(bukkit != null);
        return bukkit;
    }

    public static Paintings BukkitToNotch(Art art) {
        Paintings nms = artwork.inverse().get(art);
        Preconditions.checkArgument(nms != null);
        return nms;
    }
}
