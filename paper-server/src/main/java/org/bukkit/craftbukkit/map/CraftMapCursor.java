package org.bukkit.craftbukkit.map;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.map.MapCursor;

public final class CraftMapCursor {

    public static final class CraftType extends OldEnumHolderable<MapCursor.Type, MapDecorationType> implements MapCursor.Type {

        private static int count = 0;

        public static MapCursor.Type minecraftToBukkit(MapDecorationType minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.MAP_DECORATION_TYPE);
        }

        public static MapCursor.Type minecraftHolderToBukkit(Holder<MapDecorationType> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.MAP_DECORATION_TYPE);
        }

        public static MapDecorationType bukkitToMinecraft(MapCursor.Type bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<MapDecorationType> bukkitToMinecraftHolder(MapCursor.Type bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftType(final Holder<MapDecorationType> holder) {
            super(holder, count++);
        }

        @Override
        public byte getValue() {
            return (byte) CraftRegistry.getMinecraftRegistry(Registries.MAP_DECORATION_TYPE).getId(this.getHandle());
        }
    }
}
