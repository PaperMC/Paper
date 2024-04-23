package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.item.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Cat;

public class CraftCat extends CraftTameableAnimal implements Cat {

    public CraftCat(CraftServer server, EntityCat entity) {
        super(server, entity);
    }

    @Override
    public EntityCat getHandle() {
        return (EntityCat) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftCat";
    }

    @Override
    public Type getCatType() {
        return CraftType.minecraftHolderToBukkit(getHandle().getVariant());
    }

    @Override
    public void setCatType(Type type) {
        Preconditions.checkArgument(type != null, "Cannot have null Type");

        getHandle().setVariant(CraftType.bukkitToMinecraftHolder(type));
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(EnumColor.byId(color.getWoolData()));
    }

    public static class CraftType {

        public static Type minecraftToBukkit(CatVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            IRegistry<CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            return Registry.CAT_VARIANT.get(CraftNamespacedKey.fromMinecraft(registry.getKey(minecraft)));
        }

        public static Type minecraftHolderToBukkit(Holder<CatVariant> minecraft) {
            return minecraftToBukkit(minecraft.value());
        }

        public static CatVariant bukkitToMinecraft(Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            IRegistry<CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            return registry.get(CraftNamespacedKey.toMinecraft(bukkit.getKey()));
        }

        public static Holder<CatVariant> bukkitToMinecraftHolder(Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            IRegistry<CatVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<CatVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own cat variant with out properly registering it.");
        }
    }
}
