package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cat;

public class CraftCat extends CraftTameableAnimal implements Cat {

    public CraftCat(CraftServer server, net.minecraft.world.entity.animal.Cat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Cat getHandle() {
        return (net.minecraft.world.entity.animal.Cat) this.entity;
    }

    @Override
    public Type getCatType() {
        return CraftType.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setCatType(Type type) {
        Preconditions.checkArgument(type != null, "type cannot be null");

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

    public static class CraftType extends OldEnumHolderable<Type, CatVariant> implements Type {
        private static int count = 0;

        public static Type minecraftToBukkit(CatVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.CAT_VARIANT);
        }

        public static Type minecraftHolderToBukkit(Holder<CatVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.CAT_VARIANT);
        }

        public static CatVariant bukkitToMinecraft(Type bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<CatVariant> bukkitToMinecraftHolder(Type bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftType(final Holder<CatVariant> holder) {
            super(holder, count++);
        }
    }

    @Override
    public void setLyingDown(boolean lyingDown) {
        this.getHandle().setLying(lyingDown);
    }

    @Override
    public boolean isLyingDown() {
        return this.getHandle().isLying();
    }

    @Override
    public void setHeadUp(boolean headUp) {
        this.getHandle().setRelaxStateOne(headUp);
    }

    @Override
    public boolean isHeadUp() {
        return this.getHandle().isRelaxStateOne();
    }
}
