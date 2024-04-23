package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.item.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        if (angry) {
            getHandle().startPersistentAngerTimer();
        } else {
            getHandle().stopBeingAngry();
        }
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(EnumColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isWet() {
        return getHandle().isWet();
    }

    @Override
    public float getTailAngle() {
        return getHandle().getTailAngle();
    }

    @Override
    public boolean isInterested() {
        return getHandle().isInterested();
    }

    @Override
    public void setInterested(boolean flag) {
        getHandle().setIsInterested(flag);
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant {

        public static Variant minecraftToBukkit(WolfVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            IRegistry<WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);
            Variant bukkit = Registry.WOLF_VARIANT.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static Variant minecraftHolderToBukkit(Holder<WolfVariant> minecraft) {
            return minecraftToBukkit(minecraft.value());
        }

        public static WolfVariant bukkitToMinecraft(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT)
                    .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }

        public static Holder<WolfVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            IRegistry<WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);

            if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<WolfVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own wolf variant with out properly registering it.");
        }
    }
}
