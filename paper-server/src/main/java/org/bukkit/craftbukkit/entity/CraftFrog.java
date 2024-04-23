package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Entity;

public class CraftFrog extends CraftAnimals implements org.bukkit.entity.Frog {

    public CraftFrog(CraftServer server, Frog entity) {
        super(server, entity);
    }

    @Override
    public Frog getHandle() {
        return (Frog) entity;
    }

    @Override
    public String toString() {
        return "CraftFrog";
    }

    @Override
    public Entity getTongueTarget() {
        return getHandle().getTongueTarget().map(net.minecraft.world.entity.Entity::getBukkitEntity).orElse(null);
    }

    @Override
    public void setTongueTarget(Entity target) {
        if (target == null) {
            getHandle().eraseTongueTarget();
        } else {
            getHandle().setTongueTarget(((CraftEntity) target).getHandle());
        }
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

        public static Variant minecraftToBukkit(FrogVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            IRegistry<FrogVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.FROG_VARIANT);
            Variant bukkit = Registry.FROG_VARIANT.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static Variant minecraftHolderToBukkit(Holder<FrogVariant> minecraft) {
            return minecraftToBukkit(minecraft.value());
        }

        public static FrogVariant bukkitToMinecraft(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return CraftRegistry.getMinecraftRegistry(Registries.FROG_VARIANT)
                    .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }

        public static Holder<FrogVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            IRegistry<FrogVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.FROG_VARIANT);

            if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<FrogVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own frog variant with out properly registering it.");
        }
    }
}
