package org.bukkit.craftbukkit.map;

import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.map.MapCursor;

public final class CraftMapCursor {

    public static final class CraftType implements MapCursor.Type, Handleable<MapDecorationType> {

        private static int count = 0;

        public static MapCursor.Type minecraftToBukkit(MapDecorationType minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.MAP_DECORATION_TYPE, Registry.MAP_DECORATION_TYPE);
        }

        public static MapCursor.Type minecraftHolderToBukkit(Holder<MapDecorationType> minecraft) {
            return CraftType.minecraftToBukkit(minecraft.value());
        }

        public static MapDecorationType bukkitToMinecraft(MapCursor.Type bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<MapDecorationType> bukkitToMinecraftHolder(MapCursor.Type bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.MAP_DECORATION_TYPE);
        }

        private final NamespacedKey key;
        private final MapDecorationType mapDecorationType;
        private final String name;
        private final int ordinal;

        public CraftType(NamespacedKey key, MapDecorationType mapDecorationType) {
            this.key = key;
            this.mapDecorationType = mapDecorationType;
            // For backwards compatibility, minecraft values will still return the uppercase name without the namespace,
            // in case plugins use for example the name as key in a config file to receive type specific values.
            // Custom types will return the key with namespace. For a plugin this should look than like a new type
            // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
            if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
                this.name = key.getKey().toUpperCase(Locale.ROOT);
            } else {
                this.name = key.toString();
            }
            this.ordinal = CraftType.count++;
        }

        @Override
        public MapDecorationType getHandle() {
            return this.mapDecorationType;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public int compareTo(MapCursor.Type type) {
            return this.ordinal - type.ordinal();
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public String toString() {
            // For backwards compatibility
            return this.name();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftType)) {
                return false;
            }

            return this.getKey().equals(((MapCursor.Type) other).getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }

        @Override
        public byte getValue() {
            return (byte) CraftRegistry.getMinecraftRegistry(Registries.MAP_DECORATION_TYPE).getId(this.getHandle());
        }
    }
}
