package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.WolfVariant;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, net.minecraft.world.entity.animal.Wolf wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return this.getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        if (angry) {
            this.getHandle().startPersistentAngerTimer();
        } else {
            this.getHandle().stopBeingAngry();
        }
    }

    @Override
    public net.minecraft.world.entity.animal.Wolf getHandle() {
        return (net.minecraft.world.entity.animal.Wolf) this.entity;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        this.getHandle().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isWet() {
        return this.getHandle().isWet;
    }

    @Override
    public float getTailAngle() {
        return this.getHandle().getTailAngle();
    }

    @Override
    public boolean isInterested() {
        return this.getHandle().isInterested();
    }

    @Override
    public void setInterested(boolean flag) {
        this.getHandle().setIsInterested(flag);
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant implements Variant, Handleable<WolfVariant> {

        public static Variant minecraftToBukkit(WolfVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.WOLF_VARIANT, Registry.WOLF_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<WolfVariant> minecraft) {
            return CraftVariant.minecraftToBukkit(minecraft.value());
        }

        public static WolfVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<WolfVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            net.minecraft.core.Registry<WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);

            if (registry.wrapAsHolder(CraftVariant.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<WolfVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own wolf variant with out properly registering it.");
        }

        private final NamespacedKey key;
        private final WolfVariant variant;

        public CraftVariant(NamespacedKey key, WolfVariant variant) {
            this.key = key;
            this.variant = variant;
        }

        @Override
        public WolfVariant getHandle() {
            return this.variant;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.key.toString();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftVariant otherVariant)) {
                return false;
            }

            return this.getKey().equals(otherVariant.getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }
}
