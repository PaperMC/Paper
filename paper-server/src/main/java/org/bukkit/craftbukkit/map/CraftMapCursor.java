package org.bukkit.craftbukkit.map;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.map.MapCursor;

public final class CraftMapCursor {

    public static final class CraftType {

        public static MapCursor.Type minecraftToBukkit(MapDecorationType minecraft) {
            Preconditions.checkArgument(minecraft != null);

            IRegistry<MapDecorationType> registry = CraftRegistry.getMinecraftRegistry(Registries.MAP_DECORATION_TYPE);
            MapCursor.Type bukkit = Registry.MAP_DECORATION_TYPE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static MapCursor.Type minecraftHolderToBukkit(Holder<MapDecorationType> minecraft) {
            return minecraftToBukkit(minecraft.value());
        }

        public static MapDecorationType bukkitToMinecraft(MapCursor.Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return CraftRegistry.getMinecraftRegistry(Registries.MAP_DECORATION_TYPE)
                    .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }

        public static Holder<MapDecorationType> bukkitToMinecraftHolder(MapCursor.Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            IRegistry<MapDecorationType> registry = CraftRegistry.getMinecraftRegistry(Registries.MAP_DECORATION_TYPE);

            if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<MapDecorationType> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own map cursor type without properly registering it.");
        }
    }
}
