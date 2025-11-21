package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftZombieNautilus extends CraftAbstractNautilus implements org.bukkit.entity.ZombieNautilus {
    public CraftZombieNautilus(final CraftServer server, final ZombieNautilus entity) {
        super(server, entity);
    }

    @Override
    public ZombieNautilus getHandle() {
        return (ZombieNautilus) this.entity;
    }

    @Override
    public org.bukkit.entity.ZombieNautilus.Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(org.bukkit.entity.ZombieNautilus.Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant extends HolderableBase<ZombieNautilusVariant> implements org.bukkit.entity.ZombieNautilus.Variant {

        public static org.bukkit.entity.ZombieNautilus.Variant minecraftHolderToBukkit(Holder<ZombieNautilusVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.ZOMBIE_NAUTILUS_VARIANT);
        }

        public static Holder<ZombieNautilusVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftVariant(final Holder<ZombieNautilusVariant> holder) {
            super(holder);
        }
    }
}
