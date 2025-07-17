package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;

public class CraftFrog extends CraftAnimals implements org.bukkit.entity.Frog {

    public CraftFrog(CraftServer server, Frog entity) {
        super(server, entity);
    }

    @Override
    public Frog getHandle() {
        return (Frog) this.entity;
    }

    @Override
    public Entity getTongueTarget() {
        return this.getHandle().getTongueTarget().map(net.minecraft.world.entity.Entity::getBukkitEntity).orElse(null);
    }

    @Override
    public void setTongueTarget(Entity target) {
        if (target == null) {
            this.getHandle().eraseTongueTarget();
        } else {
            this.getHandle().setTongueTarget(((CraftEntity) target).getHandle());
        }
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant extends OldEnumHolderable<Variant, FrogVariant> implements Variant {
        private static int count = 0;

        public static Variant minecraftToBukkit(FrogVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.FROG_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<FrogVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.FROG_VARIANT);
        }

        public static FrogVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<FrogVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }
        public CraftVariant(final Holder<FrogVariant> holder) {
            super(holder, count++);
        }
    }
}
