package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Cat;

public class CraftCat extends CraftTameableAnimal implements Cat {

    public CraftCat(CraftServer server, net.minecraft.world.entity.animal.Cat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cat getHandle() {
        return (net.minecraft.world.entity.animal.Cat) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCat";
    }

    @Override
    public Type getCatType() {
        return CraftType.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setCatType(Type type) {
        Preconditions.checkArgument(type != null, "Cannot have null Type");

        this.getHandle().setVariant(CraftType.bukkitToMinecraftHolder(type));
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        this.getHandle().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    public static class CraftType implements Type, Handleable<CatVariant> {
        private static int count = 0;

        public static Type minecraftToBukkit(CatVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.CAT_VARIANT, Registry.CAT_VARIANT);
        }

        public static Type minecraftHolderToBukkit(Holder<CatVariant> minecraft) {
            return CraftType.minecraftToBukkit(minecraft.value());
        }

        public static CatVariant bukkitToMinecraft(Type bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<CatVariant> bukkitToMinecraftHolder(Type bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.CAT_VARIANT);
        }

        private final NamespacedKey key;
        private final CatVariant catVariant;
        private final String name;
        private final int ordinal;

        public CraftType(NamespacedKey key, CatVariant catVariant) {
            this.key = key;
            this.catVariant = catVariant;
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
        public CatVariant getHandle() {
            return this.catVariant;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public int compareTo(Type variant) {
            return this.ordinal - variant.ordinal();
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

            return this.getKey().equals(((CraftType) other).getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }
}
